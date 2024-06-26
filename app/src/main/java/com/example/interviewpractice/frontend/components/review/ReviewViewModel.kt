package com.example.interviewpractice.frontend.components.review

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.interviewpractice.frontend.MMViewModel

class ReviewViewModel(): MMViewModel() {

    var reviewText = mutableStateOf("")
    var reviewScores = mutableListOf<Pair<String,Int>>()

    override fun update() {
        Log.d("REVIEW VIEW MODEL", "Updated")
    }


}