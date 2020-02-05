package com.example.forum.controller;

import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.repository.PostRepo;
import com.example.forum.domain.Comment;
import com.example.forum.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PostController {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

//    @GetMapping("/greeting")
//    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
//        return "greeting";
//    }

    @GetMapping
    public String main(Model model){
        Iterable<Post> posts = postRepo.findAll();
//        model.put("posts", posts);
        model.addAttribute("posts", posts);
        return "main";
    }
    @GetMapping("/post/{post}")
    public String viewPost(@PathVariable Post post, Model model, Map<String, Object> temp, User user){
        Iterable<Comment> comments = commentRepo.findCommentsByPost_Id(post.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Есть ли кто залогиненный
        if(principal instanceof UserDetails)
            model.addAttribute("currentUser", principal);

        return "postPage";
    }

    @GetMapping("/postAdd")
    public String postAdd(){
        return "postAdd";
    }

    @GetMapping("/postEdit/{post}")
    public String postEdit(@AuthenticationPrincipal User user, @PathVariable Post post, Model model){
        model.addAttribute("post", post);
        return "postEdit";
    }

    @PostMapping("/postAdd")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String title, @RequestParam String text, Map<String, Object> model){
        Post post = new Post(title, text, user);
        if(!post.getTitle().equals("") && !post.getText().equals("")) {
            postRepo.save(post);
            Iterable<Post> posts = postRepo.findAll();
            model.put("posts", posts);
            return "redirect:/";
        }
        else
            return "postAdd";
    }

    @PostMapping("/postEdit/{post}")
    public String edit(@AuthenticationPrincipal User user, @PathVariable Post post, @RequestParam("title") String title, @RequestParam("text") String text){
        if(!title.equals(""))
            post.setTitle(title);
        if(!text.equals(""))
            post.setText(text);
        postRepo.save(post);
        return "redirect:/post/{post}";
    }

}