package com.beepiz.blescancoroutines.sample.common.extensions

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.State.DESTROYED
import android.arch.lifecycle.LifecycleOwner
import kotlinx.coroutines.experimental.Job

fun Lifecycle.createJob(cancelState: Lifecycle.State = DESTROYED): Job = Job().also { job ->
    addObserver(object : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
            if (currentState == cancelState) {
                removeObserver(this)
                job.cancel()
            }
        }
    })
}
