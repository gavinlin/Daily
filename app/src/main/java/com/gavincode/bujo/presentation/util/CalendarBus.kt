package com.gavincode.bujo.presentation.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by gavinlin on 15/3/18.
 */

object CalendarBus {
    private val bus = PublishSubject.create<Any>()

    fun send(any: Any) {
        bus.onNext(any)
    }

    fun toObservable(): Observable<Any> = bus
}