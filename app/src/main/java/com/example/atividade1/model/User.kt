package com.example.atividade1.model

import java.io.Serializable

data class User(
    val nome: String,
    var pontos: Int
) : Serializable