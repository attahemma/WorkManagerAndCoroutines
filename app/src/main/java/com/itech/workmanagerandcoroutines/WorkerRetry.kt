package com.itech.workmanagerandcoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerRetry(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork() : Result{
        println("Still Working: ${WorkStatusSingleton.workRetryCount}")
        return if (WorkStatusSingleton.workRetryCount < 3){
            WorkStatusSingleton.workRetryCount++
            Result.retry()
        }else {
            Result.success()
        }
    }
}