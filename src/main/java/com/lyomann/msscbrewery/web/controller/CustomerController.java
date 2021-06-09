package com.lyomann.msscbrewery.web.controller;

import com.lyomann.msscbrewery.services.CustomerService;
import com.lyomann.msscbrewery.web.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping({"/{customerId}"})
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID customerId){
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity handlePost(CustomerDto customerDto){
        CustomerDto savedDto = customerService.saveNewCustomer(customerDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location","/api/v1/customer/" + savedDto.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping({"/{customerId}"})
    public void handleUpdate(){

    }


}
