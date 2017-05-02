/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import java.io.InputStream;
import java.util.Iterator;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public abstract interface ResourceReader {

    public abstract <E> Iterator<E> read(InputStream paramInputStream, Class<E> paramClass);
}
