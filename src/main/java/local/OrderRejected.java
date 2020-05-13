package local;

public class OrderRejected extends AbstractEvent {

    private Long orderId;

    public OrderRejected(){
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
