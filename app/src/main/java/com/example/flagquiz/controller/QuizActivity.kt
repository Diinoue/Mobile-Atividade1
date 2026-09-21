package com.example.flagquiz.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flagquiz.R
import com.example.flagquiz.modelo.Bandeira
import com.example.flagquiz.modelo.Bandeiras
import com.example.flagquiz.modelo.RespostaQuiz
import com.example.flagquiz.modelo.Resultado

class QuizActivity : AppCompatActivity() {
    private lateinit var txtContador: TextView
    private lateinit var imgBandeira: ImageView
    private lateinit var editResposta: EditText
    private lateinit var btnResponder: Button
    private lateinit var txtFeedback: TextView
    private lateinit var btnProxima: Button

    private var nome = ""

    private var sorteadas = IntArray(TOTAL_PERGUNTAS)

    private val respostasDadas = ArrayList<String>()
    private val acertos = ArrayList<Boolean>()

    private var perguntaAtual = 0
    private var pontuacao = 0
    private var jaRespondeu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        txtContador = findViewById(R.id.txtContador)
        imgBandeira = findViewById(R.id.imgBandeira)
        editResposta = findViewById(R.id.editResposta)
        btnResponder = findViewById(R.id.btnResponder)
        txtFeedback = findViewById(R.id.txtFeedback)
        btnProxima = findViewById(R.id.btnProxima)

        nome = intent.getStringExtra("nome") ?: ""

        if (savedInstanceState == null) {
            sortearBandeiras()
        } else {
            recuperarPartida(savedInstanceState)
        }

        btnResponder.setOnClickListener {
            responder()
        }

        btnProxima.setOnClickListener {
            avancar()
        }

        mostrarPergunta()

        if (jaRespondeu) {
            val ultimaResposta = respostasDadas[respostasDadas.size - 1]
            val ultimoAcerto = acertos[acertos.size - 1]

            editResposta.setText(ultimaResposta)
            mostrarFeedback(ultimoAcerto, bandeiraAtual())
        }
    }

    private fun sortearBandeiras() {
        val bandeirasDaPartida = Bandeiras.sortear(TOTAL_PERGUNTAS)

        for (i in bandeirasDaPartida.indices) {
            val bandeira = bandeirasDaPartida[i]
            sorteadas[i] = Bandeiras.lista.indexOf(bandeira)
        }
    }

    private fun bandeiraAtual(): Bandeira {
        val posicao = sorteadas[perguntaAtual]
        return Bandeiras.lista[posicao]
    }

    private fun mostrarPergunta() {
        val bandeira = bandeiraAtual()
        val numeroDaPergunta = perguntaAtual + 1

        txtContador.text = getString(R.string.contador_formato, numeroDaPergunta, TOTAL_PERGUNTAS)
        imgBandeira.setImageResource(bandeira.imagem)

        editResposta.setText("")
        editResposta.isEnabled = true
        btnResponder.isEnabled = true

        txtFeedback.visibility = View.INVISIBLE
        btnProxima.visibility = View.INVISIBLE
    }

    private fun responder() {
        val resposta = editResposta.text.toString().trim()

        if (resposta.isEmpty()) {
            Toast.makeText(this, R.string.digite_resposta, Toast.LENGTH_SHORT).show()
            return
        }

        val bandeira = bandeiraAtual()
        val acertou = Bandeiras.estaCorreta(resposta, bandeira)

        if (acertou) {
            pontuacao = pontuacao + PONTOS_POR_ACERTO
        }

        respostasDadas.add(resposta)
        acertos.add(acertou)
        jaRespondeu = true

        mostrarFeedback(acertou, bandeira)
    }

    private fun mostrarFeedback(acertou: Boolean, bandeira: Bandeira) {
        if (acertou) {
            txtFeedback.text = getString(R.string.correto)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.verde_certo))
        } else {
            txtFeedback.text = getString(R.string.incorreto, bandeira.pais)
            txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.vermelho_errado))
        }

        txtFeedback.visibility = View.VISIBLE

        editResposta.isEnabled = false
        btnResponder.isEnabled = false

        val ultimaPergunta = perguntaAtual == TOTAL_PERGUNTAS - 1
        if (ultimaPergunta) {
            btnProxima.setText(R.string.ver_placar)
        } else {
            btnProxima.setText(R.string.proxima)
        }

        btnProxima.visibility = View.VISIBLE
    }

    private fun avancar() {
        val ultimaPergunta = perguntaAtual == TOTAL_PERGUNTAS - 1

        if (ultimaPergunta) {
            irParaPlacar()
        } else {
            perguntaAtual = perguntaAtual + 1
            jaRespondeu = false
            mostrarPergunta()
        }
    }

    private fun irParaPlacar() {
        val respostas = ArrayList<RespostaQuiz>()

        for (i in 0 until TOTAL_PERGUNTAS) {
            val resposta = RespostaQuiz(sorteadas[i], respostasDadas[i], acertos[i])
            respostas.add(resposta)
        }

        val resultado = Resultado(nome, pontuacao, respostas)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("resultado", resultado)
        startActivity(intent)

        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putIntArray(KEY_SORTEADAS, sorteadas)
        outState.putInt(KEY_ATUAL, perguntaAtual)
        outState.putInt(KEY_PONTOS, pontuacao)
        outState.putBoolean(KEY_RESPONDEU, jaRespondeu)
        outState.putStringArrayList(KEY_RESPOSTAS, respostasDadas)
        outState.putBooleanArray(KEY_ACERTOS, montarArrayDeAcertos())
    }

    private fun recuperarPartida(savedInstanceState: Bundle) {
        sorteadas = savedInstanceState.getIntArray(KEY_SORTEADAS) ?: IntArray(TOTAL_PERGUNTAS)
        perguntaAtual = savedInstanceState.getInt(KEY_ATUAL)
        pontuacao = savedInstanceState.getInt(KEY_PONTOS)
        jaRespondeu = savedInstanceState.getBoolean(KEY_RESPONDEU)

        val respostasSalvas = savedInstanceState.getStringArrayList(KEY_RESPOSTAS)
        if (respostasSalvas != null) {
            respostasDadas.addAll(respostasSalvas)
        }

        val acertosSalvos = savedInstanceState.getBooleanArray(KEY_ACERTOS)
        if (acertosSalvos != null) {
            for (acerto in acertosSalvos) {
                acertos.add(acerto)
            }
        }
    }

    private fun montarArrayDeAcertos(): BooleanArray {
        val array = BooleanArray(acertos.size)

        for (i in acertos.indices) {
            array[i] = acertos[i]
        }

        return array
    }

    companion object {
        const val TOTAL_PERGUNTAS = 5
        const val PONTOS_POR_ACERTO = 20

        private const val KEY_SORTEADAS = "sorteadas"
        private const val KEY_ATUAL = "atual"
        private const val KEY_PONTOS = "pontos"
        private const val KEY_RESPONDEU = "respondeu"
        private const val KEY_RESPOSTAS = "respostas"
        private const val KEY_ACERTOS = "acertos"
    }
}
