package pl.xayanix.dpdgroupproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;


public interface OrderRepository extends JpaRepository<OrderDAO, Long> {
}
