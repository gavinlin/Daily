package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

/**
 * Created by gavinlin on 24/3/18.
 */

class BulletViewModel @Inject constructor(
        val dailyBulletRepository: DailyBulletRepository
): ViewModel() {
    private val dailyBulletLiveData: MutableLiveData<DailyBullet>
            = MutableLiveData()

    fun getDailyBullet(): LiveData<DailyBullet> {
        return dailyBulletLiveData
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
    }

    fun save() {
        dailyBulletLiveData.value?.let {
            dailyBulletRepository.updateDailyBullet(it)
        }
    }
}