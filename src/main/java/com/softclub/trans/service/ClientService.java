package com.softclub.trans.service;

import com.softclub.trans.DTO.ClientDTO;
import com.softclub.trans.Mapper.ClientMapper;
import com.softclub.trans.entity.Client;
import com.softclub.trans.entity.Order;
import com.softclub.trans.repository.ClientRepository;
import com.softclub.trans.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientMapper clientMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addClientAndUpdateOrders(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        List<Order> orders = client.getOrders();
        try {
            clientRepository.save(client);

            for (Order order : orders) {
                order.setClient(client);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }
}
