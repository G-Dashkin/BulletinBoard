package com.example.bulletinboard.dialoghelper

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.bulletinboard.MainActivity
import com.example.bulletinboard.R
import com.example.bulletinboard.accounthelper.AccountHelper
import com.example.bulletinboard.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val activity = act
    val accHelper = AccountHelper(activity)

    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(activity)
        val rootDialogElement = SignDialogBinding.inflate(activity.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        setDialogState(index, rootDialogElement)

        val dialog = builder.create()
        rootDialogElement.btSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btForgetP.setOnClickListener {
            setOnClickResetPassword(rootDialogElement, dialog)
        }
        rootDialogElement.btGoogleSignIn.setOnClickListener {
            accHelper.signInWithGoogle()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        if (rootDialogElement.edSignEmail.text.isNotEmpty()){
            activity.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString()).addOnCompleteListener {
                task -> if (task.isSuccessful){
                    Toast.makeText(activity, R.string.email_reset_password_was_sent, Toast.LENGTH_LONG).show()
                }
                }
            dialog?.dismiss()
            } else {
                rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
                }
    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss()
        if(index == DialogConst.SIGN_UP_STATE){
            accHelper.singUpWithEmail(
                rootDialogElement.edSignEmail.text.toString(),
                rootDialogElement.edSignPassword.text.toString())
        } else {
            accHelper.singInWithEmail(
                rootDialogElement.edSignEmail.text.toString(),
                rootDialogElement.edSignPassword.text.toString())
        }
    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {
        if(index == DialogConst.SIGN_UP_STATE){
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_up)
            rootDialogElement.btSignUpIn.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvSignTitle.text = activity.resources.getString(R.string.ac_sign_in)
            rootDialogElement.btSignUpIn.text = activity.resources.getString(R.string.sign_in_action)
            rootDialogElement.btForgetP.visibility = View.VISIBLE
        }
    }
}