package com.tui.proof.ws.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {
  private String key;
  private String operation;
  private Object value;
}
