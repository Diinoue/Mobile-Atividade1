package com.example.flagquiz.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flagquiz.R

class MainActivity : AppCompatActivity() {
    private lateinit var editNome: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editNome = findViewById(R.id.editNome)

        editNome.setText(intent.getStringExtra("nome") ?: "")
    }

    fun goNextActivity(view: View) {
        val nome = editNome.text.toString().trim()
        if (nome.isEmpty()) {
            Toast.makeText(this, R.string.informe_nome, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("nome", nome)
        startActivity(intent)
    }
}
