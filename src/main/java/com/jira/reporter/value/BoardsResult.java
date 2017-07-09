package com.jira.reporter.value;

import java.util.List;

/**
 * @author Oleg Zaidullin
 */
public class BoardsResult {
    private int     maxResults;
    private int     startAt;
    private int     total;
    private boolean isLast;
    private List<Board>  values;

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean last) {
        isLast = last;
    }

    public List<Board> getValues() {
        return values;
    }

    public void setValues(List<Board> values) {
        this.values = values;
    }
}
