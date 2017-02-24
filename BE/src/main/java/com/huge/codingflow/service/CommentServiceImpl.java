package com.huge.codingflow.service;

import com.google.inject.Inject;
import com.huge.codingflow.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: HUGE-gilbert
 * Date: 10/19/15
 * Time: 11:19 AM
 */
public final class CommentServiceImpl implements CommentService {
    private List<Comment> comments = new ArrayList<>();

    @Inject
    private CommentServiceImpl() {
        comments.add(new Comment("Peter Parker", "This is a comment."));
        comments.add(new Comment("John Doe", "This is *another* comment."));
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Comment> addComment(Comment comment) {
        comments.add(comment);
        return comments;
    }
}
