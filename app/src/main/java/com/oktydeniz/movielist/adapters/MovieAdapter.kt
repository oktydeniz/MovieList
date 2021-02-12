package com.oktydeniz.movielist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.oktydeniz.movielist.R
import com.oktydeniz.movielist.fragments.MoviesFragmentDirections
import com.oktydeniz.movielist.models.MovieModel

class MovieAdapter(
    private val model: List<MovieModel>, private val context: Context,
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        holder.movieTitle.text = model[position].title
        holder.movieRating.rating = model[position].rating!!.toFloat()
        holder.movieRatingDate.text = model[position].ratingDate
        holder.movieImage.setImageBitmap(model[position].movieImage)
        holder.cardView.setOnClickListener {
            val action = MoviesFragmentDirections.actionMoviesFragmentToAddMovieFragment(
                "old",
                model[position].id!!
            )
            Navigation.findNavController(it).navigate(action)
        }
        val animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
        holder.cardView.animation = animation
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        val movieRatingDate: TextView = itemView.findViewById(R.id.movie_rating_date)
        val movieRating: RatingBar = itemView.findViewById(R.id.movie_rating)
        val movieImage: ImageView = itemView.findViewById(R.id.movie_image)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }
}