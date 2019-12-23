package com.akshay.msdemo.salesservice.controller;

import com.akshay.msdemo.salesservice.CustomerServiceProxy;
import com.akshay.msdemo.salesservice.model.SalesOrder;
import com.akshay.msdemo.salesservice.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sales-order")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @GetMapping("/test-url")
    public String testingConnections(){
        System.out.println("hi there... connection successful!!!");
        return "Hi there!!!";
    }

    @PostMapping("/")
    public SalesOrder createOrder(@RequestParam(value = "description") String description,
                                  @RequestParam(value = "customerEmail") String customerEmail
//                                  ,@RequestParam(value = "itemsList") String [] itemsList
    ) {
        System.out.println("reached here");
//        SalesOrder response = new SalesOrder();
        System.out.println("received all these customers from core service: " + customerServiceProxy.getAllCustomers());
        System.out.println("*********************" + customerServiceProxy.getCustomerByEmail(customerEmail));
//        System.out.println(response);

        return null;
    }

}