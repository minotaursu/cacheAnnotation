package com.minotaur.cache.support.impl;

/**
 * operation
 * Created by minotaur on 15/11/18.
 */
public enum Operation {

    PUT(3), EVICT(2), CACHE(1);

    Operation(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
