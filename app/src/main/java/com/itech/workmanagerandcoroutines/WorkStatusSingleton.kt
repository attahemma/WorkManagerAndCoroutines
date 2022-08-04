package com.itech.workmanagerandcoroutines

object WorkStatusSingleton {
    var workComplete: Boolean = false
    var workMessage = ""
    var workRetryCount: Int = 0
}