package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import com.gavincode.bujo.presentation.ui.BaseViewModel
import io.reactivex.Maybe
import io.reactivex.MaybeOnSubscribe
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

    private val bulletStateLiveData: MutableLiveData<BulletModel>
            = MutableLiveData()

    fun getDailyBullet(): LiveData<DailyBullet> {
        return dailyBulletLiveData
    }

    fun getDailyBulletState(): LiveData<BulletModel> = bulletStateLiveData

    fun fetchDailyBullet(id: String) {
        dailyBulletRepository.getDailyBullet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { dailyBulletLiveData.postValue(it) },
                        onComplete = { newBulletInterval(null) }
                )
                .addTo(disposables)
    }

    private fun newBulletInterval(date: Long?) {
        (if (date == null) LocalDate.now() else LocalDate.ofEpochDay(date))
                .apply {
                    dailyBulletLiveData.postValue(
                            DailyBullet(UUID.randomUUID().toString(),
                                    "",
                                    "",
                                    false,
                                    this,
                                    0,
                                    false,
                                    false,
                                    arrayListOf())
                    )
                }

    }

    fun exit() {
        Maybe.create(MaybeOnSubscribe<DailyBullet>{
            dailyBulletLiveData.value?.apply {
                when (content.isNotEmpty() || title.isNotEmpty()) {
                    true -> it.onSuccess(this)
                    else -> it.onComplete()
                }
            }
        }).flatMapCompletable({
            dailyBulletRepository.updateDailyBullet(it)
        }).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = { bulletStateLiveData.postValue(BulletModel.Saved()) }
                )
                .addTo(disposables)
    }

    fun delete() {
        dailyBulletLiveData.value?.apply {
            dailyBulletRepository.deleteDailyBullet(this)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(onComplete = { bulletStateLiveData.postValue(BulletModel.Deleted())},
                            onError = { it.printStackTrace() })
                    .addTo(disposables)
        }
    }

    fun newDailyBullet(long: Long?) {
        newBulletInterval(long)
    }

    fun changeCheck(content: String) {
        val dailyBullet = dailyBulletLiveData.value
        dailyBullet?.isList= dailyBullet?.isList?.not() ?: false
        dailyBullet?.content = content
        dailyBulletLiveData.postValue(dailyBullet)
    }
}