package com.example.board.web.controller;

import com.example.board.web.model.*;
import com.example.board.web.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/free")
public class BoardsController {

    private final BoardService boardService;

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") int id, @ModelAttribute SearchCriteria criteria, Model model){
        boardService.postHitUp(id);

        Post findPost = boardService.findPostById(id);
        model.addAttribute("post", findPost);

        List<AttachFile> fileList = boardService.findFiles(id);
        model.addAttribute("fileList", fileList);

        List<Comment> commentList = boardService.findComment(id);
        model.addAttribute("commentList", commentList);

        String searchQueryString = criteria.generateSearchQueryString();
        model.addAttribute("searchQueryString", searchQueryString);

        return "view";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute SearchCriteria criteria, Model model){
        List<Category> categories = boardService.findCategories();
        model.addAttribute("categories", categories);

        List<Post> postList = boardService.findPosts(criteria);
        model.addAttribute("postList", postList);

        PageManager pageManager = boardService.getPageManager(criteria);
        model.addAttribute("pageManager", pageManager);

        return "list";
    }

    @PostMapping("/comment.do")
    public String saveComment(@RequestParam int id,
                              @RequestParam String comment,
                              RedirectAttributes redirectAttributes){

        if(StringUtils.hasText(comment)){
            boardService.saveComment(id, comment);
        }
        redirectAttributes.addAttribute("id", id);
        return "redirect:/boards/free/view/{id}";
    }

}
