package maxz.monitor.services.callers.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"name"})
public abstract class AbstractTask implements ITask {

    private final String name;

    public AbstractTask(String name) {
        this.name = name;
    }


}
