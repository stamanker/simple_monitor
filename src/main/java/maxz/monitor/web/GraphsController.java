package maxz.monitor.web;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.GraphDataHolder;
import maxz.monitor.services.callers.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController()
@RequestMapping("/api/v1/graph")
@Slf4j
public class GraphsController {

    @Autowired
    GraphDataHolder graphDataHolder;

    @RequestMapping(value = {"/metric/*"})
    public Collection<Object[]> getPlannerData(HttpServletRequest request) {
        log.info("request {} from {}", request.getRequestURI(), request.getRemoteAddr());
        String x = StringUtils.getAfterLast(request.getRequestURI(), "/");
        Collection<Object[]> byGroup = graphDataHolder.getBySource(x);
        printEntries(x, byGroup);
        return byGroup;
    }

    @RequestMapping(value = {"/group/*"})
    public Collection<Object[]> getPlannerData2(HttpServletRequest request) throws Exception {
        log.info("request {} from {}", request.getRequestURI(), request.getRemoteAddr());
        String x = StringUtils.getAfterLast(request.getRequestURI(), "/");
        Collection<Object[]> byGroup = graphDataHolder.getByGroup(x);
        return byGroup;
    }

    private void printEntries(String x, Collection<Object[]> byGroup) {
        //System.out.println("req = " + x);
        for (Object[] objects : byGroup) {
            //System.out.println(Arrays.toString(objects));
        }
    }

}
