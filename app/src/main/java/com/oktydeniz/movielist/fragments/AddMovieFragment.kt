package com.oktydeniz.movielist.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.oktydeniz.movielist.R
import com.oktydeniz.movielist.adapters.SQLAdapter
import com.oktydeniz.movielist.databinding.FragmentAddMovieBinding
import com.oktydeniz.movielist.util.ImageSettings
import com.oktydeniz.movielist.models.MovieModel
import java.util.*


class AddMovieFragment : Fragment() {

    private var binding: FragmentAddMovieBinding? = null
    private var selectedPictureUri: Uri? = null
    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { it ->
            val status = AddMovieFragmentArgs.fromBundle(it).action
            val id = AddMovieFragmentArgs.fromBundle(it).id
            if (status == "old") {
                binding!!.movieNameEditText.setText("")
                binding!!.ratingBar.rating = 0f
                binding!!.saveData.visibility = View.INVISIBLE
                binding!!.deleteItem.visibility = View.VISIBLE
                binding!!.updateData.visibility = View.VISIBLE
                getSelectedData(id)
                binding!!.deleteItem.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setPositiveButton(resources.getText(R.string.delete)) { _, _ ->
                        SQLAdapter(requireContext()).deleteOneData(id)
                        val action =
                            AddMovieFragmentDirections.actionAddMovieFragmentToMoviesFragment()
                        Navigation.findNavController(it).navigate(action)
                    }
                    builder.setNegativeButton(resources.getText(R.string.cancel)) { _, _ ->
                    }
                    builder.setCancelable(false)
                    builder.setTitle(resources.getText(R.string.delete))
                    builder.setIcon(R.drawable.delete_drawable)
                    builder.setMessage(resources.getText(R.string.alert_message_item))
                    val dialog = builder.create()
                    dialog.show()
                }

            } else {
                binding!!.movieNameEditText.setText("")
                binding!!.ratingBar.rating = 0f
                binding!!.saveData.visibility = View.VISIBLE
                binding!!.updateData.visibility = View.INVISIBLE
                binding!!.deleteItem.visibility = View.INVISIBLE
                insertData()
            }
        }
    }

    private fun getSelectedData(id: Int) {
        val find = SQLAdapter(requireContext()).selectedData(id)
        if (find != null) {
            binding!!.movieNameEditText.setText(find.title)
            binding!!.addMovieImageView.setImageBitmap(find.movieImage)
            binding!!.ratingBar.rating = find.rating!!
        }
        binding!!.addMovieImageView.setOnClickListener {
            getImage()
        }

        binding!!.updateData.setOnClickListener {
            //copy imageView image  from sql to image variable
            val drawable = binding!!.addMovieImageView.drawable as BitmapDrawable
            val bitmap: Bitmap = drawable.bitmap
            //if user don't wanna change movie image, we need to copy image and put again
            //inside model
            selectedBitmap = ImageSettings.makeImageSmall(bitmap)
            val title = binding!!.movieNameEditText.text.toString()
            val rating = binding!!.ratingBar.rating
            val image = selectedBitmap!!
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val ratingDate = "$day/$month/$year"
            val model = MovieModel(title, ratingDate, rating, image)
            model.id = id
            SQLAdapter(requireContext()).updateData(model)
            val action = AddMovieFragmentDirections.actionAddMovieFragmentToMoviesFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun insertData() {
        binding!!.addMovieImageView.setOnClickListener {
            getImage()
        }
        binding!!.saveData.setOnClickListener {
            if (selectedBitmap != null) {
                val title = binding!!.movieNameEditText.text.toString()
                val rating = binding!!.ratingBar.rating
                val image = ImageSettings.makeImageSmall(selectedBitmap!!)
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val ratingDate = "$day/$month/$year"
                val model = MovieModel(title, ratingDate, rating, image!!)
                val insert = SQLAdapter(requireContext())
                insert.insertData(model)
                val action = AddMovieFragmentDirections.actionAddMovieFragmentToMoviesFragment()
                Navigation.findNavController(it).navigate(action)
            } else {
                getImage()
            }
        }
    }

    private fun getImage() {
        activity?.let {
            if (context?.let { context ->
                    ContextCompat.checkSelfPermission(
                        context.applicationContext,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            } else {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPictureUri = data.data
            try {
                if (selectedPictureUri != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(
                            requireContext().contentResolver,
                            selectedPictureUri!!
                        )
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        binding!!.addMovieImageView.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedPictureUri
                        )
                        binding!!.addMovieImageView.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}


