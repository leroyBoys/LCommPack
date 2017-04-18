package com.lgame.util.comm;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 2017/3/30.
 */
public class RandomTool {

    public static Random getRandom(){
        return ThreadLocalRandom.current();
    }

    public static int Next() {
        return getRandom().nextInt();
    }

    public static int Next(int max) {
        return getRandom().nextInt(max);
    }

    public static int Next(int min, int max) {
        return (getRandom().nextInt(max - min) + min);
    }
}
