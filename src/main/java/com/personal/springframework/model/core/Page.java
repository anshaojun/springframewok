package com.personal.springframework.model.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: springframework
 * @description: 分页类
 * @author: 安少军
 * @create: 2021-12-29 09:45
 **/
@Data
public class Page<T> {

    //总页数
    private int totalPage;

    //当前页
    private int page;

    //总条数
    private int totalNum;

    //从第几条开始
    private int startRow;

    //从第几条结束
    private int endRow;

    //每页几条
    private int limit;

    //数据筛选
    private String dataScope_;

    private List<T> list = new ArrayList<T>();

    public void count() {
        int totlaPageTemp = this.totalNum / this.limit;
        int plus = (this.totalNum % this.limit) == 0 ? 0 : 1;
        totlaPageTemp += plus;

        if (totlaPageTemp <= 0) {
            totlaPageTemp = 1;
        }
        this.totalPage = totlaPageTemp;

        if (this.totalPage < this.page) {
            this.page = this.totalPage;
        }

        if (this.page < 1) {
            this.page = 1;
        }

        this.startRow = (this.page - 1) * this.limit;
        this.endRow = startRow + limit;
    }

}
