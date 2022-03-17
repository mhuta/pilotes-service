package com.example.pilotes.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Client {
  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  @NotNull
  private String telephone;
}
