package com.distributedsystems.orders.billing.services;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.billing.components.WarehouseConsumer;
import com.distributedsystems.orders.billing.entities.Order;
import com.distributedsystems.orders.billing.entities.OrderItem;
import com.distributedsystems.orders.billing.entities.Product;
import com.distributedsystems.orders.billing.repositories.OrdersRepository;
import com.distributedsystems.orders.billing.repositories.ProductsRepository;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
  private static final Log LOG = LogFactory.getLog(WarehouseConsumer.class);
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private OrdersRepository ordersRepository;
  
  @Autowired
  private ProductsRepository productsRepository;
  
  @Autowired
  private Queue warehouseQueue;

  @Value("${queue.warehouse.exchange}")
  private String warehouseExchange;

  @Override
  public void sendOrderToStockChecking(int orderId) {
    Message body = new Message(String.valueOf(orderId).getBytes(), new MessageProperties());
    rabbitTemplate.send(warehouseExchange, warehouseQueue.getName(), body);
  }

	@Override
	public void checkOrderItemsStock(int orderId) {
		Optional<Order> order = ordersRepository.findById(orderId);
		
		if(order.isPresent()) {
			List<OrderItem> outStock = productsOutStock(order.get());
			if(outStock.isEmpty()) {
				// TODO: Se envie a reserva
				LOG.info("Reservar");
			} else {
				// TODO: Enviar correo de los productos fuera de stock
				LOG.info("Fuera de stock");
			}
		}
	}
	
	private List<OrderItem> productsOutStock(Order order) {
		return order.getItems().stream()
			.filter(item -> {
				Optional<Product> warehouseProduct = productsRepository.findById(item.getId());
				
				if(!warehouseProduct.isPresent()) {
					return false;
				} 

				int stock = warehouseProduct.get().getStock();
				
				return stock == 0 || stock < item.getQuantity();
			})
			.collect(Collectors.toList());
	}
}