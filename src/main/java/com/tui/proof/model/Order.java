package com.tui.proof.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Order {
  @NotNull
  private String number;
  @NotNull
  private Address deliveryAddress;
  @NotNull
  private int pilotes;
  @NotNull
  private double orderTotal;

}
