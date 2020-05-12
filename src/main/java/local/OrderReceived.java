package local;

public class OrderReceived extends AbstractEvent {

    private Long orderId;

    public OrderReceived(){
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
