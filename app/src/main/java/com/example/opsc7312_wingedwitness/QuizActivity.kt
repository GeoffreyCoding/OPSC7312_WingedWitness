package com.example.opsc7312_wingedwitness

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizActivity : AppCompatActivity() {

    private val questionsAndAnswers = arrayOf(
        arrayOf("Is the sky blue?", true),
        arrayOf("Is grass green?", true),
        arrayOf("Is water dry?", false),
        arrayOf("Is fire cold?", false),
        arrayOf("Is sugar salty?", false)
    )

    private val questionsOnly = questionsAndAnswers.map { it[0] as String }.toList()
    private lateinit var quizAdapter: QuizGameAdapter // reference to adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val recyclerView = findViewById<RecyclerView>(R.id.rvQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        quizAdapter = QuizGameAdapter(questionsOnly) // Initialize adapter
        recyclerView.adapter = quizAdapter

        val checkAnswersButton = findViewById<Button>(R.id.btnCheckAnswers) // Assuming you have a button with this id in your layout
        checkAnswersButton.setOnClickListener {
            checkUserAnswers()
        }
    }

    private fun checkUserAnswers() {
        val userAnswers = quizAdapter.getUserAnswers()
        var correctCount = 0
        for (i in questionsAndAnswers.indices) {
            if (userAnswers[i] == questionsAndAnswers[i][1]) {
                correctCount++
            }
        }
        Toast.makeText(this, "You got $correctCount out of ${questionsOnly.size} correct!", Toast.LENGTH_SHORT).show()
    }
}
