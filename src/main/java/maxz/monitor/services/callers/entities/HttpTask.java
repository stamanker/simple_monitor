package maxz.monitor.services.callers.entities;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"url"}, callSuper = true)
public class HttpTask extends AbstractTask {

    public String url;

    public HttpTask(String name, String url) {
        super(name);
        this.url = url;
    }

}
