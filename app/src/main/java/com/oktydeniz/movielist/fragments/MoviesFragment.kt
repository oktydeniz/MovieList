package com.oktydeniz.movielist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.oktydeniz.movielist.R
import com.oktydeniz.movielist.adapters.MovieAdapter
import com.oktydeniz.movielist.adapters.SQLAdapter
import com.oktydeniz.movielist.databinding.FragmentMoviesBinding
import com.oktydeniz.movielist.models.MovieModel
import kotlin.collections.ArrayList

class MoviesFragment : Fragment() {

    private var binding: FragmentMoviesBinding? = null
    private var model = ArrayList<MovieModel>()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializing()
        binding!!.floatingActionButton.setOnClickListener {
            val action = MoviesFragmentDirections.actionMoviesFragmentToAddMovieFragment("new", 0)
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(action)
        }
    }

    private fun initializing() {
        model = SQLAdapter(requireContext()).readAllData()
        adapter = MovieAdapter(model, requireContext())
        binding!!.moviesRecyclerView.adapter = adapter
        if (model.size > 0) {
            binding!!.imageView.visibility = View.INVISIBLE
            adapter.notifyDataSetChanged()
        } else {
            binding!!.imageView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }
}