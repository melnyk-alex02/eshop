package com.alex.eshop.wrapper;

import java.util.List;

public class PageWrapper<T> {
    private List<T> content;

    private long totalElements;

    public PageWrapper(List<T> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}