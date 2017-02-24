package com.huge.codingflow.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.huge.codingflow.web.CodingFlowUndertowService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Objects;

/**
 * Handles the JSON test.
 */
public final class JsonHandler implements HttpHandler {
    private final ObjectMapper objectMapper;

    @Inject
    private JsonHandler(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, CodingFlowUndertowService.JSON_UTF8);
        exchange.getResponseSender().send(ByteBuffer.wrap(
                objectMapper.writeValueAsBytes(
                        Collections.singletonMap("message", "Hello, World!"))));
    }
}