package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.customState.ResultState

interface UseCaseObserver<T> {
    fun onUpdate(resultState:ResultState<T>)
}