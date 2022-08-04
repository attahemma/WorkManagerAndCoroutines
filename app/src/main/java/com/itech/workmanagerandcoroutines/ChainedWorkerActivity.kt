package com.itech.workmanagerandcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkContinuation
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.itech.workmanagerandcoroutines.databinding.ActivityChainedWorkerBinding

class ChainedWorkerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChainedWorkerBinding
    val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChainedWorkerBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        addEventListenerToButtons()
    }

    private fun addEventListenerToButtons() {
        binding.btnSingleSucceed.setOnClickListener{
            val objectRequest = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val networkRequest = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val dbRequest = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            workManager.beginWith(objectRequest)
                .then(networkRequest)
                .then(dbRequest)
                .enqueue()
        }
        binding.btnSingleFail.setOnClickListener{
            val objectRequest = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val networkRequest = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to false))
                .build()
            val dbRequest = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            workManager.beginWith(objectRequest)
                .then(networkRequest)
                .then(dbRequest)
                .enqueue()
        }
        binding.btnGroupSucceed.setOnClickListener{
            val objectRequest1 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val objectRequest2 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val networkRequest = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val dbRequest = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()

            workManager.beginWith(listOf(objectRequest1, objectRequest2))
                .then(networkRequest)
                .then(dbRequest)
                .enqueue()
        }
        binding.btnGroupFail.setOnClickListener{
            val objectRequest1 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val objectRequest2 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to false))
                .build()
            val networkRequest = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()
            val dbRequest = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true))
                .build()

            workManager.beginWith(listOf(objectRequest1, objectRequest2))
                .then(networkRequest)
                .then(dbRequest)
                .enqueue()
        }
        binding.btnMultipleSucceed.setOnClickListener{
            val objectRequest1 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val objectRequest2 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "TWO"))
                .build()
            val networkRequest1 = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val networkRequest2 = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "TWO"))
                .build()
            val dbRequest1 = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val dbRequest2 = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "TWO"))
                .build()
            val recommendation1 = workManager.beginWith(objectRequest1)
                .then(networkRequest1)
                .then(dbRequest1)
            val recommendation2 = workManager.beginWith(objectRequest2)
                .then(networkRequest2)
                .then(dbRequest2)
            val root = WorkContinuation.combine(listOf(recommendation1, recommendation2))
            root.enqueue()
        }
        binding.btnMultipleFail.setOnClickListener{
            val objectRequest1 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val objectRequest2 = OneTimeWorkRequestBuilder<ObjectDetectedWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "TWO"))
                .build()
            val networkRequest1 = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val networkRequest2 = OneTimeWorkRequestBuilder<NetworkRequestWorker>()
                .setInputData(workDataOf(SUCCESS to false, "NAME" to "TWO"))
                .build()
            val dbRequest1 = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "ONE"))
                .build()
            val dbRequest2 = OneTimeWorkRequestBuilder<DatabaseWriteWorker>()
                .setInputData(workDataOf(SUCCESS to true, "NAME" to "TWO"))
                .build()

            val recommendation1 = workManager.beginWith(objectRequest1)
                .then(networkRequest1)
                .then(dbRequest1)
            val recommendation2 = workManager.beginWith(objectRequest2)
                .then(networkRequest2)
                .then(dbRequest2)
            val root = WorkContinuation.combine(listOf(recommendation1, recommendation2))
            root.enqueue()
        }
    }
}