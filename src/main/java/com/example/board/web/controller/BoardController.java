package com.example.board.web.controller;

import com.example.board.web.exception.InvalidPasswordException;
import com.example.board.web.model.*;
import com.example.board.web.service.BoardService;
import com.example.board.web.validation.ModifyValidator;
import com.example.board.web.validation.WriteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/free")
public class BoardController {

    private final BoardService boardService;
    private final WriteValidator writeValidator;
    private final ModifyValidator modifyValidator;

    @GetMapping("/write")
    public String writeForm(@ModelAttribute("post") PostDto dto, Model model) {
        List<Category> categories = boardService.findCategories();
        model.addAttribute("categories", categories);
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute("post") PostDto dto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        writeValidator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            List<Category> categories = boardService.findCategories();
            model.addAttribute("categories", categories);
            return "write";
        }

        int id = boardService.savePost(dto);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/boards/free/view/{id}";
    }

    @PostMapping("/modify")
    public String modifyForm(@RequestParam int id,
                             @ModelAttribute SearchCriteria criteria,
                             Model model){

        Post post = boardService.findPostById(id);
        model.addAttribute("post", post);

        List<AttachFile> fileList = boardService.findFiles(id);
        model.addAttribute("fileList", fileList);

        model.addAttribute("criteria", criteria);
        return "modify";
    }

    @PostMapping("/modify.do")
    public String modify(@ModelAttribute PostDto dto,
                         BindingResult bindingResult,
                         @ModelAttribute SearchCriteria criteria,
                         RedirectAttributes redirectAttributes) {

        modifyValidator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            redirectAttributes.addAttribute("id", dto.getId());
            return "redirect:/boards/free/view/{id}";
        }

        try {
            boardService.updatePost(dto);
        } catch (InvalidPasswordException e){
            // TODO 비밀번호 불일치 처리
            bindingResult.rejectValue("password", "mismatch");
            log.info("errors={}", bindingResult);
        }
        redirectAttributes.addAttribute("id", dto.getId());
        redirectAttributes.addAttribute("searchQueryString", criteria.generateSearchQueryString());
        return "redirect:/boards/free/view/{id}?{searchQueryString}";
    }

    @PostMapping("/delete.do")
    public String delete(@RequestParam int id, @RequestParam String password, @ModelAttribute SearchCriteria criteria, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("searchQueryString", criteria.generateSearchQueryString());

        try {
            boardService.deletePost(id, password);
        } catch (InvalidPasswordException e){
            redirectAttributes.addAttribute("id", id);
            //redirectAttributes.addAttribute("errors", new FieldError("post", "password", "등록시 입력한 비밀번호와 다릅니다."));
            return "redirect:/boards/free/view/{id}?{searchQueryString}";
        }
        return "redirect:/boards/free/list?{searchQueryString}";
    }


    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") int id, @ModelAttribute SearchCriteria criteria, Model model){
        boardService.postHitUp(id);

        Post findPost = boardService.findPostById(id);
        model.addAttribute("post", findPost);

        List<AttachFile> fileList = boardService.findFiles(id);
        model.addAttribute("fileList", fileList);

        List<Comment> commentList = boardService.findComment(id);
        model.addAttribute("commentList", commentList);

        model.addAttribute("criteria", criteria);

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
