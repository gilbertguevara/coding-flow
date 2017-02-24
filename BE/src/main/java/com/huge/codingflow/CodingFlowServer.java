package com.huge.codingflow;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.huge.codingflow.service.CommentModule;
import com.huge.codingflow.web.CodingFlowUndertowService;
import com.huge.codingflow.web.JsonServiceModule;
import com.huge.codingflow.web.Service;

/**
 * Main class for server app
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 11:06 AM
 */
public class CodingFlowServer {
    public static void main(String[] args) {
        // Wire the injector
        Injector injector = Guice.createInjector(
                new CommentModule(),
                new JsonServiceModule()
        );

        Service service = injector.getInstance(CodingFlowUndertowService.class);
        service.start();
    }
}
