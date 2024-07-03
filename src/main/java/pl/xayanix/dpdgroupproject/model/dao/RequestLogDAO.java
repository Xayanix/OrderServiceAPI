package pl.xayanix.dpdgroupproject.model.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String requestUrl;
    String httpMethod;
    String ipAddress;
    LocalDateTime timestamp;
}
