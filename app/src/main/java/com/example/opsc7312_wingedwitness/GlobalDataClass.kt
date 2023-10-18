package com.example.opsc7312_wingedwitness

import android.app.Application

class GlobalDataClass: Application() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Companion object to work with the login and signup
    companion object{
        var UserDataList = arrayListOf<UserData>()
        var SightingDataList = arrayListOf<SightingData>()
        var QuizDataList = arrayListOf<QuizData>()
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//