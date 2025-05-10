package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState

class TestSubscriber<T> : Subscriber<DomainState<T>> {

    val updates = mutableListOf<DomainState<T>>()

    override fun onUpdate(domainState: DomainState<T>) {
        updates.add(domainState)
    }
}