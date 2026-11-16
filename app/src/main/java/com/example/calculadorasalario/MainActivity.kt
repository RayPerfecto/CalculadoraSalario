package com.example.calculadorasalario

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        configurarSpinner(
            R.id.spGrupoProfesional,
            listOf("Grupo A", "Grupo B", "Grupo C", "Grupo D", "Grupo E")
        )

        configurarSpinner(
            R.id.spNumeroPagas,
            listOf("12 pagas", "14 pagas")
        )

        configurarSpinner(
            R.id.spEstadoCivil,
            listOf("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a")
        )

        findViewById<Button>(R.id.btnCalcular).setOnClickListener {
            calcular()
        }
    }

    private fun configurarSpinner(id: Int, opciones: List<String>) {
        val spinner = findViewById<Spinner>(id)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opciones
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }

    private fun calcular() {
        val edad = findViewById<EditText>(R.id.etEdad).text.toString()
        val discapacidad = findViewById<EditText>(R.id.etDiscapacidad).text.toString()
        val bruto = findViewById<EditText>(R.id.etSalarioBruto).text.toString()
        val hijos = findViewById<EditText>(R.id.etNumeroHijos).text.toString()

        if (edad.isBlank() || discapacidad.isBlank() ||
            bruto.isBlank() || hijos.isBlank()
        ) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val salarioBruto = bruto.replace(",", ".").toDoubleOrNull()
        val numeroEdad = edad.toIntOrNull()
        val porcentajeDiscapacidad = discapacidad.toIntOrNull()
        val numeroHijos = hijos.toIntOrNull()

        if (salarioBruto == null || salarioBruto !in 1.0..1000000.0 ||
            numeroEdad == null || numeroEdad !in 16..100 ||
            porcentajeDiscapacidad == null || porcentajeDiscapacidad !in 0..100 ||
            numeroHijos == null || numeroHijos !in 0..20
        ) {
            Toast.makeText(
                this,
                "Comprueba los datos introducidos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = Intent(this, ResultadoActivity::class.java)

        intent.putExtra("edad", numeroEdad)
        intent.putExtra("grupo", findViewById<Spinner>(R.id.spGrupoProfesional).selectedItem.toString())
        intent.putExtra("pagas", findViewById<Spinner>(R.id.spNumeroPagas).selectedItem.toString())
        intent.putExtra("discapacidad", porcentajeDiscapacidad)
        intent.putExtra("bruto", salarioBruto)
        intent.putExtra("estadoCivil", findViewById<Spinner>(R.id.spEstadoCivil).selectedItem.toString())
        intent.putExtra("hijos", numeroHijos)

        startActivity(intent)
    }
}
