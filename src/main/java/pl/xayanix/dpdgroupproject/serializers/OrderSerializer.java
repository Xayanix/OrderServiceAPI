package pl.xayanix.dpdgroupproject.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;

import java.util.Map;

public class OrderSerializer implements Serializer<OrderDAO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, OrderDAO data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Cannot serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {

    }
}
