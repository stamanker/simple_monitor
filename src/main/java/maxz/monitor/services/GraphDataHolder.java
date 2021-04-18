package maxz.monitor.services;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
@Slf4j
public class GraphDataHolder {

    private final Map<String, Map<String, List<Object[]>>> data = new ConcurrentSkipListMap<>();
    private final Map<String, String> groups = new HashMap<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss"); // MM-dd

    @PostConstruct
    private void setupGroups() {
        groups.put("jws01", "jws");
        groups.put("jws02", "jws");
        groups.put("jws03", "jws");
        groups.put("jws04", "jws");
        groups.put("pas", "jws");
        groups.put("FreeMem", "System");
        groups.put("Threads", "Threads");
    }

    public void record(String metric, Object value) {
        Map<String, List<Object[]>> groupMap = data.computeIfAbsent(groups.get(metric), g -> new ConcurrentHashMap<>());
        List<Object[]> sourceList = groupMap.computeIfAbsent(metric, s -> new LinkedList<>());
        sourceList.add(new Object[]{getTime(), value});
        //System.out.println("sourceList = " + sourceList.size());
        if (sourceList.size() > 500) {
            sourceList.remove(0);
        }
    }

    public Collection<Object[]> getByGroup(String group) {
        long start = System.currentTimeMillis();
        try {
            Map<String, List<Object[]>> m = data.getOrDefault(group, new LinkedHashMap<>());
            return transform(m);
        } finally {
            log.info("getByGroup took = " + (System.currentTimeMillis() - start));
        }
    }

    public Collection<Object[]> getBySource(String s) {
        long start = System.currentTimeMillis();
        LinkedList<Object[]> result = new LinkedList<>();
        result.add(new Object[]{"Time", s});
        result.addAll(data.values().stream()
                .flatMap(it -> it.entrySet().stream())
                .filter(x -> x.getKey().equals(s))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseGet(LinkedList::new)
        );
        log.info("getBySource took = " + (System.currentTimeMillis() - start));
        return result;
    }

    private Collection<Object[]> transform(Map<String, List<Object[]>> map) {
        List<Object[]> result = new LinkedList<>();
        LinkedList<String> strings = new LinkedList<>(map.keySet());
        strings.addFirst("Time");
        Object[] headers = strings.toArray();
        result.add(headers);
        int depth = map.values().stream().mapToInt(List::size).max().orElse(0) - 1; // overall max depth
        //System.out.println("depth = " + depth);
        for (int d = 0; d < depth; d++) {
            Object[] row = new Object[headers.length];
            int i = 0;
            for (List<Object[]> list : map.values()) {
                if (d > list.size() - 1) {
                    row[i + 1] = 0; //empty data
                } else {
                    if(list.get(d)==null) {
                        System.out.println("it is");
                    }
                    row[i + 1] = list.get(d)[1];
                    if (row[0] == null && list.get(d)[0] != null) {
                        row[0] = list.get(d)[0];
                    }
                }
                i++;
            }
            result.add(row);
        }
        return result;
    }

    public static void main(String[] args) {
        GraphDataHolder x = new GraphDataHolder();
        x.setupGroups();
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

    @NotNull
    private String getTime() {
        return simpleDateFormat.format(new Date());
    }

}
