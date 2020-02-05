package com.s95ammar.weeklyschedule.views

import android.widget.EditText
import com.s95ammar.weeklyschedule.util.input

class BlankFieldRequiredException(fieldName: String): RuntimeException() {
	override var message: String = "Field $fieldName cannot be blank"
}

@Throws(BlankFieldRequiredException::class)
fun requireNonBlankFields(vararg fieldToContentDesc: Pair<EditText, String>) {
	fieldToContentDesc.forEach { (editText, fieldName) ->
		if (editText.input.isBlank()) throw BlankFieldRequiredException(fieldName)
	}
}
