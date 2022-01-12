package com.bbkj.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 翻页封装对象.
 * User: 包永雄
 * Date: 11-4-8
 * Time: 下午6:06
 */
public class Page {
    public static int DEAULT_PAGE_SIZE = 10;

    private int pageNo = 1;
    private int totalNum = 0;
    private int pageSize = DEAULT_PAGE_SIZE;
    private List pageList = new ArrayList();
    private long queryTime;

    public Page() {
    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        if (this.pageNo < 1) {
            this.pageNo = 1;
        }
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize, int totalNum) {
        this(pageNo, pageSize);
        this.totalNum = totalNum;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return (totalNum - 1) / pageSize + 1;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List getPageList() {
        if (pageList == null) {
            return new ArrayList();
        }
        return pageList;
    }

    public void setPageList(List pageList) {
        this.pageList = pageList;
    }

    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getPrePageNo() {
        return pageNo > 1 ? pageNo - 1 : pageNo;
    }

    public int getNextPageNo() {
        return pageNo >= getTotalPage() ? getTotalPage() : pageNo + 1;
    }

    public int getResultsSize() {
        return pageList != null ? getStartIndex() + pageList.size() : 0;
    }
}
