package com.example.interviewpractice.frontend.views.mainview

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.interviewpractice.controller.AuthController
import com.example.interviewpractice.frontend.views.home.HomeScreen
import com.example.interviewpractice.frontend.views.auth.login.LoginScreen
import com.example.interviewpractice.frontend.views.auth.register.RegisterScreen
import androidx.compose.ui.platform.LocalContext
import com.example.interviewpractice.controller.HistoryController
import com.example.interviewpractice.controller.NotificationController
import com.example.interviewpractice.controller.UserController
import com.example.interviewpractice.controller.QuestionController
import com.example.interviewpractice.controller.ReviewController
import com.example.interviewpractice.frontend.components.navbar.NavBar
import com.example.interviewpractice.frontend.views.profile.leaderboard.LeaderboardView
import com.example.interviewpractice.frontend.views.search.makequestion.MakeQuestionScreen
import com.example.interviewpractice.frontend.views.notifications.Notifications
import com.example.interviewpractice.frontend.views.profile.ProfileView
import com.example.interviewpractice.frontend.views.profile.bestquestions.BestQuestionsViewModel
import com.example.interviewpractice.frontend.views.review.ReviewView
import com.example.interviewpractice.frontend.views.search.SearchView
import com.example.interviewpractice.frontend.views.submitanswer.SubmitAnswer
import com.example.interviewpractice.model.AuthModel
import com.example.interviewpractice.model.MainModel
import com.example.interviewpractice.types.FetchType
import androidx.compose.foundation.layout.Box
import com.example.interviewpractice.frontend.components.Loader
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.interviewpractice.frontend.components.LoadingOverlay
import com.example.interviewpractice.frontend.views.answerquestion.AnswerScreen
import com.google.firebase.auth.FirebaseAuth
import com.example.interviewpractice.frontend.views.seereview.SeeReviewView
import com.example.interviewpractice.types.Notification

@RequiresApi(Build.VERSION_CODES.S)
@Composable
//@Preview
fun MainView(
    vm: MainViewModel,
    uc: UserController,
    ac: AuthController,
    qc: QuestionController,
    rc: ReviewController,
    nc: NotificationController,
    hc: HistoryController,
    am: AuthModel,
    mm: MainModel,
    bestQuestionsViewModel: BestQuestionsViewModel
    )

{
    // NavController //////////////////////////////////////////////////////////
    val unc = rememberNavController()
    val anc = rememberNavController()
    val router: Router = Router(anc)
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        mm.invalidateAll()
        vm.user = firebaseAuth.currentUser
        am.authLoading = false
        Log.d("MAINVIEW","Auth state determined, stopped authLoading")
//        if (user != null) {
//            // User is signed in, you can use the user object here
//            Log.d("MAINVIEW", "onAuthStateChanged:signed_in:" + user.uid)
//            // Proceed with accessing user information or navigating to your main activity
//        } else {
//            // User is signed out
//            Log.d("MAINVIEW", "onAuthStateChanged:signed_out")
//            // Handle the sign-out case or redirect the user to the login activity
//        }
    }

    LaunchedEffect(Unit) {
        mm.invalidateAll()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)

    }
    DisposableEffect(Unit) {
        onDispose {
            vm.unsubscribe()
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        }
    }




    val (dataForSeeReview, setDataForSeeReview) = remember { mutableStateOf(Notification()) }
    val navigateToSeeReview: (Notification) -> Unit = { data ->
        setDataForSeeReview(data)
        anc.navigate("see review") // Navigate to the "see review" screen when data is set
    }

    vm.error?.let {err ->

        //TODO: Work with snackbars instead of Toasts. This is just a placeholder
        val text = err.message
        val duration = Toast.LENGTH_LONG

        val toast = Toast.makeText(LocalContext.current, text, duration) // in Activity
        toast.show()
        am.clearError()
    }
    if (vm.authLoading) {
        Loader()
    }
    else {
        Box {
            if (vm.user != null) {
                //If user is signed in

            NavHost(navController = anc, startDestination = "home") {
                composable("reviews") {
                    ReviewView(mm=mm,c=rc,router=router)
                }
                composable("leaderboard") {
                    LeaderboardView(mm=mm,c=uc)
                }
                composable("answer question") {
                    AnswerScreen(mm=mm,qc=qc, router=router)
                }
                composable("see review") {
                    SeeReviewView(mm = mm, n = dataForSeeReview)
                }
//            composable("best questions") {
//                BestQuestionsView(vm= bestQuestionsViewModel)
//            }
                composable("make question") {
                    MakeQuestionScreen(mm=mm, questionController = qc,
                        goToHome = { anc.navigate("home") })
                }
                composable("home") {
                    HomeScreen(c = ac, mm=mm,qc=qc,uc=uc,r=router)
                }
                composable("notifications") {
                    Notifications(mm=mm, c=nc, goToSeeReview =  navigateToSeeReview)
                }
                composable("search") {
                    SearchView(
                        c = qc, mm=mm, r=router)
                }
                composable("profile") {
                    ProfileView(
                        mm=mm, c = uc, ac=ac, uc=uc, goToLeaderboard = { anc.navigate("leaderboard")}, hc=hc)
                }
            }
            NavBar(
                goToReviews={anc.navigate("reviews")},
                goToSearch={anc.navigate("search")},
                goToHome={anc.navigate("home")},
                goToNotifications={anc.navigate("notifications")},
                goToProfile={anc.navigate("profile")},
                nc=nc,
                mm=mm)

            }
            else {
                NavHost(navController = unc, startDestination = "login") {
                    composable("login") {
                        LoginScreen(c = ac, am=am) { unc.navigate("register") }
                    }
                    composable("register") {
                        RegisterScreen(am=am, c = ac)
                    }
                }
                //composable("question") {
                //    SubmitAnswer(qc = QuestionController(am = am, mm = mm))
                //}
            }

            if (vm.loading > 0) {
                LoadingOverlay()
            }
    }

    }

}
