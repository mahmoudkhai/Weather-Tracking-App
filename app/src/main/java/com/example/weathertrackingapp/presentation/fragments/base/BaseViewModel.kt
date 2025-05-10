package com.example.weathertrackingapp.presentation.fragments.base

import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.common.observerPattern.Subscriber

abstract class BaseViewModel<OBSERVER> : Observer<OBSERVER> {

    abstract val subscribers: MutableSet<Subscriber<OBSERVER>>

    override fun registerSubscriber(subscriber: Subscriber<OBSERVER>) {
        subscribers.add(subscriber)
    }

    override fun unregisterSubscriber(subscriber: Subscriber<OBSERVER>) {
        subscribers.remove(subscriber)
    }

    override fun notifyAllSubscribers(newEvent: OBSERVER) {
        subscribers.forEach { it.onUpdate(newEvent) }
    }
}