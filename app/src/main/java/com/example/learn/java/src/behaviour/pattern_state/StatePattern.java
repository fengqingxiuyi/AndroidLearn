package com.example.learn.java.src.behaviour.pattern_state;

import com.example.learn.java.src.behaviour.pattern_state.state.Context;
import com.example.learn.java.src.behaviour.pattern_state.state.StartState;
import com.example.learn.java.src.behaviour.pattern_state.state.StopState;

/**
 * @author ShenBF
 * @desc 使用 Context 来查看当状态 State 改变时的行为变化
 * @date 2018/7/31
 */
public class StatePattern {

    public static void main(String[] args) {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        System.out.println(context.getState().toString());

        StopState stopState = new StopState();
        stopState.doAction(context);

        System.out.println(context.getState().toString());
    }

}
