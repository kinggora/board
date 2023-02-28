package com.example.board.web.service;

import com.example.board.web.exception.InvalidPasswordException;
import com.example.board.web.model.*;
import com.example.board.web.repository.CategoryRepository;
import com.example.board.web.repository.CommentRepository;
import com.example.board.web.repository.FileRepository;
import com.example.board.web.repository.PostRepository;
import com.example.board.web.util.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final FileStore fileStore;

    public int savePost(PostDto dto){
        Post post = Post.builder()
                .categoryId(dto.getCategoryId())
                .writer(dto.getWriter())
                .password(dto.getPassword())
                .title(dto.getTitle())
                .content(dto.getContent())
                .regDate(new Timestamp(System.currentTimeMillis()))
                .build();
        int id = postRepository.savePost(post);
        saveFile(id, dto.getNewFiles());
        return id;
    }

    public void updatePost(PostDto dto){
        if(!postRepository.checkPassword(dto.getId(), dto.getPassword())){
            throw new InvalidPasswordException();
        }
        Post post = Post
                .builder()
                .postId(dto.getId())
                .writer(dto.getWriter())
                .password(dto.getPassword())
                .title(dto.getTitle())
                .content(dto.getContent())
                .modDate(new Timestamp(System.currentTimeMillis()))
                .build();
        postRepository.updatePost(post);
        fileRepository.updateDeleteFile(dto.getId(), dto.getExistingFiles());
        saveFile(dto.getId(), dto.getNewFiles());
    }

    public void deletePost(int postId, String password){
        if(!postRepository.checkPassword(postId, password)){
            throw new InvalidPasswordException();
        }
        List<AttachFile> files = fileRepository.findFiles(postId);
        fileStore.deleteFiles(files);
        postRepository.deletePost(postId);
    }

    public PageManager getPageManager(SearchCriteria criteria){
        int totalCount = postRepository.getPostCount(criteria);
        return new PageManager(totalCount, criteria);
    }

    public Post findPostById(int id){
        return postRepository.findPostById(id);
    }

    public List<Post> findPosts(SearchCriteria criteria){
        int startRow = (criteria.getPage() - 1) * criteria.getPageSize();
        criteria.setStartRow(startRow);
        return postRepository.findPosts(criteria);
    }

    public void postHitUp(int id){
        postRepository.hitUp(id);
    }

    public AttachFile findFileById(int id){
        return fileRepository.findFileById(id);
    }

    public List<AttachFile> findFiles(int id){
        return fileRepository.findFiles(id);
    }

    public List<Category> findCategories(){
        return categoryRepository.findCategories();
    }

    public List<Comment> findComment(int postId){
        return commentRepository.findComment(postId);
    }

    public void saveComment(int postId, String content){
        Comment comment = Comment.builder()
                .postId(postId)
                .content(content)
                .regDate(new Timestamp(System.currentTimeMillis()))
                .build();
        commentRepository.saveComment(comment);
    }

    private void saveFile(int postId, List<MultipartFile> files){
        List<AttachFile> attachFiles = fileStore.uploadFiles(postId, files);
        if(!attachFiles.isEmpty()){
            fileRepository.saveFile(attachFiles);
        }
    }
}
