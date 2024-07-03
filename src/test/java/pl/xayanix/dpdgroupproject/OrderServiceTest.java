package pl.xayanix.dpdgroupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.repository.OrderRepository;
import pl.xayanix.dpdgroupproject.service.impl.OrderService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, OrderDAO> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessOrder_ValidOrder() {
        OrderDAO order = new OrderDAO();
        order.setShipmentNumber("12345");
        order.setReceiverEmail("test@example.com");
        order.setReceiverCountryCode("US");
        order.setSenderCountryCode("CA");
        order.setStatusCode(100);

        orderService.processOrder(order);

        verify(kafkaTemplate, times(1)).send(eq("order-topic"), eq(order));
    }

    @Test
    public void testProcessOrder_NullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderService.processOrder(null));
    }

    @Test
    public void testProcessOrder_MissingShipmentNumber() {
        OrderDAO order = new OrderDAO();
        order.setReceiverEmail("test@example.com");
        order.setReceiverCountryCode("US");
        order.setSenderCountryCode("CA");

        assertThrows(IllegalArgumentException.class, () -> orderService.processOrder(order));
    }

    @Test
    public void testUpdateOrder_ValidUpdate() {
        Long orderId = 1L;
        OrderDAO orderUpdates = new OrderDAO();
        orderUpdates.setStatusCode(50);

        orderService.updateOrder(orderId, orderUpdates);

        verify(kafkaTemplate, times(1)).send(eq("order-topic"), eq(orderUpdates));
    }

    @Test
    public void testUpdateOrder_NullStatusCode() {
        Long orderId = 1L;
        OrderDAO orderUpdates = new OrderDAO();

        orderService.updateOrder(orderId, orderUpdates);

        verify(kafkaTemplate, times(1)).send(eq("order-topic"), eq(orderUpdates));
    }




}
