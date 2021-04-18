package maxz.monitor.services;

import lombok.extern.slf4j.Slf4j;
import maxz.monitor.services.callers.entities.HttpTask;
import maxz.monitor.services.callers.CallWrapper;
import maxz.monitor.services.callers.entities.GeneralTask;
import maxz.monitor.services.callers.entities.ITask;
import maxz.monitor.services.callers.entities.ResStatus;
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

    @Autowired
    private ExecutorService executor;
    @Autowired
    private GraphDataHolder graphDataHolder;

    @Scheduled(fixedDelay = 2_000)
    public void process() {
        ITask[] tasks = new ITask[] {
            new HttpTask("jws01", "http://google.com"),
            new GeneralTask("FreeMem") {

            },
            new GeneralTask("Threads") {

            },
        };
//        List<CompletableFuture> list = new ArrayList<>();
        for(ITask task : tasks) {
//            list.add(
                CompletableFuture.runAsync(() -> {
                    Response res;
                    if(task instanceof HttpTask) {
                        res = new CallWrapper().call((HttpTask)task);
                    } else {
                        if(task.getName().equalsIgnoreCase("FreeMem")) {
                            res = new Response(
                                    ResStatus.OK,
                                    Runtime.getRuntime().freeMemory()
                            );
                        } else if(task.getName().equalsIgnoreCase("Threads")) {
                            res = new Response(
                                    ResStatus.OK,
                                    Thread.activeCount()
                            );
                        } else {
                            throw new RuntimeException();
                        }
                    }
                    graphDataHolder.record(task.getName(), res.value);
                    //log.info("*** RequestWorker.process {} = {} (took {} ms)", task.getName(), res, res.value);
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
