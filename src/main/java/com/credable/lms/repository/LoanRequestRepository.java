package com.credable.lms.repository;

import com.credable.lms.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    Optional<LoanRequest> findByCustomerNumberAndStatus(String customerNumber, String status);
}
