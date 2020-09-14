package com.example.learn.java.src.creation.pattern_prototype.prototype;

/**
 * @author ShenBF
 * @desc 创建一个实现了 Cloneable 接口的抽象类。
 * @date 2018/7/24
 */
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    public abstract void draw();

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

}
