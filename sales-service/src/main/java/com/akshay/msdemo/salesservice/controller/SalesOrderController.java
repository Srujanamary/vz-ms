package com.akshay.msdemo.salesservice.controller;

import com.akshay.msdemo.salesservice.model.Customer;
import com.akshay.msdemo.salesservice.model.Item;
import com.akshay.msdemo.salesservice.service.CustomerServiceProxy;
import com.akshay.msdemo.salesservice.model.SalesOrder;
import com.akshay.msdemo.salesservice.service.ItemProxy;
import com.akshay.msdemo.salesservice.service.OrderLineItemService;
import com.akshay.msdemo.salesservice.service.SalesOrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/sales-order", produces = "application/json")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private OrderLineItemService orderLineItemService;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private ItemProxy itemProxy;

    @GetMapping("/test-url")
    public String testingConnections(){
        System.out.println("hi there... connection successful!!!");
        return "Hi there!!!";
    }

    @PostMapping("/")
    public Long createOrder(@RequestBody SalesOrder orderData) {

        Boolean flag = true;
        Double calculatedTotal = 0.0;

        Customer customerEmailResponse = customerServiceProxy.getCustomerByEmail(orderData.getCustomerEmail());

        List<String> itemNames = orderData.getItemsList();

        if (customerEmailResponse != null) {
            System.out.println("email matched...");

            for(String item: itemNames){
                Item itemObject = itemProxy.findItemByName(item);
                if (itemObject != null) {
                    calculatedTotal += itemObject.getPrice();
//                    continue;
                }
                else {
                    flag=false;
                    System.out.println("'" + item + "' was not found in the items list");
                }
            }

            if (flag == true) {
                // send the orderData to service and persist the object
                System.out.println("flag = true , so everything looks good and now attempting to create the order");
                orderData.setTotalPrice(calculatedTotal);
                SalesOrder orderToPersist = salesOrderService.addNewOrder(orderData);
                orderLineItemService.addItemsToOrder(itemNames, orderToPersist.getId());
                return orderToPersist.getId();
            }
            else {
                // send message saying items currently not available in the store...
                System.out.println("Few items in your order are currently not available in the store...");
                return null;
            }
        }
        System.out.println("Customer with the provided email is not registered yet... [tracked in sales-order while placing the order!");
        return null;
    }


//    @PostMapping("/")
//    public Long createOrder(@RequestParam(value = "description") String description,
//                            @RequestParam(value = "itemsList") List<String> itemsList,
//                            @RequestParam(value = "customerEmail") String customerEmail) {
//        System.out.println("alive");
//        Boolean flag = true;
//        Double calculatedTotal = 0.0;
//
//        Customer customerEmailResponse = customerServiceProxy.getCustomerByEmail(customerEmail);
//        List<String> itemNames = itemsList;
//
//        if (customerEmailResponse != null) {
//            System.out.println("email matched...");
//
//            for(String item: itemNames){
//                if (itemProxy.findItemByName(item) != null) {
//                    calculatedTotal += itemProxy.findItemByName(item).getPrice();
////                    continue;
//                }
//                else {
//                    flag=false;
//                    System.out.println("'" + item + "' was not found in the items list");
//                }
//            }
//
//            if (flag == true) {
//                // send the orderData to service and persist the object
//                System.out.println("flag = true , so everything looks good and now attempting to create the order");
//
////                SalesOrder orderData = new SalesOrder();
////                orderData.setTotalPrice(calculatedTotal);
////                orderData.
//
//                SalesOrder orderToPersist = salesOrderService.addNewOrder(customerEmail, description, calculatedTotal);
//                orderLineItemService.addItemsToOrder(itemNames, orderToPersist.getId());
//                return orderToPersist.getId();
//            }
//            else {
//                // send message saying items currently not available in the store...
//                System.out.println("Few items in your order are currently not available in the store...");
//                return null;
//            }
//        }
//        System.out.println("Customer with the provided email is not registered yet... [tracked in sales-order while placing the order!");
//        return null;
//    }


    @GetMapping("/{customerEmail}" )
    public List<SalesOrder> getOrdersByEmail(@PathVariable("customerEmail") String customerEmail) {
        List<SalesOrder> matchingOrders = salesOrderService.findOrdersByEmail(customerEmail);
        if (matchingOrders != null) {
            System.out.println(matchingOrders + "...MO...");
            return matchingOrders;
        }
        return null;
    }

}
