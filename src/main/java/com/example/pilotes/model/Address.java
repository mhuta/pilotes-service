package com.example.pilotes.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Address {
  @NotNull
  private String street;
  @NotNull
  private String postcode;
  @NotNull
  private String city;
  @NotNull
  private String country;
}
