package pl.xayanix.dpdgroupproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.repository.OrderRepository;
import pl.xayanix.dpdgroupproject.service.IEmailService;
import pl.xayanix.dpdgroupproject.service.IOrderService;
import pl.xayanix.dpdgroupproject.service.impl.KafkaConsumerService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class KafkaConsumerServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private IEmailService emailService;

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListenOrderTopic_NewOrder() {
        OrderDAO order = new OrderDAO();
        order.setShipmentNumber("12345");
        order.setReceiverEmail("test@example.com");
        order.setReceiverCountryCode("US");
        order.setSenderCountryCode("CA");

        kafkaConsumerService.listenOrderTopic(order);

        verify(orderService, times(1)).saveOrderAsynchronously(order);
        verify(emailService, times(1)).sendEmail(order);
    }

    @Test
    public void testListenOrderTopic_ExistingOrder() {
        OrderDAO order = new OrderDAO();
        order.setId(1L);
        order.setShipmentNumber("12345");
        order.setReceiverEmail("test@example.com");
        order.setReceiverCountryCode("US");
        order.setSenderCountryCode("CA");

        OrderDAO existingOrder = new OrderDAO();
        existingOrder.setId(1L);
        existingOrder.setShipmentNumber("12345");
        existingOrder.setReceiverEmail("old@example.com");
        existingOrder.setReceiverCountryCode("US");
        existingOrder.setSenderCountryCode("CA");

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(existingOrder));

        kafkaConsumerService.listenOrderTopic(order);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderService, times(1)).saveOrderAsynchronously(existingOrder);
        verify(emailService, times(1)).sendEmail(existingOrder);
    }

}
