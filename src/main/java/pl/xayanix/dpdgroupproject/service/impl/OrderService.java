/**
 * Service implementation for managing order-related operations.
 * Validates orders, processes them asynchronously, and updates orders via Kafka messaging.
 */
package pl.xayanix.dpdgroupproject.service.impl;

import com.google.common.base.Preconditions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.repository.OrderRepository;
import pl.xayanix.dpdgroupproject.service.IOrderService;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderDAO> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderDAO> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Validates and processes an order by sending it to the Kafka topic for further processing.
     * Validates the order fields including shipment number, receiver email, receiver country code, and sender country code.
     *
     * @param order The OrderDAO object representing the order to process.
     * @throws IllegalArgumentException If any validation check fails for the order.
     */
    @Override
    @Transactional
    public void processOrder(OrderDAO order) {
        this.validateOrder(order);
        this.validateStatusCode(order);

        kafkaTemplate.send("order-topic", order);
    }

    /**
     * Validates and updates an existing order identified by orderId with the provided orderUpdates.
     * Sends the updated order to the "order-topic" Kafka topic for further processing.
     * Validates the order status code if present in the orderUpdates.
     *
     * @param orderId The ID of the order to update.
     * @param orderUpdates The OrderDAO object containing updated fields for the order.
     * @throws IllegalArgumentException If any validation check fails for the order status code.
     */
    @Override
    public void updateOrder(Long orderId, OrderDAO orderUpdates) {
        if (orderUpdates.getStatusCode() != null) {
            this.validateStatusCode(orderUpdates);
        }

        orderUpdates.setId(orderId);
        kafkaTemplate.send("order-topic", orderUpdates);
    }

    /**
     * Asynchronously saves an order to the database.
     *
     * @param order The OrderDAO object to save asynchronously.
     */
    @Async("asyncExecutor")
    public void saveOrderAsynchronously(OrderDAO order) {
        orderRepository.save(order);
    }

    /**
     * Validates the mandatory fields of an order: shipment number, receiver email, receiver country code, and sender country code.
     *
     * @param order The OrderDAO object to validate.
     * @throws IllegalArgumentException If any mandatory field is null or empty.
     */
    private void validateOrder(OrderDAO order) {
        Preconditions.checkArgument(order != null, "Order cannot be null");
        Preconditions.checkArgument(order.getShipmentNumber() != null && !order.getShipmentNumber().isEmpty(), "ShipmentNumber must be provided and cannot be empty");
        Preconditions.checkArgument(order.getReceiverEmail() != null && !order.getReceiverEmail().isEmpty(), "ReceiverEmail must be provided and cannot be empty");
        Preconditions.checkArgument(order.getReceiverCountryCode() != null && !order.getReceiverCountryCode().isEmpty(), "ReceiverCountryCode must be provided and cannot be empty");
        Preconditions.checkArgument(order.getSenderCountryCode() != null && !order.getSenderCountryCode().isEmpty(), "SenderCountryCode must be provided and cannot be empty");
    }

    /**
     * Validates the status code of an order.
     * Ensures the status code is between 0 and 100 (inclusive).
     *
     * @param order The OrderDAO object containing the status code to validate.
     * @throws IllegalArgumentException If the status code is not within the valid range (0-100).
     */
    private void validateStatusCode(OrderDAO order) {
        Preconditions.checkArgument(order.getStatusCode() != null && order.getStatusCode() >= 0 && order.getStatusCode() <= 100, "Value must be between 0 and 100");
    }
}
