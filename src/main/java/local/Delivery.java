package local;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    private Long orderId;
    private String product;
    private Integer qty;
    private String status;

    @PrePersist
    @PostPersist
    @PostUpdate
    public void onPrePersist(){

        if ("receive".equals(this.getStatus())) {
            OrderReceived orderReceived = new OrderReceived();
            BeanUtils.copyProperties(this, orderReceived);
            orderReceived.publishAfterCommit();
        }

        if ("reject".equals(this.getStatus())) {
            OrderRejected orderRejected = new OrderRejected();
            BeanUtils.copyProperties(this, orderRejected);
            orderRejected.publishAfterCommit();
        }
    }



    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
