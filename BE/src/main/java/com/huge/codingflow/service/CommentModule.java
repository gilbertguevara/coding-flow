package com.huge.codingflow.service;

import com.google.inject.AbstractModule;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 11:21 AM
 */
public class CommentModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommentService.class).to(CommentServiceImpl.class).asEagerSingleton();
    }
}
