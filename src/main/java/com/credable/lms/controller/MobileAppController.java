package com.credable.lms.controller;

import com.credable.lms.entity.LoanRequest;
import com.credable.lms.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MobileAppController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/loan/request")
    public ResponseEntity<LoanRequest> requestLoan(@RequestParam String customerNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(loanService.requestLoan(customerNumber, amount));
    }

    @GetMapping("/loan/status/{id}")
    public ResponseEntity<LoanRequest> getLoanStatus(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanStatus(id));
    }
}
