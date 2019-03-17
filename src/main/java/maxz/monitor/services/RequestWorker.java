package maxz.monitor.services;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.callers.entities.CallData;
import maxz.monitor.services.callers.CallWrapper;
import maxz.monitor.services.callers.entities.Response;
//import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
//@Timed
public class RequestWorker {

    private final ExecutorService executor;
    private final GraphDataHolder graphDataHolder;

    @Autowired
    public RequestWorker(ExecutorService executor, GraphDataHolder dataHolder) {
        this.executor = executor;
        this.graphDataHolder = dataHolder;
    }

    @Scheduled(fixedDelay = 5_000)
    public void process() {
        log.info("...");
        System.out.println("...");
        CallData[] data = new CallData[] {
            new CallData("ping1", "http://google.com.ua/", null, "GET"),
        };
//        List<CompletableFuture> list = new ArrayList<>();
        for(CallData callerData : data) {
//            list.add(
                CompletableFuture.runAsync(() -> {
                    Response res = new CallWrapper().call(callerData);
                    graphDataHolder.record(callerData.name, res.duration);
                    log.info("*** RequestWorker.process {} = {} (took {} ms)", callerData.name, res, res.duration);
                }, executor)
//            )
            ;
        }
//        try {
//            CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()])).get();
//        } catch (Exception e) {
//            log.error("Error waiting: " + e.getMessage());
//        }
    }

}
