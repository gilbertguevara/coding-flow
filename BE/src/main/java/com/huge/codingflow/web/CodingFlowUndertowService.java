package com.huge.codingflow.web;

import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.huge.codingflow.web.handler.CommentHandler;
import com.huge.codingflow.web.handler.JsonHandler;
import com.huge.codingflow.web.handler.MainHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.resource.FileResourceManager;
import org.xnio.Options;

import java.io.File;

import static io.undertow.Handlers.resource;

/**
 * Coding flow undertow Service
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 10:37 AM
 */
public class CodingFlowUndertowService implements Service {
    public static final String JSON_UTF8 = MediaType.JSON_UTF_8.toString();

    private final Injector injector;
    private Undertow server;

    @Inject
    private CodingFlowUndertowService(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void start() {
        server = Undertow.builder()
                .addHttpListener(8081, "0.0.0.0")
                .setBufferSize(1024 * 16)
                .setIoThreads(Runtime.getRuntime().availableProcessors() * 2) //this seems slightly faster in some configurations
                .setSocketOption(Options.BACKLOG, 10000)
                .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false) //don't send a keep-alive header for HTTP/1.1 requests, as it is not required
                .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true)
                .setServerOption(UndertowOptions.ENABLE_STATISTICS, false)
                .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
                .setHandler(Handlers.path()
                        .addPrefixPath("/", injector.getInstance(MainHandler.class))
                        .addPrefixPath("/js", resource(new FileResourceManager(new File("src/main/resources/js"), 1024)))
                        .addPrefixPath("/json", injector.getInstance(JsonHandler.class))
                        .addPrefixPath("/comments.json", injector.getInstance(CommentHandler.class)))
                .setWorkerThreads(200)
                .build();
        server.start();
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
