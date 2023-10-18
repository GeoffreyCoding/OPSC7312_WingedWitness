package com.example.opsc7312_wingedwitness

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizGameAdapter(private var questions: MutableList<String>) :
    RecyclerView.Adapter<QuizGameAdapter.ViewHolder>() {

    private val userAnswers = Array<Boolean?>(questions.size) { null } // Array to store user answers

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        val yesCheckBox: CheckBox = view.findViewById(R.id.yesCheckBox)
        val noCheckBox: CheckBox = view.findViewById(R.id.noCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragement_quiz, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.questionTextView.text = question

        // Reset checkbox states
        holder.yesCheckBox.isChecked = userAnswers[position] == true
        holder.noCheckBox.isChecked = userAnswers[position] == false

        holder.yesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.noCheckBox.isChecked = false
                userAnswers[position] = true
            }
        }

        holder.noCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.yesCheckBox.isChecked = false
                userAnswers[position] = false
            }
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun resetUserAnswers(newQuestions: List<String>) {
        userAnswers.fill(null) // Reset all answers to null
    }

    fun getUserAnswers(): Array<Boolean?> {
        return userAnswers
    }

    fun updateQuestions(newQuestions: List<String>) {
        userAnswers.fill(null)
        questions.clear()
        questions.addAll(newQuestions)
        notifyDataSetChanged()
    }

}
