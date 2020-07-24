package com.fastcon.etherapp.util.functions

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import com.google.android.material.textfield.TextInputLayout

class KeyBoardUtils {

    companion object {
    /**
     *  Show softKeyboard.
     * @param context
     */

    fun showKeyBoard(@NonNull context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * Hide softKeyboard.
     *
     * @param activity
     * @param editText
     */
    fun editTextHideKeyBoard(activity: Activity, editText: EditText) {
        if ((activity.currentFocus == null) || activity.currentFocus !is EditText) {
            editText.requestFocus()
        }
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }


    /**
     * Hide softKeyboard.
     *
     * @param activity
     * @param TextLayout
     */
    fun textLayoutHideKeyBoard(activity: Activity, editText: TextInputLayout) {
        if ((activity.currentFocus == null) || activity.currentFocus !is TextInputLayout) {
            editText.requestFocus()
        }
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}
}