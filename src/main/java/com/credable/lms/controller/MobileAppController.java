package com.credable.lms.controller;

import com.credable.lms.entity.Customer;
import com.credable.lms.entity.LoanRequest;
import com.credable.lms.repository.CustomerRepository;
import com.credable.lms.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MobileAppController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<Customer> subscribe(@RequestParam String customerNumber) {
        Customer customer = new Customer();
        customer.setCustomerNumber(customerNumber);
        customer.setName("Test User"); // Mock name; replace with CBS KYC call in production
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @PostMapping("/loan/request")
    public ResponseEntity<LoanRequest> requestLoan(@RequestParam String customerNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(loanService.requestLoan(customerNumber, amount));
    }

    @GetMapping("/loan/status/{id}")
    public ResponseEntity<LoanRequest> getLoanStatus(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanStatus(id));
    }

    @GetMapping("/transactions/{customerNumber}")
    public ResponseEntity<String> getTransactions(@PathVariable String customerNumber) {
        // Mock transaction data; replace with CBS call in production
        String mockResponse = "{\"customerNumber\": \"" + customerNumber + "\", \"transactions\": []}";
        return ResponseEntity.ok(mockResponse);
    }
}
