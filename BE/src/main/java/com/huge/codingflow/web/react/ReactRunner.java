package com.huge.codingflow.web.react;

import com.huge.codingflow.model.Comment;

import java.util.List;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 1:35 PM
 */
public interface ReactRunner {

    /**
     * Render the comment box html
     *
     * @param comments
     * @return
     */
    String renderCommentBox(List<Comment> comments);
}