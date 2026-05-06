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

//import kebutuhan map
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

//import lokasi
import org.osmdroid.views.overlay.Marker
import android.location.Location
import android.location.LocationManager
import android.content.Context
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest

class MainActivity : AppCompatActivity() {



    //function dapat lokasi GPS real-time
    private fun ambilLokasiGps(mapView: MapView){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        //coba ambil lokasi dari GPS
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        location?.let {
            val userLocation = GeoPoint(it.latitude, it.longitude)

            val markerBaru = Marker(mapView)
            markerBaru.position = userLocation
            markerBaru.title = "Lokasi Foto"
            markerBaru.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapView.overlays.add(markerBaru)

            mapView.controller.animateTo(userLocation)
            mapView.controller.setZoom(18.0)
            mapView.invalidate()

            android.widget.Toast.makeText(this, "Lokasi Terdeteksi!", android.widget.Toast.LENGTH_SHORT).show()
        } ?: run {
            android.widget.Toast.makeText(this, "GPS Belum Siap", android.widget.Toast.LENGTH_LONG).show()
        }
    }


    //variabel untuk mengambil foto
    private val ambilFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK){
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            val ivFoto = findViewById<ImageView>(R.id.ivFoto)
            ivFoto.setImageBitmap(imageBitmap)

            val mapView = findViewById<MapView>(R.id.mapView)
            ambilLokasiGps(mapView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inslatalasi konfigurasi OSM
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))


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

        //MapView
        val mapView = findViewById<MapView>(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        //controll map ke titik tertentu
        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(-8.6705, 115.2126)
        mapController.setCenter(startPoint)

    }
}