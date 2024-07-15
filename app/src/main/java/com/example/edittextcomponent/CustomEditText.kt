package com.example.edittextcomponent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

@SuppressLint("SetTextI18n")
class CustomEditText(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    enum class InputType {
        NAME, EMAIL, PASSWORD
    }

    private var inputType: InputType
    private var errorIcon: Drawable? = null
    private var tvError: TextView

    private val editText: EditText
    private val mcvLayout: MaterialCardView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_layout, this, true)

        editText = findViewById(R.id.editText)
        tvError = findViewById(R.id.tvError)
        mcvLayout = findViewById(R.id.mcvLayout)


        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0).apply {
            try {
                val type = getInt(R.styleable.CustomEditText_inputType, 0)
                inputType = InputType.values()[type]
            } finally {
                recycle()
            }

            setHintandError()
        }



        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvError.visibility = View.GONE
                mcvLayout.strokeColor = R.color.blue

            }

            override fun afterTextChanged(s: Editable?) {
                validate()
            }
        })
    }

    private fun validate() {
        val text = editText.text.toString()
        val isValid = when (inputType) {
            InputType.NAME -> text.isNotBlank()
            InputType.EMAIL -> Patterns.EMAIL_ADDRESS.matcher(text).matches()
            InputType.PASSWORD -> text.length >= 6
        }
        if (!isValid) {
            tvError.visibility = View.VISIBLE
        }
        editText.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (isValid) null else errorIcon,
            null
        )
    }

    private fun setHintandError() {

        when (inputType) {
            InputType.NAME -> {
                editText.hint = "Enter Name"
                tvError.text = "*This field can no be empty"
            }

            InputType.PASSWORD -> {
                editText.hint = "Enter Pasword"
                tvError.text = "*Length should be greater than 6"
            }

            InputType.EMAIL -> {
                editText.hint = "Enter Email"
                tvError.text = "*Invalid email type"
            }
        }
    }

}
