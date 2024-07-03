package pl.xayanix.dpdgroupproject.service;

import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;


public interface IEmailService {

	/**
	 * Sends an email.
	 *
	 * @param orderDAO The OrderDAO object containing email details.
	 */
	void sendEmail(OrderDAO orderDAO);

}
