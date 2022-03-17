package com.example.pilotes.ws.controller;

import com.example.pilotes.model.Order;
import com.example.pilotes.model.OrderRequest;
import com.example.pilotes.notification.MiquelNotifyService;
import com.example.pilotes.service.OrderSearchService;
import com.example.pilotes.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
  private static final Pattern SEARCH_PATTERN = Pattern.compile("(\\w+?)([:<>])(\\w+?),");

  private final OrderService orderService;
  private final OrderSearchService orderSearchService;
  private final OrderValidator orderValidator;
  private final MiquelNotifyService miquelAsyncNotifyService;

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public void createOrder(@RequestBody final OrderRequest orderRequest) {
    orderValidator.validateOrder(orderRequest);
    orderService.createOrder(orderRequest);

    miquelAsyncNotifyService.notifyMiquel(orderRequest.getOrder().getPilotes());
  }

  @PutMapping
  public void updateOrder(@RequestBody final OrderRequest orderRequest) {
    orderValidator.validateOrder(orderRequest);
    orderService.updateOrder(orderRequest);
  }

  @GetMapping
  public List<Order> findOrders(@RequestParam(value = "search", required = false) String search) {
    final List<SearchCriteria> params = new ArrayList<>();
    if (search != null) {
      final Matcher matcher = SEARCH_PATTERN.matcher(search + ",");
      while (matcher.find()) {
        params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
      }
    }
    return orderSearchService.searchOrder(params);
  }
}
