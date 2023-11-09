package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
//import com.mapbox.android.gestures.Utils
import kotlin.concurrent.thread

//---------------------------------------------------------------------------------------------------------------------//


class MainActivity : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private lateinit var loginUnderline: View
    private lateinit var signUpUnderline: View
    private lateinit var loginViews: List<View>
    private lateinit var signUpViews: List<View>
    private lateinit var buttonLogin: Button
    private lateinit var signUpTextView: TextView
    private var isSignUpMode = false
    private lateinit var errorLabel:TextView
    private lateinit var loginEmail:TextView
    private lateinit var loginPassword:TextView

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initializing firebase firestore
        Firebase.initialize(this)
        //FindViews
        loginEmail = findViewById(R.id.LoginEmailtxt)
        loginPassword = findViewById(R.id.LoginPasswordtxt)
        errorLabel = findViewById(R.id.txtErrorLabel)
        errorLabel.visibility = View.INVISIBLE
        loginUnderline = findViewById(R.id.loginUnderline)
        signUpUnderline = findViewById(R.id.signUpUnderline)
        buttonLogin = findViewById(R.id.buttonLogin)
        signUpTextView = findViewById(R.id.signUpTextView)
        var email = findViewById<EditText>(R.id.SignUpEmailtxt)
        var password = findViewById<EditText>(R.id.SignUpPasswordtxt)
        var confPassword = findViewById<EditText>(R.id.SignUpconfirmtxt)
        //looking for user in firebase auth db
        val DBHandler = DBHandler()

        // Initialize login views
        loginViews = listOf(
            findViewById(R.id.LoginEmaillbl),
            findViewById(R.id.LoginPasswordlbl),
            findViewById(R.id.LoginEmailtxt),
            findViewById(R.id.LoginPasswordtxt)
        )

        // Initialize signup views
        signUpViews = listOf(
            findViewById(R.id.SignUpEmaillbl),
            findViewById(R.id.SignUpPasswordlbl),
            findViewById(R.id.SignUpEmailtxt),
            findViewById(R.id.SignUpPasswordtxt),
            findViewById(R.id.SignUpConfirmlbl),
            findViewById(R.id.SignUpconfirmtxt)
        )

        //-------------------------------------------------------------------------------------------------------------//
        //SignUp
        signUpTextView.setOnClickListener {
            isSignUpMode = !isSignUpMode

            // Toggle visibility of login and sign-up views
            if (isSignUpMode) {
                showViews(signUpViews)
                hideViews(loginViews)
                buttonLogin.text = "Sign-Up"
            } else {
                showViews(loginViews)
                hideViews(signUpViews)
                buttonLogin.text = "LogIn"
            }

            // Toggle underlines based on mode
            loginUnderline.visibility = if (!isSignUpMode) View.VISIBLE else View.GONE
            signUpUnderline.visibility = if (isSignUpMode) View.VISIBLE else View.GONE
        }
        var dataValidation = dataValidation()

        //-------------------------------------------------------------------------------------------------------------//
        //Login
        buttonLogin.setOnClickListener {
            if(buttonLogin.text=="LogIn"){
                val email = findViewById<EditText>(R.id.LoginEmailtxt).text.toString()
                val password = findViewById<EditText>(R.id.LoginPasswordtxt).text.toString()


                DBHandler.signInUser(email, password) { isSuccess, result ->
                    runOnUiThread {
                        if (isSuccess) {
                            //get corresponding ID from firebase
                            val userUID = result.toString()
                            //get data with ID
                            DBHandler.getUserDataByUserUID(userUID) { userData ->
                                runOnUiThread {
                                    if (userData != null) {
                                        var internalUserData = UserData()
                                        internalUserData.userId = userUID
                                        internalUserData.userEmail = userData.userEmail
                                        internalUserData.metricOrImperial = userData.metricOrImperial
                                        internalUserData.lat = userData.lat
                                        internalUserData.lng = userData.lng
                                    } else {
                                        // No user with the provided email and password was found
                                        errorLabel.visibility = View.VISIBLE
                                        loginEmail.text = ""
                                        loginPassword.text = ""
                                    }
                                }
                            }

                            val intent = Intent(this, HomePageActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else {
                            // No user with the provided email and password was found
                            errorLabel.visibility = View.VISIBLE
                            loginEmail.text = ""
                            loginPassword.text = ""
                        }
                    }
                }
            }
            else{
                val dbHandler = DBHandler()
                dbHandler.signUpUser(email.text.toString(), password.text.toString(), "0.0", "0.0", "metric") { success, result ->
                    if (success) {
                        val userUID = result // result contains the new user's UID
                    } else {
                        val errorMessage = result // result contains the error message
                    }
                }
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------//
    //GetLowestInt
    fun getLowestInt() : UserData? {
        val userId = GlobalDataClass.UserDataList.minByOrNull { it.userId }
        return userId
    }

    //-------------------------------------------------------------------------------------------------------------//
    //On Login
    fun onLoginClick(view: View) {
        loginUnderline.visibility = View.VISIBLE
        signUpUnderline.visibility = View.GONE
        isSignUpMode = false
        showViews(loginViews)
        hideViews(signUpViews)
        buttonLogin.text = "LogIn"
    }

    //-------------------------------------------------------------------------------------------------------------//
    //On SignUp
    fun onSignUpClick(view: View) {
        signUpUnderline.visibility = View.VISIBLE
        loginUnderline.visibility = View.GONE
        isSignUpMode = true
        showViews(signUpViews)
        hideViews(loginViews)
        buttonLogin.text = "Sign-Up"
    }

    //-------------------------------------------------------------------------------------------------------------//
    //Show Views
    private fun showViews(views: List<View>) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    //-------------------------------------------------------------------------------------------------------------//
    //Hide Views
    private fun hideViews(views: List<View>) {
        for (view in views) {
            view.visibility = View.GONE
        }
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
