package com.example.forum.service;

import com.example.forum.domain.Post;
import com.example.forum.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {
    private PostRepo postRepo;

    @Autowired
    public PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public Page<Post> findPaginated(Pageable pageable){
        List<Post> posts = postRepo.findAll(Sort.by("id").descending());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> list;

        if(posts.size() < startItem){
            list = Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }
        return new PageImpl<Post>(list, PageRequest.of(currentPage,pageSize), posts.size());
    }
}
