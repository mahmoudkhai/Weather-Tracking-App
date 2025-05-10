package com.example.weathertrackingapp.common.observerPattern

interface Subscriber<T> {
    fun onUpdate(domainState: T)
}