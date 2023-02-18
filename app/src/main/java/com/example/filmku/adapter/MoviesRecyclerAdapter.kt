package com.example.filmku.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmku.R
import com.example.filmku.databinding.MovieCardBinding
import com.example.filmku.pojo.MovieModel

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MoviesRecyclerAdapter :
    RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder>() {

    lateinit var moviesList: List<MovieModel>

    class ViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel) {
            binding.listItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val listItemBinding = MovieCardBinding.inflate(itemView, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val url: String = moviesList[position].image
        Picasso.get().load(url).error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder).into(holder.binding.movieIv, object : Callback {

            override fun onSuccess() {
                holder.binding.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception) {
                Log.d("pop Adapter", e.message.toString())
                holder.binding.progressBar.visibility = View.GONE
            }
        })
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setData(list: List<MovieModel>) {
        moviesList = list
        notifyDataSetChanged()
    }

}


