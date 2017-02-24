package com.huge.codingflow.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.huge.codingflow.model.Comment;
import com.huge.codingflow.service.CommentService;
import com.huge.codingflow.web.react.ReactRunner;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 2:49 PM
 */
public class MainHandler implements HttpHandler {
    private final CommentService commentService;
    private final ObjectMapper objectMapper;
    private final Configuration configuration;
    private final ReactRunner reactRunner;

    @Inject
    private MainHandler(CommentService commentService, ObjectMapper objectMapper, Configuration configuration, ReactRunner reactRunner) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
        this.configuration = configuration;
        this.reactRunner = reactRunner;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        exchange.getResponseHeaders().put(
                Headers.CONTENT_TYPE, MediaType.HTML_UTF_8.toString());

        configuration.setDirectoryForTemplateLoading(new File("src/main/resources"));
        Template template = configuration.getTemplate("index.html");
        StringWriter writer = new StringWriter();
        Map<String, Object> modelMap = new HashMap<>();

        List<Comment> comments = commentService.getComments();
        modelMap.put("content", reactRunner.renderCommentBox(comments));
        modelMap.put("data", objectMapper.writeValueAsString(comments));

        template.process(modelMap, writer);

        exchange.getResponseSender().send(writer.toString());
    }
}
