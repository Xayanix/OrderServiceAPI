package pl.xayanix.dpdgroupproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.service.IEmailService;


@Service
public class EmailService implements IEmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailService.class);

	// TODO: Implement async email service
	@Override
	public void sendEmail(OrderDAO orderDAO) {
		logger.info("Sent email to " + orderDAO.getReceiverEmail() + " with data: " + orderDAO);
	}

}
