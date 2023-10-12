package com.example.opsc7312_wingedwitness


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var loginUnderline: View
    private lateinit var signUpUnderline: View
    private lateinit var loginViews: List<View>
    private lateinit var signUpViews: List<View>
    private lateinit var buttonLogin: Button
    private lateinit var signUpTextView: TextView
    private var isSignUpMode = false
    private lateinit var errorLabel:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        errorLabel = findViewById(R.id.txtErrorLabel)
        errorLabel.visibility = View.INVISIBLE
        loginUnderline = findViewById(R.id.loginUnderline)
        signUpUnderline = findViewById(R.id.signUpUnderline)
        buttonLogin = findViewById(R.id.buttonLogin)
        signUpTextView = findViewById(R.id.signUpTextView)
        var email = findViewById<EditText>(R.id.SignUpEmailtxt)
        var password = findViewById<EditText>(R.id.SignUpPasswordtxt)
        var confPassword = findViewById<EditText>(R.id.SignUpconfirmtxt)
        // Initialize login and sign-up views
        loginViews = listOf(
            findViewById(R.id.LoginEmaillbl),
            findViewById(R.id.LoginPasswordlbl),
            findViewById(R.id.LoginEmailtxt),
            findViewById(R.id.LoginPasswordtxt)
        )

        signUpViews = listOf(
            findViewById(R.id.SignUpEmaillbl),
            findViewById(R.id.SignUpPasswordlbl),
            findViewById(R.id.SignUpEmailtxt),
            findViewById(R.id.SignUpPasswordtxt),
            findViewById(R.id.SignUpConfirmlbl),
            findViewById(R.id.SignUpconfirmtxt)
        )

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
        buttonLogin.setOnClickListener {
            if(buttonLogin.text=="LogIn"){
                val email = findViewById<EditText>(R.id.LoginEmailtxt).text.toString()
                val password = findViewById<EditText>(R.id.LoginPasswordtxt).text.toString()
                val user = GlobalDataClass.UserDataList.find { it.userEmail == email
                        && it.userPassword == password }
                if (user != null) {
                    val intent = Intent(this, HomePageActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                } else {
                    // No user with the provided email and password was found

                }


            }else{

                if (dataValidation.validateSingUpInput(email.text.toString(),password.text.toString()
                        ,confPassword.text.toString())) {
                    // Input is valid, create UserData object
                    errorLabel.visibility = View.INVISIBLE
                     val userData = UserData()
                    val userId = getLowestInt()
                    if(userId == null){
                        userData.userId = 0
                        userData.userEmail = email.text.toString()
                        userData.userPassword = password.text.toString()
                        userData.metricOrImperial = 'M'
                        GlobalDataClass.UserDataList.add(userData)
                    }else{
                        userData.userId = userId.userId
                        userData.userEmail = email.text.toString()
                        userData.userPassword = password.text.toString()
                        userData.metricOrImperial = 'M'
                        GlobalDataClass.UserDataList.add(userData)
                    }
                    } else {
                    // Handle invalid input (e.g., display an error message)
                    errorLabel.visibility = View.VISIBLE
                }
            }


        }

    }

    fun getLowestInt() : UserData? {
        val userId = GlobalDataClass.UserDataList.minByOrNull { it.userId }
        return userId
    }

    fun onLoginClick(view: View) {
        loginUnderline.visibility = View.VISIBLE
        signUpUnderline.visibility = View.GONE
        isSignUpMode = false
        showViews(loginViews)
        hideViews(signUpViews)
        buttonLogin.text = "LogIn"
    }

    fun onSignUpClick(view: View) {
        signUpUnderline.visibility = View.VISIBLE
        loginUnderline.visibility = View.GONE
        isSignUpMode = true
        showViews(signUpViews)
        hideViews(loginViews)
        buttonLogin.text = "Sign-Up"
    }

    private fun showViews(views: List<View>) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    private fun hideViews(views: List<View>) {
        for (view in views) {
            view.visibility = View.GONE
        }
    }
}
