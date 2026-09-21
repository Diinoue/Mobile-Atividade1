package com.example.flagquiz.modelo

import com.example.flagquiz.R
import java.text.Normalizer

data class Bandeira(
    val imagem: Int,
    val pais: String,
    val sinonimos: List<String>
)

object Bandeiras {
    val lista: List<Bandeira> = listOf(
        Bandeira(R.drawable.flag_franca, "França", listOf("france")),
        Bandeira(R.drawable.flag_italia, "Itália", listOf("italy")),
        Bandeira(R.drawable.flag_irlanda, "Irlanda", listOf("ireland")),
        Bandeira(R.drawable.flag_belgica, "Bélgica", listOf("belgium")),
        Bandeira(R.drawable.flag_romenia, "Romênia", listOf("romania")),
        Bandeira(R.drawable.flag_nigeria, "Nigéria", listOf("nigeria")),
        Bandeira(R.drawable.flag_peru, "Peru", listOf()),
        Bandeira(R.drawable.flag_chade, "Chade", listOf("chad")),
        Bandeira(R.drawable.flag_alemanha, "Alemanha", listOf("germany", "deutschland")),
        Bandeira(R.drawable.flag_holanda, "Holanda", listOf("paises baixos", "netherlands")),
        Bandeira(R.drawable.flag_russia, "Rússia", listOf("russia")),
        Bandeira(R.drawable.flag_austria, "Áustria", listOf("austria")),
        Bandeira(R.drawable.flag_colombia, "Colômbia", listOf("colombia")),
        Bandeira(R.drawable.flag_armenia, "Armênia", listOf("armenia")),
        Bandeira(R.drawable.flag_ucrania, "Ucrânia", listOf("ukraine")),
        Bandeira(R.drawable.flag_polonia, "Polônia", listOf("poland")),
        Bandeira(R.drawable.flag_indonesia, "Indonésia", listOf("indonesia")),
        Bandeira(R.drawable.flag_japao, "Japão", listOf("japan")),
        Bandeira(R.drawable.flag_suica, "Suíça", listOf("switzerland"))
    )

    fun sortear(quantidade: Int): List<Bandeira> {
        val embaralhadas = lista.shuffled()

        val sorteadas = ArrayList<Bandeira>()
        for (i in 0 until quantidade) {
            sorteadas.add(embaralhadas[i])
        }

        return sorteadas
    }

    fun estaCorreta(resposta: String, bandeira: Bandeira): Boolean {
        val digitada = normalizar(resposta)

        if (digitada.isEmpty()) {
            return false
        }

        if (digitada == normalizar(bandeira.pais)) {
            return true
        }

        for (sinonimo in bandeira.sinonimos) {
            if (digitada == normalizar(sinonimo)) {
                return true
            }
        }

        return false
    }

    private fun normalizar(texto: String): String {
        val semEspacos = texto.trim()

        val separado = Normalizer.normalize(semEspacos, Normalizer.Form.NFD)
        val semAcento = separado.replace(Regex("\\p{Mn}+"), "")

        val espacoUnico = semAcento.replace(Regex("\\s+"), " ")

        return espacoUnico.lowercase()
    }
}
