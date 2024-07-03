package pl.xayanix.dpdgroupproject.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.repository.OrderRepository;
import pl.xayanix.dpdgroupproject.service.IEmailService;
import pl.xayanix.dpdgroupproject.service.IOrderService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KafkaConsumerService {

    OrderRepository orderRepository;
    IEmailService emailService;
    IOrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    /**
     * Listens to messages from the "order-topic" Kafka topic and processes incoming orders asynchronously.
     * If the order ID exists in the database, it updates the existing order and processes it.
     * If the order ID does not exist, it processes the incoming order as a new order.
     *
     * @param order The OrderDAO object received from Kafka message.
     */
    @Async("asyncExecutor")
    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void listenOrderTopic(OrderDAO order) {
        logger.info("Received order with ID: {}", order.getId());

        try {
            if (order.getId() != null) {
                Optional<OrderDAO> existingOrderOpt = updateExistingOrder(order);
                existingOrderOpt.ifPresent(this::processOrder);
            } else {
                this.processOrder(order);
            }
        } catch (Exception e) {
            logger.error("Error processing order with ID: {}", order.getId(), e);
        }
    }

    /**
     * Processes the given order by sending an email notification and saving the order asynchronously.
     *
     * @param order The OrderDAO object to process.
     */
    private void processOrder(OrderDAO order){
        emailService.sendEmail(order);
        orderService.saveOrderAsynchronously(order);
    }

    /**
     * Updates an existing order in the database if there are changes compared to the new order.
     *
     * @param order The updated OrderDAO object received from Kafka.
     * @return The updated existing OrderDAO object wrapped in Optional if changes were applied; Optional.empty() otherwise.
     */
    private Optional<OrderDAO> updateExistingOrder(OrderDAO order) {
        return orderRepository.findById(order.getId())
                .filter(existingOrder -> hasChanges(existingOrder, order))
                .map(existingOrder -> {
                    updateOrderFields(existingOrder, order);
                    return existingOrder;
                });
    }

    /**
     * Checks if there are differences between two OrderDAO objects.
     *
     * @param existingOrder The existing OrderDAO object from the database.
     * @param newOrder The new OrderDAO object received from Kafka.
     * @return true if there are differences between the orders; false otherwise.
     */
    private boolean hasChanges(OrderDAO existingOrder, OrderDAO newOrder) {
        return !existingOrder.equalsUpdate(newOrder);
    }

    /**
     * Updates specific fields of an existing OrderDAO object with values from a new OrderDAO object.
     *
     * @param existingOrder The existing OrderDAO object to update.
     * @param newOrder The new OrderDAO object containing updated field values.
     */
    private void updateOrderFields(OrderDAO existingOrder, OrderDAO newOrder) {
        if (newOrder.getShipmentNumber() != null) {
            existingOrder.setShipmentNumber(newOrder.getShipmentNumber());
        }
        if (newOrder.getReceiverEmail() != null) {
            existingOrder.setReceiverEmail(newOrder.getReceiverEmail());
        }
        if (newOrder.getReceiverCountryCode() != null) {
            existingOrder.setReceiverCountryCode(newOrder.getReceiverCountryCode());
        }
        if (newOrder.getSenderCountryCode() != null) {
            existingOrder.setSenderCountryCode(newOrder.getSenderCountryCode());
        }
        if (newOrder.getStatusCode() != null) {
            existingOrder.setStatusCode(newOrder.getStatusCode());
        }
    }

}
