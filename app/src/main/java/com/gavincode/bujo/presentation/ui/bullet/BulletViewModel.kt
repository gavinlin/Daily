package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import com.gavincode.bujo.presentation.ui.BaseViewModel
import com.gavincode.bujo.presentation.ui.SingleLiveEvent
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

/**
 * Created by gavinlin on 24/3/18.
 */

class BulletViewModel @Inject constructor(
        private val dailyBulletRepository: DailyBulletRepository
): BaseViewModel() {
    private val dailyBulletLiveData: MutableLiveData<DailyBullet>
            = MutableLiveData()

    private val saveLiveData: SingleLiveEvent<Boolean>
            = SingleLiveEvent()

    fun getDailyBullet(): LiveData<DailyBullet> {
        return dailyBulletLiveData
    }

    fun getSaved(): LiveData<Boolean> {
        return saveLiveData
    }

    fun fetchDailyBullet(id: String) {
        dailyBulletRepository.getDailyBullet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { dailyBulletLiveData.postValue(it) },
                        onComplete = { dailyBulletLiveData.postValue(
                                DailyBullet(UUID.randomUUID().toString(),
                                        "",
                                        "",
                                        false,
                                        LocalDate.now(),
                                        0,
                                        false,
                                        arrayListOf())
                        )}
                )
                .addTo(disposables)
    }


    @WorkerThread
    fun exit() {
        Flowable.just(true)
                .observeOn(Schedulers.io())
                .map(Function<Boolean, Boolean> {
                    dailyBulletLiveData.value?.let {
                        if (it.content.isNotEmpty() || it.title.isNotEmpty()) {
                            dailyBulletRepository.updateDailyBullet(it)
                            saveLiveData.postValue(true)
                        } else {
                            saveLiveData.postValue(false)
                        }
                    } ?: saveLiveData.postValue(false)
                    return@Function true
                })
                .subscribe()
                .addTo(disposables)
    }

    fun delete() {
        dailyBulletLiveData.value?.let {
            dailyBulletRepository.deleteDailyBullet(it)
        }
    }
}