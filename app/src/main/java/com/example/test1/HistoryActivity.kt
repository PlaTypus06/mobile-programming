package com.example.test1

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class HistoryActivity : AppCompatActivity() {

    // 1. Deklarasi variabel tingkat class (Global)
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var lvRiwayat: ListView
    private lateinit var btnHapus: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        // Mengatur padding agar tidak tertutup notch/status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Inisialisasi View sesuai ID di XML
        lvRiwayat = findViewById(R.id.lvRiwayat)
        btnHapus = findViewById(R.id.btnHapus)
        dbHelper = DatabaseHelper(this)

        // 3. Langsung panggil muat data agar saat pindah activity, list langsung terisi
        muatDataKeLayar()

        // 4. Logika tombol hapus
        btnHapus.setOnClickListener {
            dbHelper.hapusSemuaData()
            muatDataKeLayar() // Refresh tampilan jadi kosong
            Toast.makeText(this, "Semua riwayat dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk mengambil data dari SQLite dan memasukkannya ke ListView
    fun muatDataKeLayar() {
        val cursor = dbHelper.bacaSemuaData()
        val listData = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                val nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"))
                val lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"))
                val lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"))

                // Format tampilan di dalam list
                listData.add("Nama: $nama\nLokasi: $lat, $lon")
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Pasang adapter ke ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listData)
        lvRiwayat.adapter = adapter
    }
}