package com.example.filmku.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.filmku.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String) {

    Picasso.get().load(url).placeholder(R.drawable.ic_placeholder).into(view, object :
        Callback {
        override fun onSuccess() {
        }

        override fun onError(e: Exception) {
        }
    })
}