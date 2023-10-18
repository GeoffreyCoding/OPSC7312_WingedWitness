package com.example.opsc7312_wingedwitness

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizActivity : AppCompatActivity() {

    private val questionsAndAnswers = listOf(
        arrayOf("Is the sky blue?", true),
        arrayOf("Is grass green?", true),
        arrayOf("Is water dry?", false),
        arrayOf("Is fire cold?", false),
        arrayOf("Is sugar salty?", false),
        arrayOf("Is the sky blue?", true),
        arrayOf("Is grass green?", true),
        arrayOf("Is water dry?", false),
        arrayOf("Is fire cold?", false),
        arrayOf("Is sugar salty?", false),
        arrayOf("Is the sky blue?", true),
        arrayOf("Is grass green?", true),
        arrayOf("Is water dry?", false),
        arrayOf("Is fire cold?", false),
        arrayOf("Is sugar salty?", false),
        arrayOf("Is the sky blue?", true),
        arrayOf("Is grass green?", true),
        arrayOf("Is water dry?", false),
        arrayOf("Is fire cold?", false),
        arrayOf("Is sugar salty?", false)
    )

    // Shuffle and take the first 5 questions
    private var selectedQuestionsAndAnswers = questionsAndAnswers.shuffled().take(5).toTypedArray()
    private var selectedQuestionsOnly = selectedQuestionsAndAnswers.map { it[0] as String }.toList()
    private lateinit var quizAdapter: QuizGameAdapter // reference to adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

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

    }

    private fun changeQuestions() {
        // Shuffle and take a new set of 5 questions
        selectedQuestionsAndAnswers = questionsAndAnswers.shuffled().take(5).toTypedArray()
        selectedQuestionsOnly = selectedQuestionsAndAnswers.map { it[0] as String }.toList()

        // Reset the user's answers
        quizAdapter.updateQuestions(selectedQuestionsOnly)
        quizAdapter.notifyDataSetChanged()
    }


    private fun checkUserAnswers() {
        val userAnswers = quizAdapter.getUserAnswers()
        var correctCount = 0
        for (i in selectedQuestionsAndAnswers.indices) {
            if (userAnswers[i] == selectedQuestionsAndAnswers[i][1]) {
                correctCount++
            }
        }
        Toast.makeText(this, "You got $correctCount out of ${selectedQuestionsOnly.size} correct!", Toast.LENGTH_SHORT).show()
    }



}
