package com.tui.proof.service;

import com.tui.proof.ws.controller.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

@Data
@AllArgsConstructor
class OrderClientSearchQueryCriteriaConsumer<OrderEntity> implements Consumer<SearchCriteria> {

  private static final String CLIENT_COLUMN = "client";
  private Predicate predicate;
  private CriteriaBuilder builder;
  private Root<OrderEntity> r;

  @Override
  public void accept(SearchCriteria param) {
    if (param.getOperation().equalsIgnoreCase(">")) {
      predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(CLIENT_COLUMN).get(param.getKey()), param.getValue().toString()));
    } else if (param.getOperation().equalsIgnoreCase("<")) {
      predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(CLIENT_COLUMN).get(param.getKey()), param.getValue().toString()));
    } else if (param.getOperation().equalsIgnoreCase(":")) {
      if (r.get(CLIENT_COLUMN).get(param.getKey()).getJavaType() == String.class) {
        predicate = builder.and(predicate, builder.like(r.get(CLIENT_COLUMN).get(param.getKey()), "%" + param.getValue() + "%"));
      } else {
        predicate = builder.and(predicate, builder.equal(r.get(CLIENT_COLUMN).get(param.getKey()), param.getValue()));
      }
    }
  }

  public Predicate getPredicate() {
    return predicate;
  }
}