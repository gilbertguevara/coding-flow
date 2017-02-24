package com.huge.codingflow.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.huge.codingflow.model.Comment;
import com.huge.codingflow.service.CommentService;
import com.huge.codingflow.web.CodingFlowUndertowService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: HUGE-gilbert
 * Date: 10/20/15
 * Time: 9:00 AM
 */
public final class CommentHandler implements HttpHandler {
    private final ObjectMapper objectMapper;
    private final CommentService commentService;

    @Inject
    private CommentHandler(CommentService commentService, ObjectMapper objectMapper) {
        this.commentService = Objects.requireNonNull(commentService);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        List<Comment> comments = new ArrayList<>();
        if (Methods.GET.equals(exchange.getRequestMethod())) {
            comments = commentService.getComments();
        } else if (Methods.POST.equals(exchange.getRequestMethod())) {
            exchange.startBlocking();
            Comment comment = objectMapper.readValue(exchange.getInputStream(), Comment.class);
            comments = commentService.addComment(comment);
        }

        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, CodingFlowUndertowService.JSON_UTF8);
        exchange.getResponseSender().send(ByteBuffer.wrap(
                objectMapper.writeValueAsBytes(comments)));
    }
}
