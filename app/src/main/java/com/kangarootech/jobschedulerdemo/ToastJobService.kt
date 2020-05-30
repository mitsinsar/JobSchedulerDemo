package com.kangarootech.jobschedulerdemo

import android.app.job.JobParameters
import android.app.job.JobService

class ToastJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        showToast(R.string.job_is_started)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        showToast(R.string.job_is_stopped)
        return false
    }
}
