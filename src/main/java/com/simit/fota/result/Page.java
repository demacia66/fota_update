package com.simit.fota.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    //总条数
    private int totalCount;
    @JsonIgnore
    private int totalPage;

    private String orderType = "asc";

    private String orderField = "ID";

    //当前页数
    private Integer currentPage = 1;

    //每页条数
    private Integer pageSize = 10;

    //起始行
    @JsonIgnore
    private int startRow;

    private List<T> dataList;

    public Page(int totalCount){
        this.pageSize = 10;
        this.currentPage = 1;
        this.totalCount = totalCount;
        this.totalPage = totalCount / this.pageSize;
        this.startRow = 0;
    }

    public Page(List<T> data,int totalCount){
        this.dataList = data;
        this.totalCount = totalCount;
        this.startRow = (this.currentPage - 1) * this.pageSize;
    }

    public Page(List<T> data,Page page){
        this.dataList = data;
        this.currentPage = page.getCurrentPage();
        this.totalCount = page.getTotalCount();
        this.pageSize = page.getPageSize();
        this.startRow = (this.currentPage - 1) * this.pageSize;
    }

    public Page(int totalCount, String orderType, String orderField, Integer currentPage, Integer pageSize) {
        this.orderType = orderType;
        this.orderField = orderField;
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
        this.startRow = ((this.currentPage - 1) * this.pageSize);
    }

    public Page(int totalCount, Integer currentPage, Integer pageSize){
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
        this.startRow = ((this.currentPage - 1) * this.pageSize);
    }

    public static <T> Page success(List<T> data,int totalCount){
        return new Page(data,totalCount);
    }

    public static <T> Page success(List<T> data,Page page){
        return new Page(data,page);
    }
}
