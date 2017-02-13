package com.wsy.geeknewstest.model.http;

/**
 * Created by hasee on 2016/12/11.
 */

public class GoldHttpResponse<T> {

    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
