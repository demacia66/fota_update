package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPage<T> {
    //总条数
    private long totalCount;
    @JsonIgnore
    private long totalPage;

    //当前页数
    private long currentPage = 1;

    //每页条数
    private long pageSize = 10;

    //起始行
    @JsonIgnore
    private int startRow;

    private List<T> dataList;




    public CheckPage(int totalCount){
        this.pageSize = 10;
        this.currentPage = 1;
        this.totalCount = totalCount;
        this.totalPage = totalCount / this.pageSize;
        this.startRow = 0;
    }

    public CheckPage(List<T> data,int totalCount){
        this.dataList = data;
        this.totalCount = totalCount;
        this.startRow = (int) ((int) (this.currentPage - 1) * this.pageSize);
    }

    public CheckPage(int totalCount, Integer currentPage, Integer pageSize){
        if (totalCount == 0){
            this.currentPage = 0;
            this.pageSize = 0;
            this.totalCount = 0;
            this.totalPage = 0;
            return;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (currentPage == null || currentPage.compareTo(0) < 0){
            currentPage = 1;
        }
        this.totalCount = totalCount;
        this.totalPage = totalCount / pageSize;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        if (this.pageSize * (this.currentPage - 1) > this.totalCount){
            this.currentPage = totalPage;
        }
        this.startRow = (int)((this.currentPage - 1) * this.pageSize);
    }


    public int getStartRow(){
        return (int) ((currentPage - 1) * pageSize);
    }

}
