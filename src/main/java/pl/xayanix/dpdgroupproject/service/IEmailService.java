package pl.xayanix.dpdgroupproject.service;

import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;


public interface IEmailService {

	void sendEmail(OrderDAO orderDAO);

}
