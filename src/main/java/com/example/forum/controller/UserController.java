package com.example.forum.controller;

import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.repository.PostRepo;
import com.example.forum.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/profile")
    public String redirectProfile(@AuthenticationPrincipal User user){return "redirect:/users/" + user.getId();}

    @GetMapping("/users/{user}")
    public String viewProfile(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model){
        Iterable<Post> posts = postRepo.findPostsByAuthor_Id(currentUser.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "userProfile";
    }

    @PostMapping("/users/{user}")
    public String addProfileImg(@AuthenticationPrincipal User currentUser, @RequestParam("file") MultipartFile file, Model model, @PathVariable User user) throws IOException {
        if(file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists())
                uploadDir.mkdir();

            if(currentUser.getFilename() != null)
                new File(uploadPath + "/" + currentUser.getFilename()).delete();

            String uuidFile = UUID.randomUUID().toString();
            String fileResultName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + fileResultName));

            currentUser.setFilename(fileResultName);
        }

        userRepo.save(currentUser);
        model.addAttribute("user", currentUser);
        return "redirect:/profile";
    }
}
