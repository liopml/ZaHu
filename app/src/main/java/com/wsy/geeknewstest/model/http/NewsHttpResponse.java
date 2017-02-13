package com.wsy.geeknewstest.model.http;

/**
 * Created by hasee on 2017/1/28.
 */

public class NewsHttpResponse<T> {

    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

}
