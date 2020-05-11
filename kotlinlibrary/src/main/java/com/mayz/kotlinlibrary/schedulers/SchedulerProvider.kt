package com.mayz.kotlinlibrary.schedulers


import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @createDate: 2020/5/8
 * @author:     mayz
 * @version:    1.0
 */
object SchedulerProvider :BaseSchedulerProvider{
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()

    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun <T> applySchedulers(): ObservableTransformer<T?, T?>? {
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(io())
                .observeOn(ui())
        }
    }
}