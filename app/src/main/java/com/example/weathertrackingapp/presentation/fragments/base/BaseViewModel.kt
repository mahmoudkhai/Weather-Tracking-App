package com.example.weathertrackingapp.presentation.fragments.base

import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer

abstract class BaseViewModel<OBSERVABLE> : Observable<OBSERVABLE> {

    abstract val observers: MutableSet<Observer<OBSERVABLE>>

    override fun registerObserver(observer: Observer<OBSERVABLE>) {
        observers.add(observer)
    }

    override fun unregisterObserver(observer: Observer<OBSERVABLE>) {
        observers.remove(observer)
    }

    override fun notifyObservers(newEvent: OBSERVABLE) {
        observers.forEach { it.onUpdate(newEvent) }
    }
}