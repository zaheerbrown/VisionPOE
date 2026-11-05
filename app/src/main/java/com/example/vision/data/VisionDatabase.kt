package com.example.vision.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.security.SecureRandom

/** Local offline storage. Accounts are device-local, NOT cloud authentication. */
class VisionDatabase(context: Context) : SQLiteOpenHelper(context, "vision.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users (email TEXT PRIMARY KEY, salt TEXT NOT NULL, hash TEXT NOT NULL, sport TEXT NOT NULL DEFAULT 'Football')")
        db.execSQL("CREATE TABLE favourites (email TEXT NOT NULL, product_id INTEGER NOT NULL, PRIMARY KEY(email,product_id))")
        db.execSQL("CREATE TABLE cart (email TEXT NOT NULL, product_id INTEGER NOT NULL, quantity INTEGER NOT NULL, PRIMARY KEY(email,product_id))")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
    private fun hex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }
    private fun digest(salt: String, password: String): String {
        var bytes = (salt + password).toByteArray(Charsets.UTF_8)
        repeat(12000) { bytes = MessageDigest.getInstance("SHA-256").digest(bytes) }
        return hex(bytes)
    }
    fun register(email: String, password: String): Boolean {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }.let(::hex)
        return try {
            writableDatabase.insertOrThrow("users", null, ContentValues().apply {
                put("email", email); put("salt", salt); put("hash", digest(salt, password))
            }); true
        } catch (_: android.database.SQLException) { false }
    }
    fun authenticate(email: String, password: String): Boolean {
        readableDatabase.rawQuery("SELECT salt,hash FROM users WHERE email=?", arrayOf(email)).use { c ->
            if (!c.moveToFirst()) return false
            return MessageDigest.isEqual(digest(c.getString(0), password).toByteArray(), c.getString(1).toByteArray())
        }
    }
    fun load(email: String) {
        Store.favourites.clear(); Store.cart.clear()
        readableDatabase.rawQuery("SELECT product_id FROM favourites WHERE email=?", arrayOf(email)).use { c ->
            while (c.moveToNext()) Store.favourites.add(c.getInt(0))
        }
        readableDatabase.rawQuery("SELECT product_id,quantity FROM cart WHERE email=?", arrayOf(email)).use { c ->
            while (c.moveToNext()) Store.cart[c.getInt(0)] = c.getInt(1)
        }
    }
    fun save(email: String) {
        writableDatabase.beginTransaction()
        try {
            writableDatabase.delete("favourites", "email=?", arrayOf(email))
            writableDatabase.delete("cart", "email=?", arrayOf(email))
            Store.favourites.forEach { id -> writableDatabase.insert("favourites", null, ContentValues().apply { put("email", email); put("product_id", id) }) }
            Store.cart.forEach { (id, qty) -> writableDatabase.insert("cart", null, ContentValues().apply { put("email", email); put("product_id", id); put("quantity", qty) }) }
            writableDatabase.setTransactionSuccessful()
        } finally { writableDatabase.endTransaction() }
    }
    fun sport(email: String): String {
        readableDatabase.rawQuery("SELECT sport FROM users WHERE email=?", arrayOf(email)).use { c -> return if (c.moveToFirst()) c.getString(0) else "Football" }
    }
    fun setSport(email: String, sport: String) {
        writableDatabase.update("users", ContentValues().apply { put("sport", sport) }, "email=?", arrayOf(email))
    }
}
