package com.example.weathertrackingapp.common.observerPattern

interface Observer<T> {
    fun onUpdate(domainState: T)
}