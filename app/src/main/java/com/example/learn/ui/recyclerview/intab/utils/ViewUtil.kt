package com.example.learn.ui.recyclerview.intab.utils

class ViewUtil {

    companion object {

        /**
         * 获取滑动方向
         */
        fun getOrientation(dx: Float, dy: Float): Char {
            if (dx == 0f && dy == 0f) { // 点击事件
                return 'c'
            }
            return if (Math.abs(dx) > Math.abs(dy)) {
                //X轴移动
                if (dx > 0) 'r' else 'l' //右,左
            } else {
                //Y轴移动
                if (dy > 0) 'b' else 't' //下,上
            }
        }

    }

}
