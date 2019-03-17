package maxz.monitor.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class GraphDataHolder {

    private Map<String, Map<String, ArrayList<Object[]>>> data = new ConcurrentSkipListMap<>();
    private Map<String, String> groups = new HashMap<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

    @PostConstruct
    private void setupGroups() {
        groups.put("terms", "gw");
    }

    public void record(String source, Object d) {
        String group = groups.get(source);
        Map<String, ArrayList<Object[]>> groupMap = data.computeIfAbsent(group, g -> new LinkedHashMap<>());
        ArrayList<Object[]> sourceList = groupMap.computeIfAbsent(source, s -> new ArrayList<>());
        sourceList.add(new Object[]{getTime(), d});
        System.out.println("sourceList = " + sourceList.size());
        if(sourceList.size()>3_000) {
            sourceList.remove(0);
            sourceList.trimToSize();
        }
    }

    @NotNull
    private String getTime() {
        return simpleDateFormat.format(new Date());
    }

    public Collection<Object[]> getByGroup(String group) {
        Map<String, ArrayList<Object[]>> m = data.getOrDefault(group, new LinkedHashMap<>());
        return transform(m);
    }

    public Collection<Object[]> getBySource(String s) {
        List<Object[]> result = new ArrayList<>();
        result.addAll(data.values().stream()
                .flatMap(it->it.entrySet().stream())
                .filter(x -> x.getKey().equals(s))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseGet(ArrayList::new)
        );
        result.add(0, new Object[]{"Time", s});
        return result;
    }

    private Collection<Object[]> transform(Map<String, ArrayList<Object[]>> map) {
        List<Object[]> result = new ArrayList<>();
        List<String> strings = new ArrayList<>(map.keySet());
        strings.add(0, "Time");
        Object[] headers = strings.toArray();
        result.add(headers);
        int depth = map.values().stream().mapToInt(ArrayList::size).max().orElse(0); // overall max depth
        System.out.println("depth = " + depth);
        for (int d = 0; d < depth; d++) {
            Object[] x = new Object[headers.length];
            int i = 0;
            for (List<Object[]> list : map.values()) {
                if(d > list.size()-1) {
                    x[i+1] = 0;
                } else {
                    x[i+1] = list.get(d)[1];
                    if(list.get(d)[0]!=null && x[0]==null) {
                        x[0] = list.get(d)[0];
                    }
                }
                i++;
            }
            result.add(x);
        }
        return result;
    }

    public static void main(String[] args) {
        GraphDataHolder x = new GraphDataHolder();
        x.setupGroups();
        x.record("terms", 3453);
        x.record("terms", 3);
        x.record("terms", 20);
        x.record("grades", 100);
        x.record("grades", 200);
        x.record("spr", 5);
        x.record("spr", 1);
        Collection<Object[]> gw = x.getByGroup("gw");
        for (Object[] objects : gw) {
            System.out.println(Arrays.toString(objects));
        }
        System.out.println("---");
//        System.out.println(gw);
//        System.out.println(Arrays.toString(x.getByGroup("spr").stream().findFirst().get()));
//
        System.out.println("----");
        Collection<Object[]> terms = x.getBySource("terms");
        for (Object[] term : terms) {
            System.out.println(Arrays.toString(term));
        }
    }
}
