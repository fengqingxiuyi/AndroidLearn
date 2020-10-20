package com.example.utils.event

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * 事件传递
 */
class RxBusUtil private constructor() {

    companion object {
        @JvmStatic
        val instance = RxBusUtil()
    }

    //toSerialized保证线程安全
    private val bus: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    private val stickyBus: Subject<Any> = BehaviorSubject.create<Any>().toSerialized()

    fun post(o: Any) {
        bus.onNext(o)
    }

    fun postSticky(o: Any) {
        stickyBus.onNext(o)
    }

    fun <T> subscribe(
        eventType: Class<T>?,
        action: Consumer<T>?,
        throwableAction: Consumer<Throwable?>?
    ): Disposable {
        return bus.ofType(eventType).subscribe(action ?: Consumer {
            //do nothing
        }, throwableAction ?: Consumer {
            //do nothing
        })
    }

    fun <T> subscribeSticky(
        eventType: Class<T>?,
        action: Consumer<T>?,
        throwableAction: Consumer<Throwable?>?
    ): Disposable {
        return stickyBus.ofType(eventType).subscribe(action ?: Consumer {
            //do nothing
        }, throwableAction ?: Consumer {
            //do nothing
        })
    }

}