package pl.xayanix.dpdgroupproject.model.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String shipmentNumber;
    String receiverEmail;
    String receiverCountryCode;
    String senderCountryCode;
    Integer statusCode;

    /**
     * Compares this OrderDAO object with another OrderDAO object (updatedOrderDAO) to determine if they are equal for update purposes.
     *
     * @param updatedOrderDAO The updated OrderDAO object to compare against.
     * @return {@code true} if all relevant fields (shipmentNumber, receiverEmail, receiverCountryCode, senderCountryCode, statusCode) are equal
     *         between this object and updatedOrderDAO; {@code false} otherwise.
     */
    public boolean equalsUpdate(OrderDAO updatedOrderDAO) {
        if (updatedOrderDAO.getShipmentNumber() != null && !updatedOrderDAO.getShipmentNumber().equals(this.getShipmentNumber())) {
            return false;
        }

        if (updatedOrderDAO.getReceiverEmail() != null && !updatedOrderDAO.getReceiverEmail().equals(this.getReceiverEmail())) {
            return false;
        }

        if (updatedOrderDAO.getReceiverCountryCode() != null && !updatedOrderDAO.getReceiverCountryCode().equals(this.getReceiverCountryCode())) {
            return false;
        }

        if (updatedOrderDAO.getSenderCountryCode() != null && !updatedOrderDAO.getSenderCountryCode().equals(this.getSenderCountryCode())) {
            return false;
        }

        return updatedOrderDAO.getStatusCode() == null || updatedOrderDAO.getStatusCode().equals(this.getStatusCode());
    }


}
