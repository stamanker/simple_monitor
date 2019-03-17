package maxz.monitor.services.callers.entities;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Response {

    public ResStatus status;
    public long duration;

}
