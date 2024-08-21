package com.softclub.trans.service;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPayment(Payment payment) {
        // Если сумма отрицательная, выбрасываем исключение
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма платежа должна быть положительной");
        }

        // Сохраняем платеж
        paymentRepository.save(payment);
    }
}
