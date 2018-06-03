package com.gavincode.bujo.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import com.gavincode.bujo.presentation.ui.BaseViewModel
import com.gavincode.bujo.presentation.ui.SingleLiveEvent
import com.gavincode.bujo.presentation.util.Message
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gavinlin on 26/3/18.
 */

class DailyListViewModel @Inject constructor(
        private val dailyBulletRepository: DailyBulletRepository
): BaseViewModel() {
    private val messageLiveData =  SingleLiveEvent<Message>()
    private val uiModelLiveData = MutableLiveData<DailyListUiModel>()

    private val dateLiveData = MutableLiveData<LocalDate>()

    fun bindMessage(): LiveData<Message> = messageLiveData
    fun bindUiModel(): LiveData<DailyListUiModel> = uiModelLiveData
    fun bindDate(): LiveData<LocalDate> = dateLiveData

    init {
        uiModelLiveData.postValue(DailyListUiModel.Idle())
    }

    fun fetchLiveData() {
        dateLiveData.value?.apply {
            uiModelLiveData.postValue(DailyListUiModel.Loading())
            Timber.i("fetch " + year)
            dailyBulletRepository.getDailyBullets(this)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                Timber.i("fetch daily bullets size ${it.size}")
                                when (it.size) {
                                    0    -> uiModelLiveData.postValue(DailyListUiModel.Empty ())
                                    else -> uiModelLiveData.postValue(DailyListUiModel.DailyBullets(it))
                                }
                            },
                            onComplete = {
                                Timber.e("on Complete")
                                uiModelLiveData.postValue(DailyListUiModel.Empty())
                            },
                            onError = {
                                it.printStackTrace()
                                uiModelLiveData.postValue(DailyListUiModel.Idle())
                                handleError(it)
                            }
                    )
                    .addTo(disposables)
        }
    }

    private fun handleError(throwable: Throwable) {
        messageLiveData.postValue(Message(Message.Level.ERROR,
                "", throwable.message ?: ""))
    }

    fun setDate(localDate: LocalDate) {
        Timber.i("localDate changed " + localDate.year)
        dateLiveData.postValue(localDate)
    }

    fun deleteByPosition(position: Int) {
        val bullet = (uiModelLiveData.value as DailyListUiModel.DailyBullets)
                .dailyBullets[position]
        dailyBulletRepository.deleteDailyBullet(bullet)
                .subscribeOn(Schedulers.io())
                .subscribe { fetchLiveData() }
                .addTo(disposables)
    }
}