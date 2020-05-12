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
            delivery.setStatus("init");

            deliveryRepository.save(delivery);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelRequested_OrderCancelromOrder(@Payload OrderCancelRequested orderCancelRequested){

        if(orderCancelRequested.isMe()){
            if ("init".equals(deliveryRepository.findById(orderCancelRequested.getOrderId()).get().getStatus())){
                // 취소할 수 있는 상태
                deliveryRepository.deleteById(orderCancelRequested.getOrderId());
                OrderCancelConfirmed orderCancelConfirmed = new OrderCancelConfirmed();
                orderCancelConfirmed.setOrderId(orderCancelRequested.getOrderId());
                orderCancelConfirmed.publish();

            }
            else{
                // 취소할 수 없는 상태

            }


        }
    }
}
