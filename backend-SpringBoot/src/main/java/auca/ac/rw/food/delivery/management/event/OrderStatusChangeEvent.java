package auca.ac.rw.food.delivery.management.event;

import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import org.springframework.context.ApplicationEvent;

public class OrderStatusChangeEvent extends ApplicationEvent {
    private final Order order;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangeEvent(Object source, Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        super(source);
        this.order = order;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public Order getOrder() {
        return order;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }
} 