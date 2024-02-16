package com.example.interviewpractice.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.interviewpractice.model.Model
import com.example.interviewpractice.viewmodel.auth.RegisterViewModel

class QuestionViewModel(private val model: Model, private val dummyFlag: Boolean): Subscriber {

    var showTags = if (!dummyFlag) mutableStateOf(false) else mutableStateOf(true)
    var questionText = if (!dummyFlag) mutableStateOf("") else mutableStateOf("Tell me about yourself")
    var tags = if (!dummyFlag) mutableStateOf(emptyList<String>()) else mutableStateOf(listOf("Open-Ended", "Personal"))

    init {
        model.subscribe(this)
    }

    override fun update() {
        Log.d("QUESTION VIEW MODEL", "Updated")
    }
}