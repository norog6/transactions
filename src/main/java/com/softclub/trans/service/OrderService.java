package com.softclub.trans.service;

import com.softclub.trans.DTO.InvoiceDTO;
import com.softclub.trans.DTO.OrderDTO;
import com.softclub.trans.Mapper.InvoiceMapper;
import com.softclub.trans.Mapper.OrderMapper;
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

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Transactional
    public void createOrderWithPayment(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrderId(order.getId());
        payment.setOrderId(order.getId());
        try {
            paymentService.createPayment(payment);
        } catch (Exception e) {
            System.out.println("Ошибка при создании платежа: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void updateOrderSum(Long orderId, Double newAmount) {
        Optional<Order> orderOpt = orderRepository.findByIdForUpdate(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setAmount(newAmount);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

    public void createOrderAndInvoice(OrderDTO orderDTO, InvoiceDTO invoiceDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    Order savedOrder = orderRepository.save(order);
                    invoice.setOrderId(savedOrder.getId());
                    invoiceRepository.save(invoice);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
                }
            }
        });
    }

}
