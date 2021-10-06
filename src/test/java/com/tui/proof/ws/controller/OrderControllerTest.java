package com.tui.proof.ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.model.OrderRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

  public static final String ORDERS_PATH = "/orders";

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  public void shouldCreateNewOrder() throws Exception {
    mockMvc.perform(
      post(ORDERS_PATH)
        .content(asJsonString(buildDefaultOrderRequest()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  public void shouldNotCreateNewOrderWhenNumberOfPilotesIsInvalid() throws Exception {
    final OrderRequest orderRequest = buildDefaultOrderRequest();
    orderRequest.getOrder().setPilotes(1);

    mockMvc.perform(
        post(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Number of pilotes is invalid.")));
  }

  @Test
  public void shouldUpdateExistingOrder() throws Exception {
    final OrderRequest orderRequest = buildDefaultOrderRequest();
    mockMvc.perform(
        post(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    orderRequest.getOrder().setPilotes(15);

    mockMvc.perform(
        put(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  public void shouldNotUpdateNonExistingOrder() throws Exception {
    final OrderRequest orderRequest = buildDefaultOrderRequest();
    mockMvc.perform(
        post(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    orderRequest.getOrder().setNumber("123");

    mockMvc.perform(
        put(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Order with number 123 does not exist.")));
  }

  @Test
  public void shouldFindOrderForAuthenticatedUser() throws Exception {
    final OrderRequest orderRequest = buildDefaultOrderRequest();
    mockMvc.perform(
        post(ORDERS_PATH)
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    orderRequest.getOrder().setPilotes(15);

    mockMvc.perform(
        get(ORDERS_PATH + "?search=firstName:J")
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .with(httpBasic("user", "password")))
      .andExpect(status().isOk());
  }

  @Test
  public void shouldNotFindOrderForUnauthenticatedUser() throws Exception {
    final OrderRequest orderRequest = buildDefaultOrderRequest();

    mockMvc.perform(
        get(ORDERS_PATH + "?search=firstName:J")
          .content(asJsonString(orderRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  private OrderRequest buildDefaultOrderRequest() {
    final Address address = new Address();
    address.setStreet("Long");
    address.setCity("Gdansk");
    address.setPostcode("80126");
    address.setCountry("Poland");
    final Order order = new Order();
    order.setNumber(UUID.randomUUID().toString());
    order.setPilotes(5);
    order.setDeliveryAddress(address);
    order.setOrderTotal(10);
    final Client client = new Client();
    client.setFirstName("Jon");
    client.setLastName("Snow");
    client.setTelephone("+48600100100");
    final OrderRequest orderRequest = new OrderRequest();
    orderRequest.setOrder(order);
    orderRequest.setClient(client);
    return orderRequest;
  }

  private String asJsonString(final Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}