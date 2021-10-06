package com.tui.proof.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue
  private Long id;
  private String street;
  private String postcode;
  private String city;
  private String country;
}
