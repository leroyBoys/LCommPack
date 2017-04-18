package com;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/5.
 */
public interface ProbufferInterface<T> {
    public T getObject(byte[] bytes);
}
