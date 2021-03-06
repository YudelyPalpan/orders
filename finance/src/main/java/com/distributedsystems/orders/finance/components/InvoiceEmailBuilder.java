package com.distributedsystems.orders.finance.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.distributedsystems.orders.finance.entities.Order;

@Component
public class InvoiceEmailBuilder  {

    @Autowired
    private TemplateEngine templateEngine;
   
    private String template;
    
    private Order order;

    public InvoiceEmailBuilder withTemplate(String template) {
        this.template = template;
        return this;
    }
    
    public InvoiceEmailBuilder withOrder(Order order) {
        this.order = order;
        return this;
    }
    
    public String build() {
        Context context = new Context();
        context.setVariable("order", order);
        return templateEngine.process(template, context);
    }
}