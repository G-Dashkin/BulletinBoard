package com.example.bulletinboard.accounthelper

import android.util.Log
import android.widget.Toast
import com.example.bulletinboard.MainActivity
import com.example.bulletinboard.R
import com.example.bulletinboard.constants.FirebaseAuthConstants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class AccountHelper(act:MainActivity) {
    private val activity = act
    val signInRequestCode = 132
    private lateinit var signInClient: GoogleSignInClient

    fun singUpWithEmail(email:String, password:String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            activity.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                task -> if (task.isSuccessful){
                    sendEmailVerification(task.result?.user!!)
                    activity.uiUpdate(task.result?.user)

                    } else {
//                        Toast.makeText(activity, activity.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()

                        if(task.exception is FirebaseAuthUserCollisionException){
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if(exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE){
                                Toast.makeText(activity, FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                                // Link email
                                linkEmailToG(email, password)
                            }
                        } else if(task.exception is FirebaseAuthInvalidCredentialsException ){
                            val exception = task.exception as FirebaseAuthInvalidCredentialsException
                            if(exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL){
                                Toast.makeText(activity, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            }
                        }

                        if(task.exception is FirebaseAuthWeakPasswordException){
                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            if(exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD){
                                Toast.makeText(activity, FirebaseAuthConstants.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                            }
                        }

                    }
            }
        }
    }

    fun singInWithEmail(email:String, password:String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            activity.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    task -> if (task.isSuccessful){
                activity.uiUpdate(task.result?.user)
            } else {
                Log.d("MyLog", "Exception : ${task.exception}")
                if(task.exception is FirebaseAuthInvalidCredentialsException ){
                    val exception = task.exception as FirebaseAuthInvalidCredentialsException
                    Log.d("MyLog", "Exception : ${exception.errorCode}")
                    if(exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL){
                        Toast.makeText(activity, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                    } else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD){
                        Toast.makeText(activity, FirebaseAuthConstants.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                    }
                } else if(task.exception is FirebaseAuthInvalidUserException){
                    val exception = task.exception as FirebaseAuthInvalidUserException
                    if(exception.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND){
                        Toast.makeText(activity, FirebaseAuthConstants.ERROR_USER_NOT_FOUND, Toast.LENGTH_LONG).show()
                    }
                }
            }
            }
        }
    }

    private fun linkEmailToG(email:String, password: String){
        val credential = EmailAuthProvider.getCredential(email, password)
        if(activity.mAuth.currentUser != null){
        activity.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener {
            task -> if (task.isSuccessful){
                Toast.makeText(activity, activity.resources.getString(R.string.link_done), Toast.LENGTH_LONG).show()
        }
        }
        } else {
            Toast.makeText(activity, activity.resources.getString(R.string.enter_to_g), Toast.LENGTH_LONG).show()
        }
    }

    private fun getSignInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        activity.startActivityForResult(intent, signInRequestCode)
    }

    fun signOutG(){
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token:String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.mAuth.signInWithCredential(credential).addOnCompleteListener{
            task -> if(task.isSuccessful){
                Toast.makeText(activity, "Sign in done", Toast.LENGTH_LONG).show()
                activity.uiUpdate(task.result?.user)
        } else {
            Log.d("MyLog", "Google Sign In Exception : ${task.exception}" )
        }
        }
    }

    private fun sendEmailVerification(user:FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener {
            task -> if (task.isSuccessful){
            Toast.makeText(activity, activity.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, activity.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
        }
        }
    }
}