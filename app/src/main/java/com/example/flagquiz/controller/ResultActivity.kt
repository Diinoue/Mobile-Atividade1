package com.example.flagquiz.controller

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.flagquiz.R
import com.example.flagquiz.modelo.Bandeiras
import com.example.flagquiz.modelo.Resultado

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultado = recuperarResultado()

        if (resultado == null) {
            finish()
            return
        }

        mostrarPlacar(resultado)
        montarLista(resultado)
        configurarBotaoJogarNovamente(resultado.nome)
    }

    private fun recuperarResultado(): Resultado? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getSerializableExtra("resultado", Resultado::class.java)
        }

        val objeto = intent.getSerializableExtra("resultado")

        if (objeto is Resultado) {
            return objeto
        }

        return null
    }

    private fun mostrarPlacar(resultado: Resultado) {
        val txtNome = findViewById<TextView>(R.id.txtNomeJogador)
        val txtPontuacao = findViewById<TextView>(R.id.txtPontuacao)
        val txtDetalhe = findViewById<TextView>(R.id.txtDetalhe)

        var totalDeAcertos = 0
        for (resposta in resultado.respostas) {
            if (resposta.acertou) {
                totalDeAcertos = totalDeAcertos + 1
            }
        }

        txtNome.text = getString(R.string.placar_nome, resultado.nome)
        txtPontuacao.text = getString(R.string.placar_pontos, resultado.pontuacao)
        txtDetalhe.text = getString(
            R.string.placar_detalhe,
            totalDeAcertos,
            QuizActivity.TOTAL_PERGUNTAS
        )
    }

    private fun montarLista(resultado: Resultado) {
        val linhas = ArrayList<LinhaResposta>()

        for (resposta in resultado.respostas) {
            val bandeira = Bandeiras.lista[resposta.indiceBandeira]
            linhas.add(LinhaResposta(bandeira, resposta.respostaDigitada, resposta.acertou))
        }

        val listRespostas = findViewById<ListView>(R.id.listRespostas)
        listRespostas.adapter = RespostaAdapter(this, linhas)
    }

    private fun configurarBotaoJogarNovamente(nome: String) {
        val btnJogarNovamente = findViewById<Button>(R.id.btnJogarNovamente)

        btnJogarNovamente.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nome", nome)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
