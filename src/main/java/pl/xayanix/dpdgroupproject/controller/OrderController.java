/**
 * Controller class for handling HTTP requests related to orders.
 * Responsible for receiving and processing orders, as well as updating existing orders.
 */
package pl.xayanix.dpdgroupproject.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import pl.xayanix.dpdgroupproject.exceptions.InvalidOrderException;
import pl.xayanix.dpdgroupproject.model.dao.OrderDAO;
import pl.xayanix.dpdgroupproject.service.IOrderService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

	IOrderService orderService;

	/**
	 * Asynchronously handles POST requests to create new orders.
	 *
	 * @param order The OrderDAO object representing the order to be processed.
	 * @return A CompletableFuture containing ResponseEntity with HttpStatus.OK if the order was successfully processed,
	 *         or a ResponseEntity with HttpStatus.BAD_REQUEST if the order is invalid.
	 */
	@Async("asyncExecutor")
	@PostMapping
	public CompletableFuture<ResponseEntity<?>> receiveOrder(@RequestBody OrderDAO order) {
		try {
			orderService.processOrder(order);
		} catch (IllegalArgumentException ex) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidOrderException(ex.getMessage())));
		}

		return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).build());
	}

	/**
	 * Asynchronously handles PATCH requests to update existing orders.
	 *
	 * @param orderId The ID of the order to be updated.
	 * @param order The updated OrderDAO object containing the new order data.
	 * @return A CompletableFuture containing ResponseEntity with HttpStatus.OK if the order was successfully updated,
	 *         or a ResponseEntity with HttpStatus.BAD_REQUEST if the update request is invalid.
	 */
	@Async("asyncExecutor")
	@PatchMapping("/{orderId}")
	public CompletableFuture<ResponseEntity<?>> patchOrder(@PathVariable Long orderId, @RequestBody OrderDAO order) {
		try {
			orderService.updateOrder(orderId, order);
		} catch (IllegalArgumentException ex) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidOrderException(ex.getMessage())));
		}

		return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).build());
	}
}
