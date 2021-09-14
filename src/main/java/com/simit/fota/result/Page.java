package com.simit.fota.result;

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
    private int totalPage;

    //当前页数
    private Integer currentPage = 1;

    //每页条数
    private Integer pageSize = 10;

    //起始行
    private int startRow;

    private List<T> dataList;

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

    public static <T> Page success(List<T> data,int totalCount){
        return new Page(data,totalCount);
    }

    public static <T> Page success(List<T> data,Page page){
        return new Page(data,page);
    }
}
