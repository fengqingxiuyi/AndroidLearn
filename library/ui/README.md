# ui

业务无关UI组件库

## 目录结构说明

```
ui
  |--src/main/java/com/example/ui //多级目录折叠
       |--adapter //CommonAdapter
       |--card //卡片形式的View的左右阻尼滑动效果，每次滑动只滑动一张
       |--circle //圆形、圆角View
       |--container //不好分类的容器类
            |--AutoWrappedViewGroup //自动换行的容器
       |--dialog //基础Dialog组件
       |--drag //拖拽组件
       |--expand //解决嵌套View显示不全的View
       |--loop //单个条目无限上下翻滚的View容器
       |--progress //进度条模块
       |--text //TextView相关的View
            |--MarqueeTextView //跑马灯
       |--toast //全局Toast组件
       |--updown //两个条目无限上下翻滚的View容器
       |--utils //不想和utils模块耦合的工具模块
```