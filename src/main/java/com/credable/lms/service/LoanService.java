package com.credable.lms.service;

import com.credable.lms.entity.LoanRequest;
import com.credable.lms.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private ScoringRestClient scoringRestClient;

    @Value("${credable.scoring.retries:5}")
    private int maxRetries;

    public LoanRequest requestLoan(String customerNumber, Double amount) {
        if (loanRequestRepository.findByCustomerNumberAndStatus(customerNumber, "PENDING").isPresent()) {
            throw new IllegalStateException("Ongoing loan request exists.");
        }
        LoanRequest loan = new LoanRequest();
        loan.setCustomerNumber(customerNumber);
        loan.setAmount(amount);
        loan.setStatus("PENDING");
        loan.setRetryCount(0);
        loan = loanRequestRepository.save(loan);

        // Initiate scoring
        String token = scoringRestClient.initiateQueryScore(customerNumber);
        loan.setScoringToken(token);
        processScore(loan); // Handle scoring synchronously for simplicity
        return loanRequestRepository.save(loan);
    }

    @Retryable(maxAttemptsExpression = "#{${credable.scoring.retries}}", backoff = @Backoff(delay = 2000))
    private void processScore(LoanRequest loan) {
        String response = scoringRestClient.queryScore(loan.getScoringToken());
        loan.setRetryCount(loan.getRetryCount() + 1);

        // Simplified: Assume any response means APPROVED
        if (response != null && !response.isEmpty()) {
            loan.setStatus("APPROVED");
        } else if (loan.getRetryCount() >= maxRetries) {
            loan.setStatus("FAILED");
        }
        loanRequestRepository.save(loan);
    }

    public LoanRequest getLoanStatus(Long id) {
        return loanRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    }
}
