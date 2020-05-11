package com.mayz.kotlinlibrary.schedulers

import androidx.annotation.NonNull
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

/**
 * @createDate: 2020/5/8
 * @author:     mayz
 * @version:    1.0
 */
interface BaseSchedulerProvider {
    @NonNull
    fun computation():Scheduler
    @NonNull
    fun io():Scheduler
    @NonNull
    fun ui():Scheduler
    @NonNull
    fun <T> applySchedulers(): ObservableTransformer<T?, T?>?
}