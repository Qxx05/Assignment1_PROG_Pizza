package org.example.assignment1_pizza.service;


import org.example.assignment1_pizza.model.PizzaOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaOrderService {

    // a list to store all orders
    private List<PizzaOrder> orders = new ArrayList<>();

    // id counter
    private int nextId = 1;

    public List<PizzaOrder> getAllOrders() {
        return orders;
    }

    // method to place order
    public PizzaOrder placeOrder(PizzaOrder order) {
        order.setId(nextId);
        nextId++;

        calculatePrices(order);

        orders.add(order);

        return order;
    }

    // pricing logic
    private void calculatePrices(PizzaOrder order) {

        double basePrice = 0.0;

        // base price update depending on size ordered
        if (order.getSize().equals("SMALL")) basePrice = 8.00;
        if (order.getSize().equals("MEDIUM")) basePrice = 10.00;
        if (order.getSize().equals("LARGE")) basePrice = 12.00;

        // toppings cost
        int toppingCount = 0;
        if (order.getToppings() != null) {
            toppingCount = order.getToppings().size();
        }

        double toppingsCost = toppingCount * 1.25;
        double unitPrice = basePrice + toppingsCost;
        double pizzaSubtotal = unitPrice * order.getQuantity();
        double discount = 0.0;

        if (order.getQuantity() > 3) {
            discount = pizzaSubtotal * 0.10;
        }

        double afterDiscountPizza = pizzaSubtotal - discount;

        double deliveryFee = 0.0;
        if (order.isDelivery()) {
            deliveryFee = 3.99;
        }

        double beforeTax = afterDiscountPizza + deliveryFee;
        double tax = beforeTax * 0.13;
        double total = beforeTax + tax;

        order.setSubtotal(round(pizzaSubtotal));
        order.setDiscountAmount(round(discount));
        order.setTax(round(tax));
        order.setTotal(round(total));
    }

    // to get the roundings correctly
    private double round(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
