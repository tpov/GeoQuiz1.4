package com.bignerdranch.android.geomain

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProviders
//import com.bignerdranch.android.geoquiz.QuizViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prefButton: Button
    private lateinit var questionTextView: TextView

    private var constCurrentIndex = 0
    private var points = 0
    private var persentPoints = 0

    private var codeMap = "111111"
    private var charMap = ""
    private var i = 0
    private var j = 0

    //private const val TAG = "QuizViewModel"
    //private val quizViewModel: QuizViewModel by lazy {
      //  ViewModelProviders.of(this).get(QuizViewModel::class.java)
    //}


    private var mapAnswer: MutableMap<Int, Boolean> = mutableMapOf(
        0 to true,
        1 to true,
        2 to true,
        3 to true,
        4 to true,
        5 to true
    )

    //private const val TAG = "QuizViewModel"

        private var currentIndex = 0

        private val questionBank = listOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        /*
        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(com.bignerdranch.android.geoquiz.TAG, "Got a QuizViewModel: $quizViewModel")
*/
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prefButton = findViewById(R.id.pref_button)
        questionTextView = findViewById(R.id.question_text_view)



        trueButton.setOnClickListener { view: View ->
            checkBlockMap()
            checkBlock()
            coderBlockMap()
            checkAnswer(true)
            constCurrentIndex += 1

        }
        falseButton.setOnClickListener { view: View ->
            checkBlockMap()
            checkBlock()
            coderBlockMap()
            checkAnswer(false)
            constCurrentIndex += 1

        }
        nextButton.setOnClickListener {
            if (currentIndex == 5) {
                val toastNull = Toast.makeText(this, R.string.null_toast, Toast.LENGTH_SHORT)
                toastNull.show()
                result(points)
            } else {
                moveToNext()
                updateQuestion()
            }
            checkBlock()
        }
        prefButton.setOnClickListener {
            if (currentIndex == 0) {
                Toast.makeText(this, R.string.null_toast, Toast.LENGTH_SHORT).show()
            } else {
                moveToPref()
                updateQuestion()
            }
            checkBlock()
        }
        updateQuestion()
    }

    fun moveToPref() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putInt("currentIndex", currentIndex)
            putString("codeMap", codeMap)
            putInt("points", points)
            putInt("persentPoints", persentPoints)
        }
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(saveInstanceState: Bundle) {
        super.onRestoreInstanceState(saveInstanceState)
        currentIndex = saveInstanceState?.getInt("currentIndex")!!
        codeMap = saveInstanceState?.getString("codeMap")!!
        points = saveInstanceState.getInt("points")!!
        persentPoints = saveInstanceState.getInt("persentPoints")!!
        decoderBlockMap()
        updateQuestion()
        checkBlock()
    }

    private fun toastMeState(message: String) {
        Toast.makeText(this, "${lifecycle.currentState}, $message", Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        toastMeState("ON_START")
    }
    override fun onResume() {
        super.onResume()
        toastMeState("ON_RESUME")
    }
    override fun onPostResume() {
        super.onPostResume()
        toastMeState("OnPostResume")
    }
    override fun onPause() {
        super.onPause()
        toastMeState("ON_PAUSE")
    }
    override fun onStop() {
        super.onStop()
        toastMeState("ON_STOP")
    }
    override fun onRestart() {
        super.onRestart()
        toastMeState("ON_Restart")
    }
    override fun onDestroy() {
        super.onDestroy()
        toastMeState("ON_DESTROY")
    }

    private fun updateQuestion() {
        val questionTextResId = currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = currentQuestionAnswer
        val messageResId = if(userAnswer == correctAnswer) {
            points += 1
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
    private fun result(points: Int) {
        persentPoints = points*100/6
        val toastPoints = Toast.makeText(this, "${persentPoints.toString()} %", Toast.LENGTH_SHORT)
        toastPoints.show()
    }
    private fun checkBlock() {
        if (mapAnswer[currentIndex] == false) {
            falseButton.isEnabled = false
            falseButton.isClickable = false
            trueButton.isEnabled = false
            trueButton.isClickable = false
        }
        if (mapAnswer[currentIndex] == true) {
            falseButton.isEnabled = true
            falseButton.isClickable = true
            trueButton.isEnabled = true
            trueButton.isClickable = true
        }
    }

    private fun checkBlockMap() {
        mapAnswer[currentIndex] = false
    }

    private fun coderBlockMap() {
        codeMap = ""
        for (i in 0..5) {
            if(mapAnswer[i] == false) {
                charMap = "0"
                codeMap = "$codeMap$charMap"
            } else {
                charMap = "1"
                codeMap = "$codeMap$charMap"
            }
        }
        i = 0
    }
    private fun decoderBlockMap() {
        for (j in 0..5) {
            if (codeMap[j] == '1') {
                mapAnswer[j] = true
            } else {
                mapAnswer[j] = false
            }
        }
        j = 0
    }
}