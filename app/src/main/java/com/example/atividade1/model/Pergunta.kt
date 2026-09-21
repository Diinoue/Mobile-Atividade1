package com.example.soccerteam.model

import java.io.Serializable

data class Pergunta(
    val pais: Pais,
    val enunciado: String,
) : Serializable