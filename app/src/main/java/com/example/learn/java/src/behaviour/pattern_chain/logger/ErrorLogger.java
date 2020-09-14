package com.example.learn.java.src.behaviour.pattern_chain.logger;

/**
 * @author ShenBF
 * @desc 创建扩展了该记录器类的实体类
 * @date 2018/7/31
 */
public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Error Console::Logger: " + message);
    }
    
}
