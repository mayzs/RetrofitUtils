package com.mayz.kotlinlibrary.delagate

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
interface StringCallback:Callback {
    fun onSuccess(result: String?)
}