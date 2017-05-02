/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

/**
 * 设置对象变量后需进行的额外操作<br/>
 * 如对某个或某些变量进行额外计算操作或重新设置操作。
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public abstract interface InitializeBean {

    public abstract void afterPropertiesSet();
}
