package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.common.observerPattern.Subscriber

abstract class BaseUseCase<OBSERVABLE> : Observer<OBSERVABLE> {

    abstract val subscribers: MutableSet<Subscriber<OBSERVABLE>>

    override fun registerSubscriber(subscriber: Subscriber<OBSERVABLE>) {
        subscribers.add(subscriber)
    }

    override fun unregisterSubscriber(subscriber: Subscriber<OBSERVABLE>) {
        subscribers.remove(subscriber)
    }

    override fun notifyAllSubscribers(newEvent: OBSERVABLE) {
        subscribers.forEach { it.onUpdate(newEvent) }
    }

}