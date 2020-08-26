# partition

分区组件

# 目的

实现界面动态化

# 计划

1. 实现分区吸顶/吸底功能
1. 实现瀑布流功能

# 功能

1. 支持分区的复用和重置
1. 支持单/多分区的增删改查

# 数据格式

```json
[
  {
    {},
    'type': 1
  },
  {
    [],
    'type': 2
  },
  ... //此处省略无数条
]
```

# 使用

1. 设置layoutManager，如：setLayoutManager(new GridLayoutManager(getApplicationContext(), ISpanSize.ONE));
1. 创建Adapter需继承BasePartitionAdapter，接着参考PartitionFactory实现分区工厂的创建
1. 创建分区需继承BasePartition<T>，如果允许分区复用，需要在getItemViewTypeBean方法中设置ItemViewTypeBean.repeat = true;。如果需要添加viewHolder，请参考OnePartition

# 类似项目

1. [Tangram-Android](https://github.com/alibaba/Tangram-Android)