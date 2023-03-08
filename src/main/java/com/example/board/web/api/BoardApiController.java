package com.example.board.web.api;

import com.example.board.web.exception.InvalidPasswordException;
import com.example.board.web.model.*;
import com.example.board.web.service.BoardService;
import com.example.board.web.validation.ModifyValidator;
import com.example.board.web.validation.WriteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardApiController {

    private final BoardService boardService;
    private final WriteValidator writeValidator;
    private final ModifyValidator modifyValidator;

    @GetMapping("/write")
    public List<Category> writeForm() {
        List<Category> categories = boardService.findCategories();
        return categories;
    }

    @PostMapping("/write")
    public ResponseEntity write(PostDto dto, BindingResult bindingResult) {

        writeValidator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        int id = boardService.savePost(dto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/modify")
    public Map<String, Object> modifyForm(@RequestParam Integer id){
        Map<String, Object> paramMap = new HashMap<>();
        Post post = boardService.findPostById(id);
        paramMap.put("post", post);

        List<AttachFile> fileList = boardService.findFiles(id);
        paramMap.put("fileList", fileList);

        return paramMap;
    }

    @PostMapping("/modify.do")
    public ResponseEntity modify(PostDto dto, BindingResult bindingResult) {

        modifyValidator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(bindingResult));
        }

        try {
            boardService.updatePost(dto);
        } catch (InvalidPasswordException e){
            bindingResult.rejectValue("password", "mismatch");
            log.info("errors={}", bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(bindingResult));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete.do")
    public ResponseEntity delete(@RequestParam Integer id, @RequestParam String password){
        try {
            boardService.deletePost(id, password);
        } catch (InvalidPasswordException e){
            //redirectAttributes.addAttribute("errors", new FieldError("post", "password", "등록시 입력한 비밀번호와 다릅니다."));
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }


    @GetMapping("/view/{seq}")
    public Map<String, Object> view(@PathVariable("seq") int id){
        boardService.postHitUp(id);

        Map<String, Object> paramMap = new HashMap<>();

        Post findPost = boardService.findPostById(id);
        paramMap.put("post", findPost);

        List<AttachFile> fileList = boardService.findFiles(id);
        paramMap.put("fileList", fileList);

        List<Comment> commentList = boardService.findComment(id);
        paramMap.put("commentList", commentList);

        return paramMap;
    }

    @GetMapping("/list")
    public Map<String, Object> list(SearchCriteria criteria){
        log.info("criteria={}", criteria);
        Map<String, Object> paramMap = new HashMap<>();

        List<Category> categories = boardService.findCategories();
        paramMap.put("categories", categories);

        List<Post> postList = boardService.findPosts(criteria);
        paramMap.put("postList", postList);

        PageManager pageManager = boardService.getPageManager(criteria);
        paramMap.put("pageManager", pageManager);

        return paramMap;
    }

    @PostMapping("/comment.do")
    public ResponseEntity saveComment(@RequestBody Comment comment){
        if(comment.getPostId() != null && StringUtils.hasText(comment.getContent())){
            boardService.saveComment(comment);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
