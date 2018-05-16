package com.gavincode.bujo.presentation.ui

import androidx.lifecycle.LiveData


object Navigator {

    const val ARG_DATE_LONG = "date"
    const val ARG_BULLET_ID = "bulletId"
    private const val REQ_BULLET_BASE = 0x0123
    const val REQ_BULLET_EDIT = REQ_BULLET_BASE + 1
    const val REQ_BULLET_ADD = REQ_BULLET_BASE + 2

    private val activityForResultLiveData = SingleLiveEvent<NavigateResultInfo>()
    fun sendActivityResult(navigateResultInfo: NavigateResultInfo) {
        activityForResultLiveData.postValue(navigateResultInfo)
    }

    fun getActivityForResultLiveData(): LiveData<NavigateResultInfo> = activityForResultLiveData
}