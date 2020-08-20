# Shake

摇一摇上传屏幕截图

# 使用

注册服务

```java
@Override
protected void onResume() {
    super.onResume();
    ShakeSensorManager.getInstance().onActivityResumed(this);
}

@Override
protected void onPause() {
    super.onPause();
    ShakeSensorManager.getInstance().onActivityPaused();
}
```