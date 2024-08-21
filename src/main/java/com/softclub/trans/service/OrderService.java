package com.softclub.trans.service;

import com.softclub.trans.entity.Order;
import com.softclub.trans.entity.Payment;
import com.softclub.trans.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentService paymentService;

    @Transactional
    public void createOrderWithPayment(Order order, Payment payment) {
        // Сохраняем заказ
        orderRepository.save(order);

        try {
            // Создаем платеж с новой транзакцией
            paymentService.createPayment(payment);
        } catch (Exception e) {
            // Обработка исключения (платеж не был создан)
            System.out.println("Ошибка при создании платежа: " + e.getMessage());
        }
    }
}
