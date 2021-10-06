package com.tui.proof.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "clients")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
  @Id
  @GeneratedValue
  private Long id;
  private String firstName;
  private String lastName;
  private String telephone;
}
