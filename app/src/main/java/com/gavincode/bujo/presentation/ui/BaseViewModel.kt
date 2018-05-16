package com.gavincode.bujo.presentation.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel: ViewModel() {
    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}