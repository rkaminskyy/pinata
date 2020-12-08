package com.example.pinatafarm.presentation

import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.pinatafarm.R
import com.example.pinatafarm.domain.Person
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel: SharedViewModel by sharedViewModel()

    private lateinit var imageView: ImageView
    private lateinit var labelView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        labelView = view.findViewById(R.id.label)
        imageView = view.findViewById(R.id.image)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getSelectedPerson().observe(viewLifecycleOwner, Observer { person ->
            try {
                val mutableBitmap = createMutableBitmap(person)
                drawFaceBounds(person, mutableBitmap)
                Glide.with(this).load(mutableBitmap).into(imageView)
                if (person.faceBounds.isNullOrEmpty()) throw Exception("No face detected")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun drawFaceBounds(person: Person, mutableBitmap: Bitmap) {
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        person.faceBounds?.let {
            it.forEach {
                canvas.drawRect(it, paint)
            }
        }
    }

    private fun createMutableBitmap(person: Person): Bitmap {
        val stream = resources.assets.open("img/${person.img}")
        val bitmap = BitmapFactory.decodeStream(stream)
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}