package com.tui.proof.service;

import com.tui.proof.model.OrderRequest;
import com.tui.proof.persistence.dao.OrderRepository;
import com.tui.proof.persistence.model.Address;
import com.tui.proof.persistence.model.Client;
import com.tui.proof.persistence.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int MAX_UPDATE_TIME_MINUTES = 5;
    private final OrderRepository orderRepository;

    public void createOrder(final OrderRequest orderRequest) {
        final Order order = buildOrder(orderRequest);

        orderRepository.saveAndFlush(order);
    }

    public void updateOrder(final OrderRequest orderRequest) {
        final String orderNumber = orderRequest.getOrder().getNumber();
        final Optional<Order> existingOrder = orderRepository.findById(orderNumber);
        if (existingOrder.isEmpty()) {
            throw new IllegalArgumentException("Order with number " + orderNumber + " does not exist.");
        }

        if (!canOrderBeUpdated(existingOrder.get())) {
            throw new IllegalArgumentException("Time's up. Order cannot be updated.");
        }

        orderRepository.saveAndFlush(buildOrder(orderRequest));
    }

    private Order buildOrder(OrderRequest orderRequest) {
        return Order.builder()
            .number(orderRequest.getOrder().getNumber())
            .address(Address.builder()
                .city(orderRequest.getOrder().getDeliveryAddress().getCity())
                .country(orderRequest.getOrder().getDeliveryAddress().getCountry())
                .postcode(orderRequest.getOrder().getDeliveryAddress().getPostcode())
                .street(orderRequest.getOrder().getDeliveryAddress().getStreet())
                .build())
            .client(Client.builder()
                .firstName(orderRequest.getClient().getFirstName())
                .lastName(orderRequest.getClient().getLastName())
                .telephone(orderRequest.getClient().getTelephone())
                .build())
            .pilotes(orderRequest.getOrder().getPilotes())
            .orderTotal(orderRequest.getOrder().getOrderTotal())
            .build();
    }

    private boolean canOrderBeUpdated(Order order) {
        return order.getCreateDateTime().plusMinutes(MAX_UPDATE_TIME_MINUTES).isBefore(LocalDateTime.now());
    }
}
