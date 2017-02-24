package com.huge.codingflow.web.react.impl;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.huge.codingflow.model.Comment;
import com.huge.codingflow.web.react.ReactRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 1:37 PM
 */
public final class ReactRunnerImpl implements ReactRunner {
    private ObjectMapper objectMapper;

    private ThreadLocal<V8> runtimeHolder = new ThreadLocal<V8>() {
        @Override
        protected V8 initialValue() {
            V8 runtime = V8.createV8Runtime();
            try {
                JavaVoidCallback callback = (receiver, parameters) -> {
                    if (parameters.length() > 0) {
                        Object arg1 = parameters.get(0);
                        System.out.println(arg1);
                        if (arg1 instanceof Releasable) {
                            ((Releasable) arg1).release();
                        }
                    }
                };
                runtime.registerJavaMethod(callback, "print");

                runtime.executeVoidScript(read("src/main/resources/js/v8-polyfill.js"));
                runtime.executeVoidScript(read("src/main/resources/js/node_modules/react/dist/react.js"));
                runtime.executeVoidScript(read("src/main/resources/js/node_modules/showdown/dist/showdown.js"));
                runtime.executeVoidScript(read("src/main/resources/js/commentBox.js"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return runtime;
        }
    };

    @Inject
    private ReactRunnerImpl(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    private String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    @Override
    public String renderCommentBox(List<Comment> comments) {
        V8Array commentsArray = new V8Array(runtimeHolder.get());
        for (Comment comment : comments) {
            try {
                String objectScript = "var comment = " + objectMapper.writeValueAsString(comment);
                runtimeHolder.get().executeVoidScript(objectScript);
                V8Object commentObj = runtimeHolder.get().getObject("comment");
                commentsArray.push(commentObj);
                commentObj.release();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        V8Array parameters = new V8Array(runtimeHolder.get());
        parameters.push(commentsArray);
        String renderedCommentBox = runtimeHolder.get().executeStringFunction("renderServer", parameters);
        commentsArray.release();
        parameters.release();
        runtimeHolder.get().release();

        return renderedCommentBox;
    }
}
