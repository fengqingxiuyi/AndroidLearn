# download

业务无关UI组件库

## 目录结构说明

```
download
  |--src/main/java/com/example/download //多级目录折叠
       |--bean //数据Bean
       |--db //数据库，实现断点下载功能
       |--service //启动服务和网络请求
       |--util //模块内部使用的工具
```

## 问题TODO

连点两次`下载按钮`，再怎么点击`停止按钮`，也不会停止。