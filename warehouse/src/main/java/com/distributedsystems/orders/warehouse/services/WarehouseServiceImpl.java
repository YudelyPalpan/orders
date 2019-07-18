 package com.distributedsystems.orders.warehouse.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.warehouse.components.WarehouseConsumer;
import com.distributedsystems.orders.warehouse.entities.Order;
import com.distributedsystems.orders.warehouse.entities.OrderItem;
import com.distributedsystems.orders.warehouse.entities.Product;
import com.distributedsystems.orders.warehouse.repositories.OrdersRepository;
import com.distributedsystems.orders.warehouse.repositories.ProductsRepository;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
	private static final Log LOG = LogFactory.getLog(WarehouseConsumer.class);
	private static final String DEFAULT_EXCHANGE = "";
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private Queue reservationQueue;

	@Override
	public void checkOrderItemsStock(int orderId) {
		Optional<Order> order = ordersRepository.findById(orderId);

		if (order.isPresent()) {
			List<OrderItem> outStock = productsOutStock(order.get());
			if (outStock.isEmpty()) {
				LOG.info("[Warehouse] Make reservation for order '" + orderId + "'");
				Message body = new Message(String.valueOf(orderId).getBytes(), new MessageProperties());
				rabbitTemplate.send(DEFAULT_EXCHANGE, reservationQueue.getName(), body);
			} else {
				// TODO: Enviar correo de los productos fuera de stock
				LOG.info("[Warehouse] Fuera de stock");
			}
		}
	}

	private List<OrderItem> productsOutStock(Order order) {
		return order.getItems().stream().filter(item -> {
			Optional<Product> warehouseProduct = productsRepository.findById(item.getId());

			if (!warehouseProduct.isPresent()) {
				return false;
			}

			int stock = warehouseProduct.get().getStock();

			return stock == 0 || stock < item.getQuantity();
		}).collect(Collectors.toList());
	}
}