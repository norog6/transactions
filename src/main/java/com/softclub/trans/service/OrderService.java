package com.softclub.trans.service;

import com.softclub.trans.entity.Invoice;
import com.softclub.trans.entity.Order;
import com.softclub.trans.entity.Payment;
import com.softclub.trans.repository.InvoiceRepository;
import com.softclub.trans.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional
    public void createOrderWithPayment(Order order, Payment payment) {
        // Сохраняем заказ
        orderRepository.save(order);
        payment.setOrderId(order.getId());
        try {
            paymentService.createPayment(payment);
        } catch (Exception e) {
            System.out.println("Ошибка при создании платежа: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> getAllOrders() {
        return   orderRepository.findAll();
    }
    @Transactional
    public void updateOrderStatus(Long orderId, Double newAmount) {
        Optional<Order> orderOpt = orderRepository.findByIdForUpdate(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setAmount(newAmount);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }
    public void createOrderAndInvoice(Order order, Invoice invoice) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    // Сохранение заказа
                    Order savedOrder = orderRepository.save(order);

                    // Привязка инвойса к заказу
                    invoice.setOrderId(savedOrder.getId());

                    // Сохранение инвойса
                    invoiceRepository.save(invoice);

                } catch (Exception e) {
                    // Откат транзакции в случае ошибки
                    status.setRollbackOnly();
                    throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
                }
            }
        });
    }

}
