package com.apipessoas;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;

@IntegrationTest
public class BaseTest {

  protected WireMockServer server = new WireMockServer();

  @BeforeEach
  void setUp() {
    server.resetAll();
  }
}
