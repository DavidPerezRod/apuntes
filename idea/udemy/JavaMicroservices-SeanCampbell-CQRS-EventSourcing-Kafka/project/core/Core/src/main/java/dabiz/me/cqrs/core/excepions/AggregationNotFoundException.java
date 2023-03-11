package dabiz.me.cqrs.core.excepions;

public class AggregationNotFoundException extends RuntimeException {
    public AggregationNotFoundException(String message){
        super(message);
    }
}
