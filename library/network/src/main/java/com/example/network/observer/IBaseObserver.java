package com.example.network.observer;

import io.reactivex.Observer;

public interface IBaseObserver<T> extends Observer<T> {

    void dispose();

}
