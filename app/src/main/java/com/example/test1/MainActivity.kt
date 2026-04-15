package com.example.test1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.content.Intent

//import pertemuan 4
import android.provider.MediaStore //library membuka kamera
import android.graphics.Bitmap  //library ngambil foto
import android.widget.ImageView  //library tampilin gambar
import androidx.activity.result.contract.ActivityResultContracts  //library buat ngambil gambar dari galery

class MainActivity : AppCompatActivity() {

    //variabel untuk mengambil foto
    private val ambilFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK){
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            val ivFoto = findViewById<ImageView>(R.id.ivFoto)
            ivFoto.setImageBitmap(imageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //variabel buat masukin variabel inputNama dan btnNext
        val btnNext = findViewById<Button>(R.id.btnNext)
        val inputNama = findViewById<EditText>(R.id.inputNama)

        btnNext.setOnClickListener {
            val nama = inputNama.text.toString()

            val intent = Intent (this, DetailActivity::class.java)
            intent.putExtra("EXTRA_NAMA", nama)
            startActivity(intent)
        }

        //variabel tombol kamera (pertemuan 4)
        val btnFoto = findViewById<Button>(R.id.btnFoto)
        btnFoto.setOnClickListener {
            val klik = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            ambilFoto.launch(klik)
        }
    }
}