package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
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