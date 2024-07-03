package pl.xayanix.dpdgroupproject.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;

import java.util.Map;

public class OrderDeserializer implements Deserializer<OrderDAO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public OrderDAO deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }

            return objectMapper.readValue(data, OrderDAO.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot deserialize: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {

    }
}
