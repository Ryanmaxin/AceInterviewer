package com.example.interviewpractice.frontend.views.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.interviewpractice.frontend.MMViewModel
import com.example.interviewpractice.frontend.components.historychart.HistoryChartViewModel
import com.example.interviewpractice.model.MainModel
import com.example.interviewpractice.types.User

class ProfileViewModel(): MMViewModel(){

//    val hcVM = HistoryChartViewModel(model)

    val badgeInfo = mutableMapOf<String, Int>()

    //DATA FROM BACKEND
    var user by mutableStateOf<User?>(null)

    init {

        //dummy
        badgeInfo["questionsAnswered"] = 100
    }

    override fun update() {
        if (user != model.user) {
            user = model.user
            Log.d(TAG, "New profile data: ${user.toString()}")
        }


    }
    companion object {
        private const val TAG = "ProfileViewModel"
    }
}