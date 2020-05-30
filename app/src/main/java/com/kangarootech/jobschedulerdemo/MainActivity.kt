package com.kangarootech.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobInfo.NETWORK_TYPE_ANY
import android.app.job.JobInfo.NETWORK_TYPE_NONE
import android.app.job.JobInfo.NETWORK_TYPE_UNMETERED
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.mainCancelButton
import kotlinx.android.synthetic.main.activity_main.mainNetworkRadioButtonGroup
import kotlinx.android.synthetic.main.activity_main.mainRequiredChargeSwitch
import kotlinx.android.synthetic.main.activity_main.mainRequiredIdleSwitch
import kotlinx.android.synthetic.main.activity_main.mainScheduleButton

class MainActivity : AppCompatActivity() {

    private var jobScheduler: JobScheduler? = null
    private val isConstraintSet: Boolean
        get() = getSelectedNetworkOption() != NETWORK_TYPE_NONE ||
                mainRequiredIdleSwitch.isChecked ||
                mainRequiredChargeSwitch.isChecked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        mainScheduleButton.setOnClickListener { onScheduleClick() }
        mainCancelButton.setOnClickListener { cancelJob() }
    }

    private fun onScheduleClick() {
        if (isConstraintSet) {
            scheduleJob()
        }
    }

    private fun scheduleJob() {
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceName = ComponentName(packageName, ToastJobService::class.java.name)
        val jobInfo = JobInfo.Builder(JOB_ID, serviceName).apply {
            setRequiredNetworkType(getSelectedNetworkOption())
            setRequiresCharging(mainRequiredChargeSwitch.isChecked)
            setRequiresDeviceIdle(mainRequiredIdleSwitch.isChecked)
            setOverrideDeadline(OVERRIDE_DEADLINE_TIME)
        }.build()
        jobScheduler?.schedule(jobInfo)
        showToast(R.string.job_is_set)
    }

    private fun cancelJob() {
        jobScheduler?.run {
            cancelAll()
            jobScheduler = null
        }
        showToast(R.string.job_is_cancelled)
    }

    private fun getSelectedNetworkOption(): Int {
        return when (mainNetworkRadioButtonGroup.checkedRadioButtonId) {
            R.id.mainNetworkTypeNoneButton -> NETWORK_TYPE_NONE
            R.id.mainNetworkTypeAnyButton -> NETWORK_TYPE_ANY
            else -> NETWORK_TYPE_UNMETERED
        }
    }

    companion object {
        private const val JOB_ID = 1
        private const val OVERRIDE_DEADLINE_TIME = 10000L
    }
}
