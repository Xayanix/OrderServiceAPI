package pl.xayanix.dpdgroupproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xayanix.dpdgroupproject.model.dao.RequestLogDAO;


public interface RequestLogRepository extends JpaRepository<RequestLogDAO, Long> {

}
