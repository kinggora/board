package com.example.board.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListDto {

    private List<PostViewDto> postList;
    private PageInfo pageInfo;

}
