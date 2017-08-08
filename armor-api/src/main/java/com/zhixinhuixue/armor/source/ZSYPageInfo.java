package com.zhixinhuixue.armor.source;


import com.github.pagehelper.Page;

import java.util.List;

public class ZSYPageInfo<T> {

        private int current;// 当前页数
        private int pageSize;// 每页显示的条数
        private long totalSize;// 总条数
        private int totalPage;// 总页数
        private int beginIndex;// 开始索引对应
        private int endIndex;// 结束索引
        private List<T> list;// 分页结果
        private int stepSize=5;


        public ZSYPageInfo(Page<T> list) {
            if (list==null){
                return;
            }
            this.current = list.getPageNum();
            this.pageSize = list.getPageSize();
            this.totalSize = list.getTotal();
            this.totalPage = list.getPages();
            if (this.totalPage <= this.stepSize) {
                this.beginIndex = 1;
                this.endIndex = list.getPages();
            } else {
                int beforeIndex = this.stepSize % 2 == 0 ? (this.stepSize / 2 - 1) : (this.stepSize / 2);
                int afterIndex = this.stepSize / 2;
                beginIndex = this.current - beforeIndex;
                endIndex = this.current + afterIndex;
                if (beginIndex < 1) {
                    beginIndex = 1;
                    endIndex = this.stepSize;
                }
                if (endIndex > this.totalPage) {
                    endIndex = this.totalPage;
                    beginIndex = this.totalPage - this.stepSize + 1;
                }
            }
            this.list = list;
        };

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
