package com.example.forum.controller;

import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.domain.Comment;
import com.example.forum.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    @Autowired
    private CommentRepo commentRepo;

    @PostMapping("/post/{post}")
    private String addComment(@AuthenticationPrincipal User user, @RequestParam String text, Model model, @PathVariable Post post){
        Comment comment = new Comment(text, user, post);
        if(!comment.getText().equals("") && comment.getText() != null)
            commentRepo.save(comment);

        Iterable<Comment> comments = commentRepo.findAll();
        model.addAttribute(comments);

        return "redirect:/post/{post}";
    }

//    @GetMapping("/post/{post}")
//    public String showComments(Model model){
//        Iterable<Comment> comments = commentRepo.findAll();
//        model.addAttribute("comments", comments);
//
//        return "post_page";
//    }



}
