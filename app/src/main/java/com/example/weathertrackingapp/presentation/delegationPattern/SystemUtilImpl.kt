package com.example.weathertrackingapp.presentation.delegationPattern

import java.util.Locale

class SystemUtilImpl : SystemUtil {
    override fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }
}