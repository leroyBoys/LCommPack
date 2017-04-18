/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * socket地址转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class InetSocketAddressTransformer implements PropertyTransformer<InetSocketAddress> {

    public static final InetSocketAddressTransformer SHARED_INSTANCE = new InetSocketAddressTransformer();

    public InetSocketAddress transform(String value, Field field){
        String[] parts = value.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("Can't transform property, must be in format \"address:port\"");
        }
        try {
            if ("*".equals(parts[0])) {
                return new InetSocketAddress(Integer.parseInt(parts[1]));
            }
            InetAddress address = InetAddress.getByName(parts[0]);
            int port = Integer.parseInt(parts[1]);
            return new InetSocketAddress(address, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
