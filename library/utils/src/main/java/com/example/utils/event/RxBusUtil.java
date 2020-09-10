package com.example.utils.event;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 事件传递
 */
public class RxBusUtil {
    private static RxBusUtil instance = new RxBusUtil();

    private final Subject bus;
    private final Subject stickyBus;

    public static RxBusUtil getInstance() {
        return instance;
    }

    private RxBusUtil() {
        //toSerialized保证线程安全
        bus = PublishSubject.create().toSerialized();
        stickyBus = BehaviorSubject.create().toSerialized();
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public void postSticky(Object o) {
        stickyBus.onNext(o);
    }

    public <T> Disposable subscribe(Class<T> eventType, Consumer<T> action, Consumer<Throwable> throwableAction) {
        if (null == action) {
            action = new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    //do nothing
                }
            };
        }
        if (null == throwableAction) {
            throwableAction = new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    //do nothing
                }
            };
        }
        return bus.ofType(eventType).subscribe(action, throwableAction);
    }

    public <T> Disposable subscribeSticky(Class<T> eventType, Consumer<T> action, Consumer<Throwable> throwableAction) {
        if (null == action) {
            action = new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    //do nothing
                }
            };
        }
        if (null == throwableAction) {
            throwableAction = new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    //do nothing
                }
            };
        }
        return stickyBus.ofType(eventType).subscribe(action, throwableAction);
    }

}
