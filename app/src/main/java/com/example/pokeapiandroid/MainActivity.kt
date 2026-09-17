package com.example.pokeapiandroid

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var imageViewPokemon: ImageView
    private lateinit var textName: TextView
    private lateinit var textHeight: TextView
    private lateinit var textWeight: TextView
    private lateinit var textTypes: TextView
    private lateinit var btnFetch: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageViewPokemon = findViewById(R.id.imageViewPokemon)
        textName = findViewById(R.id.textName)
        textHeight = findViewById(R.id.textHeight)
        textWeight = findViewById(R.id.textWeight)
        textTypes = findViewById(R.id.textTypes)
        btnFetch = findViewById(R.id.btnFetch)
        progressBar = findViewById(R.id.progressBar)

        btnFetch.setOnClickListener {
            fetchDittoData()
        }
        //Cargar Data
        fetchDittoData()

    }

    private fun fetchDittoData(){
        progressBar.visibility = View.VISIBLE
        btnFetch.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    RetrofitClient.apiService.getDitto()
                }
                progressBar.visibility = View.GONE
                btnFetch.isEnabled = true
                val capitalizedName = response.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }
                textName.text = "Nombre: $capitalizedName"
                textHeight.text = "Altura: ${response.height}"
                textWeight.text = "Peso: ${response.weight}"

                val typeList = response.types.joinToString(", "){ it.type.name}
                textTypes.text = "Tipo(s): $typeList"

                response.sprites.frontDefault?.let { imageUrl ->
                    Glide.with(this@MainActivity)
                        .load(imageUrl)
                        .into(imageViewPokemon)
                }

            } catch (e: Exception){
                progressBar.visibility = View.GONE
                btnFetch.isEnabled = true
                Log.e("POKE_DEBUG", "Error al consumir la API POKEAP", e)
                Toast.makeText(this@MainActivity, "Erro al cargar Pokémon: ${e.localizedMessage}",
                    Toast.LENGTH_LONG).show()
            }

        }

    }

}