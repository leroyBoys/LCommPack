/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author leroy_boy
 */
public class NameThreadFactory implements ThreadFactory {

    final ThreadGroup group;
    final AtomicInteger threadNumber;
    final String namePrefix;

    public NameThreadFactory(ThreadGroup group, String name,int threadnumber) {
        this.group = group;
        this.namePrefix = (group.getName() + ":" + name);
        this.threadNumber = new AtomicInteger(threadnumber);
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
        return t;
    }
}
