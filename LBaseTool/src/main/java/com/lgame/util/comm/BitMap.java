package com.lgame.util.comm;

/**
        * 实现BitMap
        *
        * @author xialonglei
        * @since 2016/5/26
        */
public class BitMap {
    /** 插入数的最大长度，比如100，那么允许插入bitsMap中的最大数为99 */
    private long length;
    private int[] bitsMap;
    private static final int[] BIT_VALUE = { 0x00000001, 0x00000002, 0x00000004, 0x00000008, 0x00000010, 0x00000020,
            0x00000040, 0x00000080, 0x00000100, 0x00000200, 0x00000400, 0x00000800, 0x00001000, 0x00002000, 0x00004000,
            0x00008000, 0x00010000, 0x00020000, 0x00040000, 0x00080000, 0x00100000, 0x00200000, 0x00400000, 0x00800000,
            0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000, 0x40000000, 0x80000000 };
    public BitMap(long length) {
        this.length = length;
        // 根据长度算出，所需数组大小
        bitsMap = new int[(int) (length >> 5) + ((length & 31) > 0 ? 1 : 0)];
        System.out.println("length:"+bitsMap.length);
    }

    public BitMap(long length,int[] data) {
        this.length = length;
        // 根据长度算出，所需数组大小
        int newArrayLength = (int) (length >> 5) + ((length & 31) > 0 ? 1 : 0);
        if(data.length > newArrayLength){
            throw new IllegalArgumentException("length value illegal!");
        }
        bitsMap = data;
        System.out.println("length:"+bitsMap.length);
    }
    /**
     * 根据长度获取数据 比如输入63，那么实际上是确定数62是否在bitsMap中
     *
     * @return index 数的长度
     * @return 1:代表数在其中 0:代表
     */
    public int getBit(long index) {
        if (index < 0 || index > length) {
            throw new IllegalArgumentException("length value illegal!");
        }
        int intData = (int) bitsMap[(int) ((index - 1) >> 5)];
        return ((intData & BIT_VALUE[(int) ((index - 1) & 31)])) >>> ((index - 1) & 31);
    }
    /**
     * @param index
     *            要被设置的值为index - 1
     */
    public void setBit(long index) {
        if (index < 0 || index > length) {
            throw new IllegalArgumentException("length value illegal!");
        }
        // 求出该index - 1所在bitMap的下标
        int belowIndex = (int) ((index - 1) >> 5);
        // 求出该值的偏移量(求余)
        int offset = (int) ((index - 1) & 31);
        int inData = bitsMap[belowIndex];
        bitsMap[belowIndex] = inData | BIT_VALUE[offset];
    }

    public void removeBit(long index) {
        if(index<0||index>length) {
            throw new IllegalArgumentException("length value illegal!");
        }

        // 求出该index - 1所在bitMap的下标
        int belowIndex = (int) ((index - 1) >> 5);
        // 求出该值的偏移量(求余)
        int offset = (int) ((index - 1) & 31);
        int inData = bitsMap[belowIndex];
        bitsMap[belowIndex] = inData & ~BIT_VALUE[offset];
    }
    private int[] getBitsMap(){
        return bitsMap;
    }
    public static void main(String[] args) {
        BitMap bitMap = new BitMap(10);
        bitMap.setBit(7);
        bitMap.setBit(8);
        bitMap.setBit(9);
        for (int i = 1;i<10;i++){
            System.out.print(i+" ");
        }
        System.out.println("");
        for (int i = 1;i<10;i++){
            System.out.print(bitMap.getBit(i)+" ");
        }

        BitMap bitMap2 = new BitMap(1000,bitMap.getBitsMap());
        bitMap2.removeBit(9);
        System.out.println("bat2");
        for (int i = 1;i<10;i++){
            System.out.print(bitMap2.getBit(i)+" ");
        }

        System.out.println("bat1");
        for (int i = 1;i<10;i++){
            System.out.print(bitMap.getBit(i)+" ");
        }
        /*for(int i = 0;i<bitMap.getBitsMap().length;i++){
            System.out.println("==>"+bitMap.getBitsMap()[i]);
        }*/
    }
}