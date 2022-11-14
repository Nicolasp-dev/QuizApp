package com.kotlin.myquizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

  // Global variables
  private var mUserName: String? = null
  private var mQuestionsList: ArrayList<Question>? = null
  private var mCurrentPosition: Int = 1
  private var mSelectedOptionPosition: Int = 0
  private var mCorrectAnswers: Int = 0

  // Widgets declaration
  private var progressBar: ProgressBar? = null
  private var tvProgress: TextView? = null
  private var tvQuestion: TextView? = null
  private var ivImage: ImageView? = null
  private var tvOptionOne: TextView? = null
  private var tvOptionTwo: TextView? = null
  private var tvOptionThree: TextView? = null
  private var tvOptionFour: TextView? = null
  private var btnSubmit: Button? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quiz_questions)

    // Username Init
    mUserName = intent.getStringExtra(Constants.USER_NAME)

    // Widgets Init
    progressBar = findViewById(R.id.progressBar)
    tvProgress = findViewById(R.id.tv_progress)
    tvQuestion = findViewById(R.id.tv_question)
    ivImage = findViewById(R.id.iv_image)
    tvOptionOne = findViewById(R.id.tv_option_one)
    tvOptionTwo = findViewById(R.id.tv_option_two)
    tvOptionThree = findViewById(R.id.tv_option_three)
    tvOptionFour = findViewById(R.id.tv_option_four)
    btnSubmit = findViewById(R.id.btn_submit)

    // Widgets functionality by "click listener"
    tvOptionOne?.setOnClickListener(this)
    tvOptionTwo?.setOnClickListener(this)
    tvOptionThree?.setOnClickListener(this)
    tvOptionFour?.setOnClickListener(this)
    btnSubmit?.setOnClickListener(this)

    // Questions array Init
    mQuestionsList = Constants.getQuestions()

    // Function Execution
    setQuestion()
  }

  @SuppressLint("SetTextI18n")
  private fun setQuestion() {
    // Reset styles
    defaultOptionsView()

    // QuestionView constructor
    // Select the current question by index position.
    val question: Question = mQuestionsList!![mCurrentPosition - 1]
    // Display current image.
    ivImage?.setImageResource(question.image)
    // Update the progress bar.
    progressBar?.progress = mCurrentPosition
    // Update the textView progress.
    tvProgress?.text = "$mCurrentPosition/${progressBar?.max}"
    // Set widgets question and options text.
    tvQuestion?.text = question.question
    tvOptionOne?.text = question.optionOne
    tvOptionTwo?.text = question.optionTwo
    tvOptionThree?.text = question.optionThree
    tvOptionFour?.text = question.optionFour

    // Conditional: let the app know if we are at the final question.
    if(mCurrentPosition == mQuestionsList!!.size){
      btnSubmit?.text = "FINISH"
    }else{
      btnSubmit?.text = "SUBMIT"
    }
  }

  private fun defaultOptionsView(){
    // Empty array Declaration
    val options = ArrayList<TextView>()

    // Array Filling with options
    tvOptionOne?.let{
      options.add(0, it)
    }
    tvOptionTwo?.let{
      options.add(1, it)
    }
    tvOptionThree?.let{
      options.add(2, it)
    }
    tvOptionFour?.let{
      options.add(3, it)
    }

    // Iteration: for each View, set "default" styles.
    for(option in options){
      option.setTextColor(Color.parseColor("#7A8089"))
      option.typeface = Typeface.DEFAULT
      option.background = ContextCompat.getDrawable(
        this, R.drawable.default_option_border_bg
      )
    }
  }

  // Set styles for the options menu while selecting.
  private fun selectedOptionView(tv: TextView, selectedOptionNum: Int ){
    defaultOptionsView()
    // Update the current "selectedOption".
    mSelectedOptionPosition = selectedOptionNum
    // Styles
    tv.setTextColor(Color.parseColor("#363A43"))
    tv.setTypeface(tv.typeface, Typeface.BOLD)
    tv.background = ContextCompat.getDrawable(
      this, R.drawable.selected_option_border_bg
    )
  }

  // Setting the functionality of the click event.
  @SuppressLint("SetTextI18n")
  override fun onClick(view: View?) {
    when(view?.id){
      R.id.tv_option_one -> {
        tvOptionOne?.let{
          selectedOptionView(it, 1)
        }
      }
      R.id.tv_option_two -> {
        tvOptionTwo?.let{
          selectedOptionView(it, 2)
        }
      }
      R.id.tv_option_three -> {
        tvOptionThree?.let{
          selectedOptionView(it, 3)
        }
      }
      R.id.tv_option_four -> {
        tvOptionFour?.let{
          selectedOptionView(it, 4)
        }
      }
      R.id.btn_submit ->{
        if(mSelectedOptionPosition == 0){
          mCurrentPosition++
          when{
            mCurrentPosition <= mQuestionsList!!.size ->{
              setQuestion()
            }
            else -> {
              val intent = Intent(this, ResultActivity::class.java)
              intent.putExtra(Constants.USER_NAME, mUserName)
              intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
              intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
              startActivity(intent)
              finish()
            }
          }
        }else{
          val question = mQuestionsList?.get(mCurrentPosition - 1)
          // Conditional: Set "Wrong" Answer Style || Score Update.
          if(question!!.correctAnswer != mSelectedOptionPosition){
            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
          }else{
            mCorrectAnswers++
          }
          // Set the "Right" Answer style.
          answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

          // Conditional: Update button text at the end of the quiz.
          if(mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit?.text = "FINISH"
          }else{
            btnSubmit?.text = "NEXT"
          }
          mSelectedOptionPosition = 0
        }
      }
    }
  }

  private fun answerView(answer: Int, drawableView: Int){
    when(answer){
      1 -> {
        tvOptionOne?.background = ContextCompat.getDrawable(
          this, drawableView
        )
      }
      2 -> {
        tvOptionTwo?.background = ContextCompat.getDrawable(
          this, drawableView
        )
      }
      3 -> {
        tvOptionThree?.background = ContextCompat.getDrawable(
          this, drawableView
        )
      }
      4 -> {
        tvOptionFour?.background = ContextCompat.getDrawable(
          this, drawableView
        )
      }
    }

  }
}