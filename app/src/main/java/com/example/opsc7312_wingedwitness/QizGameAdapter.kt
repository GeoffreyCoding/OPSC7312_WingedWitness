package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//---------------------------------------------------------------------------------------------------------------------//


class QuizGameAdapter(private var questions: MutableList<String>) :
    RecyclerView.Adapter<QuizGameAdapter.ViewHolder>() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private val userAnswers = Array<Boolean?>(questions.size) { null } // Array to store user answers

    //-----------------------------------------------------------------------------------------------------------------//
    //Class for the View Holder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        val yesCheckBox: CheckBox = view.findViewById(R.id.yesCheckBox)
        val noCheckBox: CheckBox = view.findViewById(R.id.noCheckBox)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragement_quiz, parent, false)
        return ViewHolder(view)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Binding Method
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

    //-----------------------------------------------------------------------------------------------------------------//
    //Get Count
    override fun getItemCount(): Int {
        return questions.size
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Reset Answers
    fun resetUserAnswers(newQuestions: List<String>) {
        userAnswers.fill(null) // Reset all answers to null
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Get Answers
    fun getUserAnswers(): Array<Boolean?> {
        return userAnswers
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Update Questions
    fun updateQuestions(newQuestions: List<String>) {
        userAnswers.fill(null)
        questions.clear()
        questions.addAll(newQuestions)
        notifyDataSetChanged()
    }

}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
