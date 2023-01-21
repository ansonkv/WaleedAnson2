package com.waleed.app.util

import android.widget.EditText
import android.widget.TextView

fun TextView.setLanguageString(englishString: String, arabicString: String) {
    this.text = LangUtils.getLanguageString(englishString, arabicString)
}

fun EditText.getStringValue(): String {
    return if (this.text.toString() == null) {
        ""
    } else {
        this.text.toString().trim()
    }

}
fun EditText.setTextValue(valueString: String){
    this.setText(valueString, TextView.BufferType.EDITABLE);
}
fun calculateProductAmount(price: Double, quantity: Int): String {
    val product = price * quantity

    return String.format("%.3f", product)
}
