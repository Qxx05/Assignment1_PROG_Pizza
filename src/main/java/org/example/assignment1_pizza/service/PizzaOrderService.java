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

        double basePrice = 0;

        // base price update depending on size ordered
        if (order.getSize().equals("SMALL")) basePrice = 8;
        if (order.getSize().equals("MEDIUM")) basePrice = 10;
        if (order.getSize().equals("LARGE")) basePrice = 12;

        // toppings cost
        int toppingCount = 0;
        if (order.getToppings() != null) {
            toppingCount = order.getToppings().size();
        }

        double toppingsCost = toppingCount * 1.25;
        double subtotal = (basePrice + toppingsCost) * order.getQuantity();

        // discounts
        double discount = 0;
        if (order.getQuantity() > 3) {
            discount = subtotal * 0.10;
        }

        // delivery fee
        double deliveryFee = 0;
        if (order.isDelivery()) {
            deliveryFee = 3.99;
        }

        double afterDiscount = subtotal - discount;
        double beforeTax = afterDiscount + deliveryFee;
        double tax = beforeTax * 0.13;
        double total = beforeTax + tax;

        // save the results in model
        order.setSubtotal(round(subtotal));
        order.setDiscountAmount(round(discount));
        order.setTax(round(tax));
        order.setTotal(round(total));
    }

    // to get the roundings correctly
    private double round(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
