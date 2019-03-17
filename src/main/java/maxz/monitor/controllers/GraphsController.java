package maxz.monitor.controllers;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.GraphDataHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RestController()
@RequestMapping("/api/v1/graph")
@Slf4j
public class GraphsController {

    @Autowired
    GraphDataHolder graphDataHolder;

    @RequestMapping(value = {"/terms", "/grades", "/sections", "/courses"})
    public Collection<Object[]> getPlannerData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //log.info("...");
        String u = request.getRequestURI();
        int i = u.lastIndexOf("/");
        String x = u.substring(i+1);
        Collection<Object[]> byGroup = graphDataHolder.getBySource(x);
        System.out.println("req = " + x);
        for (Object[] objects : byGroup) {
            System.out.println(Arrays.toString(objects));
        }
        return byGroup;
    }

    @RequestMapping(value = {"/planner", "/gw", "/spr"})
    public Collection<Object[]> getPlannerData2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String u = request.getRequestURI();
        int i = u.lastIndexOf("/");
        String x = u.substring(i+1);
        Collection<Object[]> byGroup = graphDataHolder.getByGroup(x);
        System.out.println("req = " + x);
        for (Object[] objects : byGroup) {
            System.out.println(Arrays.toString(objects));
        }
        return byGroup;
    }

}
