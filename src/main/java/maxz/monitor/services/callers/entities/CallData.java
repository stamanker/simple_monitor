package maxz.monitor.services.callers.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@Data
@EqualsAndHashCode(of = {"url", "fileName", "method"})
public class CallData {

    public String name;
    public String url;
    public String fileName;
    public String method;

    public CallData(String name, String url, String fileName, String method) {
        this.name = name;
        this.url = url;
        this.fileName = fileName;
        this.method = method;
    }

}
