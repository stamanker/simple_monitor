package maxz.monitor.services.callers.exceptions;

public class SetupException extends RuntimeException {

    public SetupException(String message, Exception e) {
        super(message, e);
    }

}
