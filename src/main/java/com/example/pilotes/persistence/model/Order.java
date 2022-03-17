package com.example.pilotes.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id
  private String number;
  private int pilotes;
  private double orderTotal;
  @CreationTimestamp
  private LocalDateTime createDateTime;
  @OneToOne(cascade = CascadeType.ALL)
  private Address address;
  @OneToOne(cascade = CascadeType.ALL)
  private Client client;
}