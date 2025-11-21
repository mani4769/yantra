package com.example.yantra

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnSendMessage = findViewById<TextView>(R.id.btn_send_message)

        btnBack.setOnClickListener { finish() }

        btnSendMessage.setOnClickListener {
            Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}
