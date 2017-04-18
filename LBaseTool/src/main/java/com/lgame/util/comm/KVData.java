package com.lgame.util.comm;

/**
 * Created by Administrator on 2017/3/21.
 */
public class KVData<K,V> {
    private K k;
    private V v;

    public KVData(){}

    public KVData(K k,V v){
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
}
