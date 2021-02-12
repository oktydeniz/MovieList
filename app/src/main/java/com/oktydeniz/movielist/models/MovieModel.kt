package com.oktydeniz.movielist.models

import android.graphics.Bitmap

class MovieModel(title: String, ratingDate: String, rating: Float, movieImage: Bitmap) {
    var title: String? = title
    var ratingDate: String? = ratingDate
    var rating: Float? = rating
    var movieImage: Bitmap? = movieImage
    var id: Int? = null
}