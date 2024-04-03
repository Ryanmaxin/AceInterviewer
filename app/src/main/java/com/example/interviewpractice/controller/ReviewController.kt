package com.example.interviewpractice.controller

import com.example.interviewpractice.helpers.getCurrentDate
import com.example.interviewpractice.helpers.verifyGenericString
import com.example.interviewpractice.model.AuthModel
import com.example.interviewpractice.model.MainModel
import com.example.interviewpractice.types.AnsweredQuestion
import com.example.interviewpractice.types.Review
import com.example.interviewpractice.types.ReviewScore
import com.example.interviewpractice.types.UserException

class ReviewController(mm: MainModel, am: AuthModel): Controller(mm,am, TAG) {

    fun verifyReview(reviewText: String, clarity: Int, understanding: Int, answeredQuestion: AnsweredQuestion) {
        handler("verifyReview") {
            verifyGenericString(reviewText,"Review text")
            verifyScore(clarity,understanding)

            val currentDate = getCurrentDate()

            val review = Review(am.getUserID(),answeredQuestion.questionID, answeredQuestion.userID,reviewText, ReviewScore(understanding,clarity),currentDate)

            mm.addReview(review)
        }

    }

    private fun verifyScore(clarity: Int, understanding: Int) {
        if (clarity < 1 || clarity > 5 ) throw UserException("Invalid clarity score")
        if (understanding < 1 || understanding > 5) throw UserException("Invalid understanding score")
    }

    fun getUser(): String {
        return am.getUserID()
    }

    companion object {
        private const val TAG = "ReviewController"
    }
}