package com.itech.workmanagerandcoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        val message = inputData.getString(WORK_MESSAGE)
        Thread.sleep(10000)
        WorkStatusSingleton.workComplete = true
        message.let {
            WorkStatusSingleton.workMessage = it!!
        }
        return Result.success()
    }
}