package com.example.calculadorasalario

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale
import androidx.activity.enableEdgeToEdge

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val bruto = intent.getDoubleExtra("bruto", 0.0)
        val hijos = intent.getIntExtra("hijos", 0)
        val discapacidad = intent.getIntExtra("discapacidad", 0)
        val estadoCivil = intent.getStringExtra("estadoCivil") ?: ""

        // Calcular porcentaje IRPF simulado
        var irpf = when {
            bruto <= 18000 -> 8.0
            bruto <= 30000 -> 12.0
            bruto <= 45000 -> 17.0
            else -> 22.0
        }

        // Reducciones simuladas
        irpf -= hijos.coerceAtMost(3) * 1.0

        if (discapacidad >= 33) {
            irpf -= 2.0
        }

        if (estadoCivil == "Casado/a") {
            irpf -= 0.5
        }

        irpf = irpf.coerceIn(2.0, 30.0)

        // Cálculo de deducciones
        val importeIrpf = bruto * irpf / 100
        val cotizaciones = bruto * 0.065
        val deducciones = importeIrpf + cotizaciones

        // Salario neto
        val neto = bruto - deducciones

        val pagasTexto = intent.getStringExtra("pagas") ?: "12 pagas"
        val numeroPagas = if (pagasTexto == "14 pagas") 14 else 12

        val netoPorPaga = neto / numeroPagas

        // Formato de moneda española
        val formatoEuro = NumberFormat.getCurrencyInstance(Locale("es", "ES"))

        findViewById<TextView>(R.id.tvBruto).text =
            formatoEuro.format(bruto)

        findViewById<TextView>(R.id.tvNeto).text =
            formatoEuro.format(neto)

        findViewById<TextView>(R.id.tvNetoPorPaga).text =
            formatoEuro.format(netoPorPaga)

        findViewById<TextView>(R.id.tvIrpf).text =
            String.format(Locale("es", "ES"), "%.1f %%", irpf)

        findViewById<TextView>(R.id.tvDeducciones).text =
            formatoEuro.format(deducciones)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}
