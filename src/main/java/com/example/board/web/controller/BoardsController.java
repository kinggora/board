package com.example.board.web.controller;

import com.example.board.web.model.*;
import com.example.board.web.repository.CategoryRepository;
import com.example.board.web.repository.CommentRepository;
import com.example.board.web.repository.FileRepository;
import com.example.board.web.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/free")
public class BoardsController {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") int id, Model model){
        postRepository.hitUp(id);
        Post findPost = postRepository.findPostById(id);
        model.addAttribute("post", findPost);

        List<AttachFile> fileList = fileRepository.findFiles(id);
        model.addAttribute("fileList", fileList);

        List<Comment> commentList = commentRepository.findComment(id);
        model.addAttribute("commentList", commentList);

        return "view";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute SearchCriteria criteria, BindingResult bindingResult, Model model){
        List<Post> postList = postRepository.findPosts(criteria);
        PageInfo pageInfo = postRepository.getPageInfo(criteria);
        model.addAttribute("postList", postList);
        model.addAttribute("pageInfo", pageInfo);

        model.addAttribute("page", criteria.getPage());
        model.addAttribute("categoryId", criteria.getCategoryId());
        model.addAttribute("searchWord", criteria.getSearchWord());

        List<Category> categories = categoryRepository.findCategories();
        model.addAttribute("categories", categories);
        return "list";
    }

    @PostMapping("/comment.do")
    public String saveComment(@RequestParam int id,
                              @RequestParam String comment,
                              RedirectAttributes redirectAttributes){

        if(comment != null && !comment.isBlank()){
            commentRepository.saveComment(id, comment);
        }
        redirectAttributes.addAttribute("id", id);
        return "redirect:/boards/free/view/{id}";
    }
}