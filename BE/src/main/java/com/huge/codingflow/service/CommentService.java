package com.huge.codingflow.service;

import com.huge.codingflow.model.Comment;

import java.util.List;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 11:18 AM
 */
public interface CommentService {
    /**
     * Returns all the comments
     * @return
     */
    List<Comment> getComments();

    /**
     * Add a comment and returns the full list of them
     * @param comment
     * @return
     */
    List<Comment> addComment(Comment comment);
}
