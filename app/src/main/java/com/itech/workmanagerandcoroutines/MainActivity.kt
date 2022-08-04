package com.itech.workmanagerandcoroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import com.itech.workmanagerandcoroutines.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    val workManager = WorkManager.getInstance(this)
    internal var outputWorkInfos: LiveData<List<WorkInfo>>? = null

    init {
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(SIMPLE_WORKER_TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

        addEventListenerToButtons()
        outputWorkInfos?.observe(this, workInfosObserver())
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer {
            listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()){
                return@Observer
            }

            val workInfo =  listOfWorkInfo[0]

            if (workInfo.state.isFinished) hideProgress()
            else showProgress()

        }
    }

    private fun addEventListenerToButtons() {
        activityMainBinding.btnStart.setOnClickListener{
            val data  = workDataOf(WORK_MESSAGE to "Work Completed!")
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            //Data.Builder().putString(WORK_MESSAGE,"Work Completed!").build()
            val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
                .setInputData(data)
                .addTag(SIMPLE_WORKER_TAG)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<SimpleWorker>(
                5,TimeUnit.SECONDS,
            2,TimeUnit.SECONDS
            ).build()
            workManager.enqueue(workRequest)

        }

        activityMainBinding.btnWorkStatus.setOnClickListener{
            val toast = Toast.makeText(this, "The work status is: ${WorkStatusSingleton.workMessage}",Toast.LENGTH_SHORT)
            toast.show()
        }

        activityMainBinding.btnResetStatus.setOnClickListener{
            WorkStatusSingleton.workComplete = false
        }

        activityMainBinding.btnWorkOnUithread.setOnClickListener{
            Thread.sleep(10000)
            WorkStatusSingleton.workComplete = true
        }

        activityMainBinding.btnWorkFail.setOnClickListener{
            val workRequest = OneTimeWorkRequestBuilder<WorkerFail>().build()
            workManager.enqueue(workRequest)
        }

        activityMainBinding.btnWorkRetry.setOnClickListener{
            val workRequest = OneTimeWorkRequestBuilder<WorkerRetry>()
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10,
                    TimeUnit.SECONDS
                ).build()
            workManager.enqueue(workRequest)
        }

        activityMainBinding.btnToChainedActivity.setOnClickListener{
            startActivity(Intent(this, ChainedWorkerActivity::class.java))
        }
    }

    private fun showProgress() {
        activityMainBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        activityMainBinding.progressBar.visibility = View.GONE
    }
}