package com.example.forum.controller;

import com.example.forum.domain.Comment;
import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.repository.CommentRepo;
import com.example.forum.repository.PostRepo;
import com.example.forum.service.PostService;
import com.example.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PostController {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String main(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Post> postPage = postService.findPaginated(PageRequest.of(currentPage - 1 , pageSize));
        model.addAttribute("postPage", postPage);
        int totalPages = postPage.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "main";
    }
    @GetMapping("/post/{post}")
    public String viewPost(@PathVariable Post post, Model model){
        Iterable<Comment> comments = commentRepo.findCommentsByPost_Id(post.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Есть ли кто залогиненный
        if(principal instanceof UserDetails)
            model.addAttribute("currentUser", principal);
        else if(principal instanceof OidcUser){
            User user = (User) userService.loadUserByUsername(((OidcUser) principal).getName());
            model.addAttribute("currentUser", user);
        }

        return "postPage";
    }

    @GetMapping("/postAdd")
    public String postAdd(){
        return "postAdd";
    }

    @GetMapping("/postEdit/{post}")
    public String postEdit(@PathVariable Post post, Model model){
        model.addAttribute("post", post);
        return "postEdit";
    }

    @PostMapping("/postAdd")
    public String add(
            @AuthenticationPrincipal Principal principal,
            @RequestParam String title, @RequestParam String text, Map<String, Object> model){
        User user = (User) userService.loadUserByUsername(principal.getName());
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