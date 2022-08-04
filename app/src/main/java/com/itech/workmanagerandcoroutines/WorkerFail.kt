package com.itech.workmanagerandcoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerFail(context: Context, params:WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        return Result.failure()
    }
}