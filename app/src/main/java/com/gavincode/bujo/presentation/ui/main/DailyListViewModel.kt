package com.gavincode.bujo.presentation.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.firebase.ui.auth.viewmodel.SingleLiveEvent
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import com.gavincode.bujo.presentation.ui.BaseViewModel
import com.gavincode.bujo.presentation.util.Message
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by gavinlin on 26/3/18.
 */

class DailyListViewModel @Inject constructor(
        private val dailyBulletRepository: DailyBulletRepository
): BaseViewModel() {
    private val messageLiveData =  SingleLiveEvent<Message>()
    private val uiModelLiveData = MutableLiveData<DailyListUiModel>()

    fun bindMessage(): LiveData<Message> = messageLiveData
    fun bindUiModel(): LiveData<DailyListUiModel> = uiModelLiveData

    init {
        uiModelLiveData.postValue(DailyListUiModel.Idle())
    }

    fun fetchLiveData(date: LocalDate) {
        uiModelLiveData.postValue(DailyListUiModel.Loading())
        dailyBulletRepository.getDailyBullets(date)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            when (it.size) {
                                0    -> uiModelLiveData.postValue(DailyListUiModel.Empty ())
                                else -> uiModelLiveData.postValue(DailyListUiModel.DailyBullets(it))
                            }
                        },
                        onComplete = {
                            uiModelLiveData.postValue(DailyListUiModel.Empty())
                        },
                        onError = {
                            uiModelLiveData.postValue(DailyListUiModel.Idle())
                            handleError(it)
                        }
                        )
                .addTo(disposables)

    }

    private fun handleError(throwable: Throwable) {
        messageLiveData.postValue(Message(Message.Level.ERROR,
                "", throwable.message ?: ""))
    }
}