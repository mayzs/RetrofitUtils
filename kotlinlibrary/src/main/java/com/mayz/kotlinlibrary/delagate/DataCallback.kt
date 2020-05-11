package com.mayz.kotlinlibrary.delagate

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
interface DataCallback<T>:Callback {
    fun onSuccess(result:T?)
}