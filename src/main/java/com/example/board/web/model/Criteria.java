package com.example.board.web.model;

import lombok.Data;

@Data
public abstract class Criteria {
    private int pageSize = 10;
    private int startRow = 0;

}
