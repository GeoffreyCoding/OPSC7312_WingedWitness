package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
class dataValidation {

    //-----------------------------------------------------------------------------------------------------------------//
    //Validate the SignUp input
     fun validateSingUpInput(email: String, password: String,confirmationPassword:String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if(confirmationPassword != password){
            return false
        }

        if (email.isEmpty() || !email.matches(emailPattern.toRegex())) {
            // Invalid email format
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            // Password should be at least 6 characters
            return false
        }

        return true
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Validate the SignUp input
    fun validateSightingInput(
        sCount: String,
        sName: String,
        sSpecies: String,
        sLocation: String
    ): Boolean {
        if (sCount.isEmpty()) {
            return false
        }

        try {
            val count = sCount.toInt()
            if (count <= 0) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }

        if (sName.isEmpty() || sSpecies.isEmpty() || sCount.isEmpty() || sLocation.isEmpty()) {
            return false
        }

        return true
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//