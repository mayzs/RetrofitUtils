package com.mayz.retrofitutils.delagate;

/**
 * @createDate: 2019/1/7
 * @author: mayz
 * @version: 1.0
 */
public interface DataCallback<T> extends Callback {
    void onSuccess(T result);
}
