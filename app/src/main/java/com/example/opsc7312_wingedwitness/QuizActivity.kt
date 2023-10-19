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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//---------------------------------------------------------------------------------------------------------------------//


class QuizActivity : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private val questionsAndAnswers = listOf(
        arrayOf("All birds have feathers?", true),
        arrayOf("Ostriches can fly?", false),
        arrayOf("The Bald Eagle is only found in the United States?", false),
        arrayOf("Penguins are birds?", true),
        arrayOf("The kiwi bird is native to Australia?", false),
        arrayOf("European Robins are known to be migratory?", true),
        arrayOf("The Harpy Eagle has claws as big as a human hand?", true),
        arrayOf("All birds lay eggs?", true),
        arrayOf("The Hummingbird can fly backwards?", false),
        arrayOf("Albatross has the smallest wingspan among birds?", false),
        arrayOf("Peacock is the national bird of India?", true),
        arrayOf("Male chickens are called hens?", false),
        arrayOf("Crows are considered among the most intelligent birds?", true),
        arrayOf("Owls can rotate their heads 360 degrees?", false),
        arrayOf("A group of flamingos is called a 'flock'?", false),
        arrayOf("Pigeons were used in World War I and World War II to carry messages?", true),
        arrayOf("The Arctic Tern migrates the longest distance among birds?", true),
        arrayOf("The Canary Islands were named after the Canary bird?", false),
        arrayOf("A duck's quack does not echo?", false),
        arrayOf("The smallest bird in the world is the Bee Hummingbird?", true)
    )
    private lateinit var back: ImageView

    //holds questions that have already been answered
    private var alreadyShownQuestions = mutableListOf<Array<Any>>()
    private var totalCorrectAnswers = 0

    // Shuffle and take the first 5 questions
    private var selectedQuestionsAndAnswers = questionsAndAnswers.shuffled().take(5).toTypedArray()
    private var selectedQuestionsOnly = selectedQuestionsAndAnswers.map { it[0] as String }.toList()
    private lateinit var quizAdapter: QuizGameAdapter // reference to adapter

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        back = findViewById(R.id.Back)
        val recyclerView = findViewById<RecyclerView>(R.id.rvQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        quizAdapter = QuizGameAdapter(selectedQuestionsOnly as MutableList<String>) // Initialize adapter
        recyclerView.adapter = quizAdapter

        val checkAnswersButton = findViewById<Button>(R.id.btnCheckAnswers) // Assuming you have a button with this id in your layout
        checkAnswersButton.setOnClickListener {
            checkUserAnswers()
        }

        val changeQuestionsButton = findViewById<Button>(R.id.btnReset)
        changeQuestionsButton.setOnClickListener {
            changeQuestions()
        }

        //-------------------------------------------------------------------------------------------------------------//
        //Back Button
        back.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    //-------------------------------------------------------------------------------------------------------------//
    //Change Questions
    private fun changeQuestions() {
        if (questionsAndAnswers.size == alreadyShownQuestions.size) {
            val totalCorrectAnswers = alreadyShownQuestions.count { it[1] as Boolean == true }
            Toast.makeText(this, "Quiz finished! You got $totalCorrectAnswers out of ${alreadyShownQuestions.size} correct!", Toast.LENGTH_LONG).show()
            return
        }

        var newQuestions: List<Array<Any>> = emptyList()
        do {
            newQuestions = questionsAndAnswers.shuffled().take(5) as List<Array<Any>>
        } while (alreadyShownQuestions.containsAll(newQuestions))

        alreadyShownQuestions.addAll(newQuestions)

        selectedQuestionsAndAnswers = newQuestions.toTypedArray()
        selectedQuestionsOnly = selectedQuestionsAndAnswers.map { it[0] as String }.toList()

        quizAdapter.updateQuestions(selectedQuestionsOnly)
    }

    //-------------------------------------------------------------------------------------------------------------//
    //Check Answers
    private fun checkUserAnswers() {
        val userAnswers = quizAdapter.getUserAnswers()
        var correctCount = 0
        for (i in selectedQuestionsAndAnswers.indices) {
            if (userAnswers[i] == selectedQuestionsAndAnswers[i][1]) {
                correctCount++
            }
        }
        totalCorrectAnswers += correctCount
        Toast.makeText(this, "For this set, you got $correctCount out of 5 correct! Total: $totalCorrectAnswers out of ${alreadyShownQuestions.size + 5}!", Toast.LENGTH_SHORT).show()
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
