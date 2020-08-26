package com.example.partition;

/**
 * 实现分区配置，将一行分为2520（最小公倍数）份，一般一行最多占10列，所以最多就分10份
 */
public interface ISpanSize {

    /**
     * 一分之一
     */
    int ONE = 2520;

    /**
     * 二分之一
     */
    int ONE_TWO = 1260;

    /**
     * 三分之一
     */
    int ONE_THIRD = 840;

    /**
     * 四分之一
     */
    int ONE_FOUR = 630;

    /**
     * 五分之一
     */
    int ONE_FIVE = 504;

    /**
     * 六分之一
     */
    int ONE_SIX = 420;

    /**
     * 七分之一
     */
    int ONE_SEVEN = 360;

    /**
     * 八分之一
     */
    int ONE_EIGHT = 315;

    /**
     * 九分之一
     */
    int ONE_NINE = 280;

    /**
     * 十分之一
     */
    int ONE_TEN = 252;

}
