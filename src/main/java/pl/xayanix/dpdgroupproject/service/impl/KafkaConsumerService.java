package pl.xayanix.dpdgroupproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.repository.OrderRepository;
import pl.xayanix.dpdgroupproject.service.IEmailService;
import pl.xayanix.dpdgroupproject.service.IOrderService;

@Service
public class KafkaConsumerService {

    private final OrderRepository orderRepository;
    private final IEmailService emailService;
    private final IOrderService orderService;

    @Autowired
    public KafkaConsumerService(OrderRepository orderRepository, IEmailService emailService, IOrderService orderService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderService = orderService;
    }

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
        if (order.getId() != null) {
            OrderDAO existingOrder = updateExistingOrder(order);
            if (existingOrder != null) {
                this.processOrder(existingOrder);
            }
            return;
        }

        this.processOrder(order);
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
     * @return The updated existing OrderDAO object if changes were applied; null otherwise.
     */
    private OrderDAO updateExistingOrder(OrderDAO order) {
        OrderDAO existingOrder = orderRepository.findById(order.getId()).orElse(null);
        if (existingOrder != null && hasChanges(existingOrder, order)) {
            updateOrderFields(existingOrder, order);
            return existingOrder;
        }

        return null;
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
