package com.mysql.compiler;
public class Idset implements ColumInit {
    @Override
    public void set(Object obj, Object integer) {
        ((com.module.GameServer)obj).setId((int)integer);
    }
}
