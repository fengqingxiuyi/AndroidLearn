# viewmodel

LiveData+ViewModel+Repository打造MVVM模式

# FAQ

## 当Fragment实现LifecycleOwner接口时，无法实现`监听Fragment生命周期`的目的，可能是BUG

版本: Lifecycle:1.1.1

解决办法: 使用过时接口`LifecycleRegistryOwner`可以解决，该类内容如下：

```java
package android.arch.lifecycle;

import android.support.annotation.NonNull;

/**
 * @deprecated Use {@code android.support.v7.app.AppCompatActivity}
 * which extends {@link LifecycleOwner}, so there are no use cases for this class.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Deprecated
public interface LifecycleRegistryOwner extends LifecycleOwner {
    @NonNull
    @Override
    LifecycleRegistry getLifecycle();
}
```
