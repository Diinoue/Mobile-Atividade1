package com.example.flagquiz.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.flagquiz.R
import com.example.flagquiz.modelo.Bandeira

data class LinhaResposta(
    val bandeira: Bandeira,
    val respostaDigitada: String,
    val acertou: Boolean
)

class RespostaAdapter(
    context: Context,
    linhas: List<LinhaResposta>
) : ArrayAdapter<LinhaResposta>(context, 0, linhas) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.item_resposta, parent, false)
        } else {
            view = convertView
        }

        val linha = getItem(position)

        if (linha != null) {
            preencherLinha(view, linha)
        }

        return view
    }

    private fun preencherLinha(view: View, linha: LinhaResposta) {
        val imgBandeira = view.findViewById<ImageView>(R.id.itemBandeira)
        val txtPais = view.findViewById<TextView>(R.id.itemPais)
        val txtResposta = view.findViewById<TextView>(R.id.itemResposta)
        val txtStatus = view.findViewById<TextView>(R.id.itemStatus)

        imgBandeira.setImageResource(linha.bandeira.imagem)
        txtPais.text = linha.bandeira.pais
        txtResposta.text = context.getString(R.string.sua_resposta, linha.respostaDigitada)

        if (linha.acertou) {
            txtStatus.text = context.getString(R.string.status_acertou)
            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.verde_certo))
        } else {
            txtStatus.text = context.getString(R.string.status_errou)
            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.vermelho_errado))
        }
    }
}
