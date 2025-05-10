package com.example.weathertrackingapp.common.observerPattern

interface Observer<T> {
    fun registerSubscriber(subscriber: Subscriber<T>)
    fun unregisterSubscriber(subscriber: Subscriber<T>)
    fun notifyAllSubscribers(newState: T)
}