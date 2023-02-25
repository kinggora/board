package com.example.board.web.controller;

import com.example.board.web.model.*;
import com.example.board.web.service.BoardService;
import com.example.board.web.validation.WriteValidator;
import com.example.board.web.validation.ModifyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/free")
public class BoardController {
    private final BoardService boardService;
    private final WriteValidator writeValidator;
    private final ModifyValidator modifyValidator;

    @GetMapping("/write")
    public String writeForm(Model model) {
        List<Category> categories = boardService.findCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postDto", new PostDto());
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute PostDto postDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        writeValidator.validate(postDto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            List<Category> categories = boardService.findCategories();
            model.addAttribute("categories", categories);
            return "write";
        }

        int id = boardService.savePost(postDto);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/boards/free/view/{id}";
    }

    @PostMapping("/modify")
    public String modifyForm(@RequestParam int id,
                             @RequestParam String searchQueryString,
                             Model model){

        Post post = boardService.findPostById(id);
        model.addAttribute("post", post);

        List<AttachFile> fileList = boardService.findFiles(id);
        model.addAttribute("fileList", fileList);

        model.addAttribute("searchQueryString", searchQueryString);
        return "modify";
    }

    @PostMapping("/modify.do")
    public String modify(@ModelAttribute PostDto postDto,
                         BindingResult bindingResult,
                         @RequestParam String searchQueryString,
                         RedirectAttributes redirectAttributes) {

        modifyValidator.validate(postDto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            redirectAttributes.addAttribute("id", postDto.getId());
            return "redirect:/boards/free/view/{id}";
        }

        boolean checkPassword = boardService.updatePost(postDto);
        if (!checkPassword) {
            //TODO 비밀번호 불일치 시 처리
            bindingResult.rejectValue("password", "mismatch");
            log.info("errors={}", bindingResult);
        }
        redirectAttributes.addAttribute("id", postDto.getId());
        redirectAttributes.addAttribute("searchQueryString", searchQueryString);
        return "redirect:/boards/free/view/{id}{searchQueryString}";
    }

    @PostMapping("/delete.do")
    public String delete(@RequestParam int id, @RequestParam String password, @RequestParam String searchQueryString, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("searchQueryString", searchQueryString);

        boolean checkPassword = boardService.deletePost(id, password);
        if(checkPassword){
            return "redirect:/boards/free/list{searchQueryString}";
        }
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("errors", new FieldError("post", "password", "등록 시 입력한 비밀번호와 다릅니다."));
        return "redirect:/boards/free/view/{id}{searchQueryString}";
    }

    @PostMapping("/download.do")
    public void downloadFile(@RequestParam int fileId, HttpServletResponse response){
        AttachFile attachFile = boardService.findFileById(fileId);

        if(attachFile != null){
            File file = new File(attachFile.getStoreDir(), attachFile.getStoreName());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                byte[] buffer = new byte[1024 * 8];
                while(true){
                    int count = fis.read(buffer);
                    if(count == -1){
                        break;
                    }
                    os.write(buffer, 0, count);
                }
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
