package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import com.gavincode.bujo.presentation.ui.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
): ViewModel() {
    private val dailyBulletLiveData: MutableLiveData<DailyBullet>
            = MutableLiveData()

    private val saveLiveData: SingleLiveEvent<Boolean>
            = SingleLiveEvent()

    private val disposables = CompositeDisposable()

    fun getDailyBullet(): LiveData<DailyBullet> {
        return dailyBulletLiveData
    }

    fun getSaved(): LiveData<Boolean> {
        return saveLiveData
    }

    fun fetchDailyBullet(id: String) {
        dailyBulletRepository.getDailyBullet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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


    fun exit() {
        dailyBulletLiveData.value?.let {
            if (it.content.isNotEmpty() || it.title.isNotEmpty()) {
                dailyBulletRepository.updateDailyBullet(it)
                saveLiveData.setValue(true)
            } else {
                saveLiveData.setValue(false)
            }
        } ?: saveLiveData.setValue(false)
    }

    fun delete() {
        dailyBulletLiveData.value?.let {
            dailyBulletRepository.deleteDailyBullet(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}