package com.example.pilotes.ws.controller;

import com.example.pilotes.model.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderValidator {
  private static final List<Integer> VALID_NUM_OF_PILOTES = List.of(5, 10, 15);

  public void validateOrder(final OrderRequest orderRequest) {
    validateTelephoneNumber(orderRequest.getClient().getTelephone());
    validateNumerOfPilotes(orderRequest.getOrder().getPilotes());
  }

  private void validateTelephoneNumber(final String telephoneNumber) {
    // TODO add implementation
  }

  private void validateNumerOfPilotes(final int pilotes) {
    if (!VALID_NUM_OF_PILOTES.contains(pilotes)) {
      throw new IllegalArgumentException("Number of pilotes is invalid. " +
        "The portion can consist only of values: " + VALID_NUM_OF_PILOTES);
    }
  }
}
