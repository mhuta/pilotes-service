package com.tui.proof.service;

import com.tui.proof.model.Address;
import com.tui.proof.persistence.model.Order;
import com.tui.proof.ws.controller.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

    private final EntityManager entityManager;

    public List<com.tui.proof.model.Order> searchOrder(final List<SearchCriteria> params) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> query = builder.createQuery(Order.class);
        final Root<Order> r = query.from(Order.class);

        final OrderClientSearchQueryCriteriaConsumer<Order> searchConsumer = new OrderClientSearchQueryCriteriaConsumer<>(builder.conjunction(), builder, r);
        params.forEach(searchConsumer);
        query.where(searchConsumer.getPredicate());

        return entityManager.createQuery(query).getResultList().stream()
            .map(this::mapToOrder)
            .collect(Collectors.toList());
    }

    private com.tui.proof.model.Order mapToOrder(Order orderEntity) {
        final com.tui.proof.model.Order order = new com.tui.proof.model.Order();
        order.setNumber(orderEntity.getNumber());
        order.setPilotes(orderEntity.getPilotes());
        order.setOrderTotal(orderEntity.getOrderTotal());
        final Address address = new Address();
        address.setCity(orderEntity.getAddress().getCity());
        address.setCountry(orderEntity.getAddress().getCountry());
        address.setPostcode(orderEntity.getAddress().getPostcode());
        address.setStreet(orderEntity.getAddress().getStreet());
        order.setDeliveryAddress(address);
        return order;
    }
}
