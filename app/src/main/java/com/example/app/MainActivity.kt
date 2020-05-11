package com.example.app


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mayz.retrofitutils.RetrofitUtils
import com.mayz.retrofitutils.delagate.StringCallback


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RetrofitUtils.getInstance().get().url("https://www.wanandroid.com/banner/json").execute(object : StringCallback {
            override fun onSuccess(result: String?) {
                Log.i("tag","===123==$result")
            }

            override fun onError(errmsg: String?) {

            }

        })
    }
}
