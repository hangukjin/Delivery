package local;

import local.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCompleted_OrderFromOrder(@Payload PaymentCompleted paymentCompleted){

        if(paymentCompleted.isMe()){
            System.out.println("##### listener paymentCompleted : " + paymentCompleted.toJson());

            Delivery delivery = new Delivery();
            delivery.setOrderId(paymentCompleted.getOrderId());
            delivery.setProduct(paymentCompleted.getProduct());
            delivery.setQty(paymentCompleted.getQty());
            delivery.setPrice(paymentCompleted.getPrice());
            delivery.setStatus("order_get");

            deliveryRepository.save(delivery);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelRequested_OrderCancelFromOrder(@Payload OrderCancelRequested orderCancelRequested){

        if(orderCancelRequested.isMe()){
            Delivery delivery = deliveryRepository.findById(orderCancelRequested.getOrderId()).get();
            if ("order_get".equals(delivery.getStatus())){
                // 취소할 수 있는 상태
                OrderCancelConfirmed orderCancelConfirmed = new OrderCancelConfirmed();
                orderCancelConfirmed.setOrderId(orderCancelRequested.getOrderId());
                orderCancelConfirmed.publish();

                delivery.setStatus("order_canceled");
                deliveryRepository.save(delivery);
            }
            else{
                // 취소할 수 없는 상태
                // Customer 쪽은 곧 Received로 바뀔 것

            }


        }
    }
}
