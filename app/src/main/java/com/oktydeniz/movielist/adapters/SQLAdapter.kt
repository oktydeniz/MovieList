package com.oktydeniz.movielist.adapters

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.oktydeniz.movielist.R
import com.oktydeniz.movielist.util.ImageSettings
import com.oktydeniz.movielist.models.MovieModel

class SQLAdapter(
    context: Context
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val context: Context? = context

    companion object {
        private const val DATABASE_NAME = "SQLLITE_DATABASE"
        private const val DATABASE_VERSION = 1
    }

    private val TABLE_NAME = "MovieTable"
    private val COL_ID = "id"
    private val COL_NAME = "movieName"
    private val COL_DATE = "ratingDate"
    private val COL_RATING = "rating"
    private val COL_IMAGE = "movieImage"

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_NAME VARCHAR(100),$COL_DATE VARCHAR(10),$COL_RATING REAL,$COL_IMAGE BLOB)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val update = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(update)
    }

    fun insertData(model: MovieModel) {
        val sqlDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, model.title)
        contentValues.put(COL_DATE, model.ratingDate)
        contentValues.put(COL_RATING, model.rating)
        val image = ImageSettings.bitmapToBytArray(model.movieImage!!)
        contentValues.put(COL_IMAGE, image)
        val result = sqlDB.insert(TABLE_NAME, null, contentValues)
        Toast.makeText(
            context,
            if (result != -1L) context!!.getString(R.string.done) else context!!.getString(R.string.error),
            Toast.LENGTH_LONG
        ).show()
        sqlDB.close()
    }

    fun updateData(model: MovieModel): MovieModel? {
        val sqlDB = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, model.title)
        cv.put(COL_RATING, model.rating)
        cv.put(COL_DATE, model.ratingDate)
        val image = ImageSettings.bitmapToBytArray(model.movieImage!!)
        cv.put(COL_IMAGE, image)
        sqlDB.update(TABLE_NAME, cv, "$COL_ID =?", arrayOf(model.id.toString()))
        return null
    }

    fun readAllData(): ArrayList<MovieModel> {
        val movieList = ArrayList<MovieModel>()
        val query = "SELECT * FROM $TABLE_NAME"
        val sqlDB = this.readableDatabase
        val result = sqlDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            val idIndex = result.getColumnIndex("id")
            val titleIndex = result.getColumnIndex("movieName")
            val ratingDateIndex = result.getColumnIndex("ratingDate")
            val ratingIndex = result.getColumnIndex("rating")
            val imageIndex = result.getColumnIndex("movieImage")
            do {
                val id = result.getString(idIndex).toInt()
                val title = result.getString(titleIndex)
                val ratingDate = result.getString(ratingDateIndex)
                val rating = result.getString(ratingIndex).toFloat()
                val image = ImageSettings.byteArrayToBitmap(result.getBlob(imageIndex))
                val movie = MovieModel(title, ratingDate, rating, image)
                movie.id = id
                movieList.add(movie)
            } while (result.moveToNext())

        }
        result.close()
        sqlDB.close()
        return movieList
    }

    fun deleteAllTable() {
        val sqlDB = this.writableDatabase
        sqlDB.delete(TABLE_NAME, null, null)
        sqlDB.close()
        Toast.makeText(context, context!!.getString(R.string.deleted_all), Toast.LENGTH_LONG).show()
    }

    fun selectedData(id: Int): MovieModel? {
        val sqlDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID =$id"
        val result = sqlDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            val titleIndex = result.getColumnIndex("movieName")
            val ratingDateIndex = result.getColumnIndex("ratingDate")
            val ratingIndex = result.getColumnIndex("rating")
            val imageIndex = result.getColumnIndex("movieImage")
            val title = result.getString(titleIndex)
            val ratingDate = result.getString(ratingDateIndex)
            val rating = result.getString(ratingIndex).toFloat()
            val image = ImageSettings.byteArrayToBitmap(result.getBlob(imageIndex))
            return MovieModel(title, ratingDate, rating, image)
        }
        result.close()
        return null

    }

    fun deleteOneData(id: Int) {
        val sqlDB = this.writableDatabase
        val delete = sqlDB.delete(TABLE_NAME,"$COL_ID=?", arrayOf(id.toString()))
        Toast.makeText(
            context,
            if (delete != -1) context!!.getString(R.string.done) else context!!.getString(R.string.error),
            Toast.LENGTH_LONG
        ).show()
    }


}