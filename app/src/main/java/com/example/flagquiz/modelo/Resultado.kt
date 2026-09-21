package com.example.flagquiz.modelo

import java.io.Serializable

data class RespostaQuiz(
    val indiceBandeira: Int,
    val respostaDigitada: String,
    val acertou: Boolean
) : Serializable

data class Resultado(
    val nome: String,
    val pontuacao: Int,
    val respostas: ArrayList<RespostaQuiz>
) : Serializable
