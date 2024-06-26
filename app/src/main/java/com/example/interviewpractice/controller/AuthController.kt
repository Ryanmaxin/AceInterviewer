package com.example.interviewpractice.controller

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.interviewpractice.helpers.EH
import com.example.interviewpractice.helpers.getCurrentDate
import com.example.interviewpractice.helpers.verifyGenericString
import com.example.interviewpractice.model.AuthModel
import com.example.interviewpractice.model.MainModel
import com.example.interviewpractice.types.ErrorType
import com.example.interviewpractice.types.FetchType
import com.example.interviewpractice.types.Tag
import com.example.interviewpractice.types.UIError
import com.example.interviewpractice.types.UserException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


class AuthController(mm: MainModel, am: AuthModel): Controller(mm,am,TAG) {

//    override fun fetchData(ft: FetchType) {
//        am.refresh()
//    }
    fun verifyRegister(
        username: String,
        password: String,
        confirm: String,
        email: String,
        foi: Set<Tag>,
        birthday: Date
    ) {
    am.authLoading = true
    Log.d(TAG,"New register, started authLoading")
        handler("verifyRegister",false) {
            verifyUsernameFormat(username)
            verifyPasswordFormat(password)
            verifyEmailFormat(email)
            verifyBirthday(birthday)
            verifyFOI(foi)
            verifyConfirm(password,confirm)

            am.loading += 1
            am.isInit = true
            am.createAccount(
                username = username,
                email = email,
                password = password,
                foi=foi,
                birthday = birthday
            )

            //User should now be authenticated, and data stored in the Firestore

            Log.d(TAG,"verifyRegister:success")
        }
    }

    fun verifySignIn(password:String,email:String) {
        am.authLoading = true
        Log.d(TAG,"New sign-in, started authLoading")
        Log.d(TAG,"SIGNING IN AS: $email")
        handler("verifySignIn",false) {
            verifyPasswordFormat(password)
            verifyEmailFormat(email)
            am.loading += 1
            am.isInit = true
            am.signIn(email = email, password = password)
            //User should now be authenticated
            Log.d(TAG,"verifySignIn:success")
            }

    }

    fun verifyLogout() {
        am.authLoading = true
        Log.d(TAG,"New logout, started authLoading")
        handler("verifyLogout") {
            am.loading += 1
            am.isInit = true
            am.logout()
        }
    }

    fun verifyForgotPassword(email: String) {
        handler("verifyForgotPassword",false) {
            verifyEmailFormat(email)

            am.resetPassword(email)
            Log.d(TAG, "verifyForgotPassword:success (sent email)")
        }
    }

    fun verifyUsernameFormat(username: String) {
        //Add more checks here as necessary
        verifyGenericString(username, "Username")
    }
    fun verifyEmailFormat(email: String) {
        //Add more checks here as necessary
        verifyGenericString(email, "Email")
    }
    fun verifyPasswordFormat(password: String) {
        //Add more checks here as necessary
        verifyGenericString(password, "Password")

    }

    fun verifyFOI(foi: Set<Tag>) {
        if (foi.isEmpty()) throw UserException("Please select at least 1 field of interest")
        if (foi.size > 3) throw UserException("You can only choose at most 3 fields of interest")
    }

    fun verifyBirthday(birthday: Date) {
        val today = Calendar.getInstance()
        val birthdate = Calendar.getInstance()
        birthdate.time = birthday

        val years = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR)
        if (years < 16) throw UserException("You must be over 16 to use this app")
    }
    fun verifyConfirm(password: String,confirm: String) {
        if (password != confirm) throw UserException("passwords")
    }

    companion object {
        private const val TAG = "AuthController"
    }
}