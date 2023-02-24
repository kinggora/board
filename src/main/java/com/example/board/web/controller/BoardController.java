package com.example.board.web.controller;

import com.example.board.web.model.AttachFile;
import com.example.board.web.model.Category;
import com.example.board.web.model.Post;
import com.example.board.web.model.PostDto;
import com.example.board.web.repository.CategoryRepository;
import com.example.board.web.repository.FileRepository;
import com.example.board.web.repository.PostRepository;
import com.example.board.web.util.FileStore;
import com.example.board.web.validation.PostCreationValidator;
import com.example.board.web.validation.PostModifyingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/free")
public class BoardController {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;
    private final PostCreationValidator creationValidator;
    private final PostModifyingValidator modifyingValidator;
    private final FileStore fileStore;

    @GetMapping("/write")
    public String writeForm(Model model) {
        ArrayList<Category> categories = categoryRepository.findCategories();
        model.addAttribute("categories", categories);
        return "write";
    }

    @PostMapping("/write.do")
    public String write(@ModelAttribute PostDto dto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        creationValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("error", bindingResult);
            return "redirect:/board/free/write";
        }

        //POST insert
        Post post = Post.builder()
                .categoryId(dto.getCategoryId())
                .writer(dto.getWriter())
                .password(dto.getPassword())
                .title(dto.getTitle())
                .content(dto.getContent())
                .regDate(new Timestamp(System.currentTimeMillis()))
                .build();

        int id = postRepository.savePost(post);

        //FILE insert
        //파일 저장 (storage)
        List<AttachFile> attachFiles = fileStore.uploadFiles(id, dto.getFiles());
        //파일 정보 저장 (database)
        if(!attachFiles.isEmpty()){
            fileRepository.saveFile(attachFiles);
        }
        redirectAttributes.addAttribute("id", id);
        return "redirect:/boards/free/view/{id}";
    }

    @PostMapping("/modify")
    public String modifyForm(@RequestParam int id,
                             Model model){
        Post post = postRepository.findPostById(id);

        //수정 시도 중 비밀번호를 틀렸을 경우 데이터 유지
        //post.modifyPost(writer, title, content);
        model.addAttribute("post", post);

        List<AttachFile> fileList = fileRepository.findFiles(id);
        model.addAttribute("fileList", fileList);
        return "modify";
    }

    @PostMapping("/modify.do")
    public String modify(@ModelAttribute PostDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        modifyingValidator.validate(dto, bindingResult);

        if (!bindingResult.hasErrors()) {
            //POST update
            Post post = Post
                    .builder()
                    .postId(dto.getId())
                    .writer(dto.getWriter())
                    .password(dto.getPassword())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .modDate(new Timestamp(System.currentTimeMillis()))
                    .build();

            boolean updateResult = postRepository.updatePost(post);

            //FILE update
            if (updateResult) {
                //파일 저장 (storage)
                List<AttachFile> attachFiles = fileStore.uploadFiles(dto.getId(), dto.getFiles());
                //파일 정보 저장 (database)
                if(!attachFiles.isEmpty()){
                    fileRepository.saveFile(attachFiles);
                }
                redirectAttributes.addAttribute("id", dto.getId());
                return "redirect:/boards/free/view/{id}";
            } else {
                bindingResult.rejectValue("password", "mismatch");
            }
        }
        log.info("errors={}", bindingResult);
        return "modify";
    }

    @PostMapping("/delete.do")
    public String delete(@RequestParam int id, @RequestParam String password, RedirectAttributes redirectAttributes){
        //삭제 성공 -> 목록 이동
        //삭제 실패 -> 게시글 페이지
        boolean deleteResult = postRepository.deletePost(id, password);
        if(deleteResult){
            return "redirect:/boards/free/list";
        } else{
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addAttribute("status", false);
            return "redirect:/boards/free/view/{id}";
        }
    }

    @PostMapping("/download.do")
    public void downloadFile(@RequestParam int fileId, HttpServletResponse response){
        AttachFile findFile = fileRepository.findFileById(fileId);
        if(findFile != null){
            fileStore.downloadFile(findFile, response);
        }
    }
}
