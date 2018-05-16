package com.gavincode.bujo.presentation.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by gavinlin on 25/3/18.
 */


class SingleLiveEvent<T>: MutableLiveData<T>() {

    val mPending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, Observer {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }
}

