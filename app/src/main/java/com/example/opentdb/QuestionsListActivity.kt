package com.example.opentdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.text.htmlEncode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_list)

        title = "Questions"
        val retrieverOpentdbMap = OpentdbRetriever()
        val callbackOpentdbMap = object : Callback<OpentdbQuery> {
            override fun onFailure(call: Call<OpentdbQuery>, t: Throwable) {
                println("Something went wrong")
            }

            override fun onResponse(call: Call<OpentdbQuery>, response: Response<OpentdbQuery>) {
                if (response.body()?.results != null) {
                    val questionResults = response.body()?.results
                    if (questionResults != null) {
                        var questionsList = mutableListOf<String>()
                        for (questionResult in questionResults) {
                            val question = questionResult.question
                            val answer = questionResult.correct_answer
                            questionsList.add("Question: $question")
                            println("Question: $question, Answer: $answer")

                        }
                        var listView = findViewById<ListView>(R.id.questionsListView)
                        println("list: $questionsList")
                        var adapter = ArrayAdapter(this@QuestionsListActivity, android.R.layout.simple_list_item_1, questionsList)
                        listView.adapter = adapter

                    }
                }
            }

        }
        retrieverOpentdbMap.getOpentdbQuestions(callbackOpentdbMap)
    }
}