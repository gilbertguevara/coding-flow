package com.huge.codingflow.web;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.huge.codingflow.web.handler.CommentHandler;
import com.huge.codingflow.web.handler.JsonHandler;
import com.huge.codingflow.web.handler.MainHandler;
import com.huge.codingflow.web.react.ReactRunner;
import com.huge.codingflow.web.react.impl.ReactRunnerImpl;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 11:01 AM
 */
public class JsonServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ReactRunner.class).to(ReactRunnerImpl.class).asEagerSingleton();
        bind(Service.class).to(CodingFlowUndertowService.class);
        bind(JsonHandler.class).asEagerSingleton();
        bind(MainHandler.class).asEagerSingleton();
        bind(CommentHandler.class).asEagerSingleton();
    }

    @Provides
    Configuration freemarkerConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}
