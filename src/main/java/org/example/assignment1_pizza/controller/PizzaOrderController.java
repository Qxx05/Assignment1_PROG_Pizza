package org.example.assignment1_pizza.controller;



import org.example.assignment1_pizza.model.PizzaOrder;
import org.example.assignment1_pizza.service.PizzaOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PizzaOrderController {

    private PizzaOrderService service;

    public PizzaOrderController(PizzaOrderService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/order";
    }

    @GetMapping("/order")
    public String showOrderForm(Model model){

        // empty object for the form to fill
        model.addAttribute("pizzaOrder", new PizzaOrder());

        // dropdown values
        model.addAttribute("sizes", List.of("SMALL", "MEDIUM", "LARGE"));
        model.addAttribute("crusts", List.of("THIN", "REGULAR", "THICK"));

        // checkbox
        model.addAttribute("toppings", List.of("PEPPERONI", "MUSHROOMS", "OLIVES", "ONIONS", "JALAPENO", "PEPPERS"));

        // load html file orderForm.html
        return "orderForm";
    }

    // handle the form submit
    @PostMapping("/order")
    public String submitOrder(@ModelAttribute("pizzaOrder") PizzaOrder pizzaOrder, Model model) {

        if (pizzaOrder.getCustomerName() == null || pizzaOrder.getCustomerName().equals("")) {
            model.addAttribute("error", "customer name must not be empty");
            addFormLists(model);
            return "orderForm";
        }

        if (pizzaOrder.isDelivery()) {
            if (pizzaOrder.getDeliveryAddress() == null || pizzaOrder.getDeliveryAddress().equals("")) {
                model.addAttribute("error", "Delivery address required if delivery is selected!");
                addFormLists(model);
                return "orderForm";
            }
        }

        PizzaOrder savedOrder = service.placeOrder(pizzaOrder);

        model.addAttribute("order", savedOrder);

        if (savedOrder.getToppings() == null || savedOrder.getToppings().size() == 0) {
            model.addAttribute("toppingsText", "None");
        }else {
            model.addAttribute("toppingsText", savedOrder.getToppings().toString());
        }


        // load summary.html
        return "summary";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {

        // get orders from service
        model.addAttribute("orders", service.getAllOrders());

        return "history";
    }

    private void addFormLists(Model model) {
        model.addAttribute("sizes", List.of("SMALL", "MEDIUM", "LARGE"));
        model.addAttribute("crusts", List.of("THIN", "REGULAR", "THICK"));
        model.addAttribute("toppings", List.of("PEPPERONI", "MUSHROOM", "OLIVES", "ONIONS", "JALAPENO", "PEPPERS"));
    }
}
