package com.fastcon.etherapp.service

import android.util.Patterns
import com.fastcon.etherapp.util.common.Commons
import com.google.android.material.textfield.TextInputLayout

object AuthenticationModel {
    fun validateEmail(textInputEmail: TextInputLayout): Boolean {
        val emailInput: String = textInputEmail.editText?.text.toString().trim()
        return if (emailInput.isEmpty()) {
            textInputEmail.error = "Field can't be empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.error = "Please enter a valid email address"
            false
        } else {
            textInputEmail.error = null
            true
        }
    }

    fun validateUsername(textInputUsername: TextInputLayout): Boolean {
        val usernameInput: String =
            textInputUsername.editText?.text.toString().trim()
        return if (usernameInput.isEmpty()) {
            textInputUsername.error = "Field can't be empty"
            false
        } else if (usernameInput.length > 20) {
            textInputUsername.error = "Username too long"
            false
        } else {
            textInputUsername.error = null
            true
        }
    }

    fun validatePassword(textInputPassword: TextInputLayout): Boolean {
        val passwordInput: String =
            textInputPassword.editText?.text.toString().trim()
        return if (passwordInput.isEmpty()) {
            textInputPassword.error = "Field can't be empty"
            false
        } else if (!Commons.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.error = "Password too weak"
            false
        } else {
            textInputPassword.error = null
            true
        }
    }


}