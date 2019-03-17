package maxz.monitor.services.callers;

import maxz.monitor.services.callers.exceptions.SetupException;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.System.*;

@Component
public class CommonCaller extends BaseCaller {

    public Response call(CallData data) {
        long start = currentTimeMillis();
        long took;
        ResStatus status;
        try {
            String x = processRequest(data.url, data.fileName, data.method);
            //out.println("x = " + x);
            if(x == null) {
                status = ResStatus.FAIL;
            } else {
                status = ResStatus.OK;
            }
        } catch (SetupException se) {
            status = ResStatus.SETUPERR;
        } catch (IOException e) {
            status = ResStatus.NETWORKERR;
        } finally {
            took = currentTimeMillis() - start;
        }
        return new Response(status, took);
    }



}
