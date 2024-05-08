/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.sessionserver;

import static org.mockito.Mockito.when;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * LogoutHandlerTest.
 */
public class LogoutHandlerTest {

  private LogoutHandler logoutHandler;
  private Headers headers;
  private Map<String, Integer> sessions;
  private Map<String, Instant> sessionCreationTimes;

  @Mock
  private HttpExchange exchange;

  /**
   * Setup tests.
   */
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    sessions = new HashMap<>();
    sessionCreationTimes = new HashMap<>();
    logoutHandler = new LogoutHandler(sessions, sessionCreationTimes);
    headers = new Headers();
    headers.add("Cookie",
        "sessionID=1234"); //Exchange object methods return Header Object but Exchange is mocked so Headers must be manually created
  }

  @Test
  public void testHandler_SessionNotExpired() throws IOException {

    //assemble
    sessions.put("1234", 1); //Fake login details since LoginHandler isn't called
    sessionCreationTimes.put("1234",
        Instant.now()); //Fake login details since LoginHandler isn't called
    when(exchange.getRequestHeaders()).thenReturn(headers);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(exchange.getResponseBody()).thenReturn(outputStream);

    //act
    logoutHandler.handle(exchange);

    //assert
    String[] response = outputStream.toString().split("Session ID: ");
    Assertions.assertEquals("1234", response[1]);
    Assertions.assertFalse(sessions.containsKey(response));
    Assertions.assertFalse(sessionCreationTimes.containsKey(response));
  }

  @Test
  public void testHandler_SessionExpired() throws IOException {

    //assemble
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    when(exchange.getRequestHeaders()).thenReturn(headers);
    when(exchange.getResponseBody()).thenReturn(outputStream);

    //act
    logoutHandler.handle(exchange);

    //assert
    String[] response = outputStream.toString().split("Session ID: ");
    Assertions.assertEquals("Session has already expired!", response[0]);
    Assertions.assertFalse(sessions.containsKey(response));
    Assertions.assertFalse(sessionCreationTimes.containsKey(response));
  }
}
