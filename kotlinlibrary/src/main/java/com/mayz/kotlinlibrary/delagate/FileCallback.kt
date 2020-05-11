package com.mayz.kotlinlibrary.delagate

import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * @createDate: 2020/5/7
 * @author:     mayz
 * @version:    1.0
 */
abstract class FileCallback(var filePath:String?,var fileName:String) :Callback{
    fun saveFile(range:Long,responseBody: ResponseBody){
        var randomAccessFile: RandomAccessFile? = null
        var inputStream: InputStream? = null
        var total = range
        var responseLength: Long = 0
        try {
            val buf = ByteArray(2048)
            var len = 0
            responseLength = responseBody.contentLength()
            inputStream = responseBody.byteStream()
            val file = File(filePath, fileName)
            val dir = File(filePath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            onStart()
            randomAccessFile = RandomAccessFile(file, "rwd")
            if (range == 0L) {
                randomAccessFile.setLength(responseLength)
            }
            randomAccessFile.seek(range)
            var progress = 0
            var lastProgress = 0
            while (inputStream.read(buf).also { len = it } != -1) {
                randomAccessFile.write(buf, 0, len)
                total += len.toLong()
                lastProgress = progress
                progress = (total * 100 / randomAccessFile.length()).toInt()
                if (progress > 0 && progress != lastProgress) {
                    onProgress(progress)
                }
            }
            onCompleted(file)
        } catch (e: Exception) {
            onError(e.message)
            e.printStackTrace()
        } finally {
            try {
                //SPDownloadUtil.getInstance().save(url, total);
                randomAccessFile?.close()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    abstract fun onStart()

    abstract fun onProgress(progress: Int)

    abstract fun onCompleted(file: File?)
}