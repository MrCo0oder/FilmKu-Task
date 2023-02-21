package com.example.filmku.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmku.databinding.MovieCardBinding
import com.example.filmku.pojo.MovieModel

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


