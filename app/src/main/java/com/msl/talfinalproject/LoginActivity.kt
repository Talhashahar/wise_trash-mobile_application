package com.msl.talfinalproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.login_activity_view.*

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_view)

        registration_button.setOnClickListener { v ->
            this.login(input_name.text.toString(), input_password.text.toString())
        }
    }

    fun login(userName: String, pass: String) {
//        if (userName.equals("test") and pass.equals("Password1!")) {
            var i : Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
//        } else {
//            AlertDialog.createWrongInputDialog(this, "Wrong Username or Password!")
//        }
    }
}