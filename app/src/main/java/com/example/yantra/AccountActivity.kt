package com.example.yantra

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    private lateinit var loginForm: LinearLayout
    private lateinit var profileView: LinearLayout
    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        loginForm = findViewById(R.id.login_form)
        profileView = findViewById(R.id.profile_view)
        inputName = findViewById(R.id.input_name)
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        profileName = findViewById(R.id.profile_name)
        profileEmail = findViewById(R.id.profile_email)
        val btnSignup = findViewById<TextView>(R.id.btn_signup)
        val btnChangePassword = findViewById<TextView>(R.id.btn_change_password)
        val btnLogout = findViewById<TextView>(R.id.btn_logout)

        btnBack.setOnClickListener { finish() }

        // Check login state
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            showProfile()
        } else {
            showLoginForm()
        }

        // Sign Up button
        btnSignup.setOnClickListener {
            val name = inputName.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save user data
            prefs.edit().apply {
                putBoolean("isLoggedIn", true)
                putString("userName", name)
                putString("userEmail", email)
                putString("userPassword", password)
                apply()
            }

            // Show success message
            showSuccessDialog("Account created successfully!")
            showProfile()
        }

        // Change Password button
        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Logout button
        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            showLoginForm()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoginForm() {
        loginForm.visibility = View.VISIBLE
        profileView.visibility = View.GONE
        inputName.setText("")
        inputEmail.setText("")
        inputPassword.setText("")
    }

    private fun showProfile() {
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = prefs.getString("userName", "User")
        val email = prefs.getString("userEmail", "user@example.com")

        profileName.text = name
        profileEmail.text = email

        loginForm.visibility = View.GONE
        profileView.visibility = View.VISIBLE
    }

    private fun showSuccessDialog(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = layoutInflater.inflate(R.layout.dialog_success, null)
        val txtMessage = view.findViewById<TextView>(R.id.success_message)
        txtMessage.text = message

        dialog.setContentView(view)
        dialog.show()

        // Auto dismiss after 2 seconds
        view.postDelayed({
            dialog.dismiss()
        }, 2000)
    }

    private fun showChangePasswordDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        val view = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val inputNewPassword = view.findViewById<EditText>(R.id.input_new_password)
        val inputConfirmPassword = view.findViewById<EditText>(R.id.input_confirm_password)
        val btnCancel = view.findViewById<TextView>(R.id.btn_cancel)
        val btnSubmit = view.findViewById<TextView>(R.id.btn_submit)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val newPassword = inputNewPassword.text.toString()
            val confirmPassword = inputConfirmPassword.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update password
            val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            prefs.edit().putString("userPassword", newPassword).apply()

            dialog.dismiss()
            showSuccessDialog("Password updated successfully!")
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
