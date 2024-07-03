package pl.xayanix.dpdgroupproject.service;

import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;


public interface IOrderService {

	void processOrder(OrderDAO order);
	void updateOrder(Long orderId, OrderDAO orderUpdates);
	void saveOrderAsynchronously(OrderDAO order);

}
