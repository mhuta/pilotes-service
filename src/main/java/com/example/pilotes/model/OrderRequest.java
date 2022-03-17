package com.example.pilotes.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderRequest {
  @NotNull
  private Order order;
  @NotNull
  private Client client;
}
