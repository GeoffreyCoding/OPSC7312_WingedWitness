package com.example.opsc7312_wingedwitness

import android.app.Application

class GlobalDataClass: Application() {
    companion object{
        var UserDataList = arrayListOf<UserData>()
        var SightingDataList = arrayListOf<SightingData>()
        var QuizDataList = arrayListOf<QuizData>()
    }
}