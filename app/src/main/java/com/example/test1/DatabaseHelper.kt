package com.example.test1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.NavigableMap

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "AbsensiDB", null, 1){
    override fun onCreate(db : SQLiteDatabase?){
        //tabel history
        val queryBuatTabel = "CREATE TABLE tb_riwayat (id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT, lat TEXT, lon TEXT)"
        db?.execSQL(queryBuatTabel)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tb_riwayat")
        onCreate(db)
    }



    //masukan ke db
    fun simpanRiwayat (nama: String, lat: String, lon: String): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put ("nama", nama)
        values.put ("lat", lat)
        values.put ("lon", lon)
        return db.insert("tb_riwayat", null, values)
    }
    fun bacaSemuaData(): Cursor{
        return readableDatabase.rawQuery("SELECT * FROM tb_riwayat", null)
    }

    fun hapusSemuaData() {
        writableDatabase.execSQL("DELETE FROM tb_riwayat")
    }
}