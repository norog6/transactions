package com.softclub.trans.service;

import com.softclub.trans.DTO.PaymentDTO;
import com.softclub.trans.Mapper.PaymentMapper;
import com.softclub.trans.entity.Payment;
import com.softclub.trans.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPayment(Payment payment) {
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма платежа должна быть положительной");
        }

        paymentRepository.save(payment);
    }

    @Transactional(timeout = 5)
    public void processPayment(PaymentDTO paymentDTO) {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        try {
            paymentRepository.save(payment);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        }
    }
}
