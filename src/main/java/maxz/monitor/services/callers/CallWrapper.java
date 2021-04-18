package maxz.monitor.services.callers;

import maxz.monitor.services.callers.entities.HttpTask;
import maxz.monitor.services.callers.entities.ResStatus;
import maxz.monitor.services.callers.entities.Response;
import maxz.monitor.services.callers.exceptions.SetupException;

import static java.lang.System.*;

public class CallWrapper extends BaseCaller {

    public Response call(HttpTask data) {
        long start = currentTimeMillis();
        long took = 0;
        ResStatus status;
        try {
            String r = processRequest(data.url);
            if (r == null) {
                status = ResStatus.FAIL;
            } else {
                status = ResStatus.OK;
                took = currentTimeMillis() - start;
            }
        } catch (SetupException se) {
            status = ResStatus.SETUPERR;
        } catch (Exception e) {
            status = ResStatus.NETWORKERR;
        }
        return new Response(status, took);
    }


}
