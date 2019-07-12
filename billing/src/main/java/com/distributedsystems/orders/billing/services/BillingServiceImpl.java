package com.distributedsystems.orders.billing.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.distributedsystems.orders.billing.entities.Order;
import com.distributedsystems.orders.billing.entities.OrderItem;
import com.distributedsystems.orders.billing.repositories.OrdersRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service("billingService")
public class BillingServiceImpl implements BillingService {
  private static final Log LOG = LogFactory.getLog(BillingServiceImpl.class);
  private static final String DEFAULT_EXCHANGE = "";
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private OrdersRepository ordersRepository;
  
  @Autowired
  private Queue financeQueue;


  @Override
  public void sendOrderToFinance(int orderId, byte[] orderPDF) {
	MessageProperties props = new MessageProperties();
	props.setHeader("orderId", orderId);
	props.setHeader("mimeType", "application/pdf");
    Message body = new Message(orderPDF, props);
    rabbitTemplate.send(DEFAULT_EXCHANGE, financeQueue.getName(), body);
  }

	@Override
	public void billOrder(int orderId) {
		Optional<Order> order = ordersRepository.findById(orderId);
		if(order.isPresent()) {
			LOG.info("[Billing] Generating bill for order'" + orderId + "' PDF ... ");
			sendOrderToFinance(orderId, createOrderPDF(order.get()));
			LOG.info("[Billing] Bill for order '" + orderId + "'complete. ");
		}
	}
	
	private byte[] createOrderPDF(Order order) {
		
		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
			Document document = new Document();
			PdfWriter.getInstance(document, outputStream);
			document.open();
			
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
			Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 13, BaseColor.BLACK);
			document.add(new Paragraph("Factura de la order de compra '" + order.getId() + "'", titleFont));
			document.add(new Paragraph("Datos de la compra", subtitleFont));
			document.add(createOrderInfoTable(order));
			document.add(new Paragraph("Detalle de la compra", subtitleFont));
			document.add(createOrderItemsTable(order));
			document.add(new Paragraph(String.format("Importe Total: $%.2f", order.getTotalAmount())));
			
			document.close();
			return outputStream.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new byte [0];
	}
	
	private PdfPTable createOrderInfoTable(Order order) {
		PdfPTable table = new PdfPTable(2);	
		table.setSpacingBefore(10);
		table.addCell("Cliente");
		table.addCell(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
		table.addCell("Fecha de order: ");
		table.addCell(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
		table.addCell("Numero de orden: ");
		table.addCell(order.getOrderNumber());
		return table;
	}
	
	private PdfPTable createOrderItemsTable(Order order) {
		PdfPTable table = new PdfPTable(5);	
		table.setSpacingBefore(10);
		addTableHeader(table, Stream.of("Producto", "Proveedor", "Precio Unitario", "Cantidad", "Total"));
		addRows(table, order);
		return table;
	}
	
	private void addTableHeader(PdfPTable table, Stream<String> columns) {
	    columns.forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}

	private void addRows(PdfPTable table, Order order) {
	    for(OrderItem item : order.getItems()) {	
	    	table.addCell(item.getProduct().getProductName());
	    	table.addCell(item.getProduct().getSupplier().getCompanyName());
	    	table.addCell(String.format("$%.2f",item.getUnitPrice()));
	    	table.addCell(String.format("%d", item.getQuantity()));
	    	table.addCell(String.format("$%.2f",item.getUnitPrice() * item.getQuantity()));
	    }
	}
}