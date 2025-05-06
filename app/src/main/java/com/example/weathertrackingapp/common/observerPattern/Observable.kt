package com.example.weathertrackingapp.common.observerPattern

interface Observable<T> {
    fun registerObserver(observer: Observer<T>)
    fun unregisterObserver(observer: Observer<T>)
    fun notifyObservers(newState: T)
}