package com;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/28.
 */
public interface Super<T> {
    public List<Integer> list = new LinkedList<>();
    public void set(T t);
}
