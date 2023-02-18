package com.example.filmku.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.filmku.R
import com.example.filmku.adapter.MoviesRecyclerAdapter
import com.example.filmku.databinding.ActivityMainBinding
import com.example.filmku.pojo.MovieModel
import com.example.filmku.repository.Status
import com.example.filmku.viewmodels.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var topMoviesList: List<MovieModel>
    private lateinit var rvAdapter: MoviesRecyclerAdapter
    private lateinit var errorMsg: Status
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initRV()
        loadData()
        binding.swipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadData()
            binding.swipe.isRefreshing = false
        })
        binding.floatingActionButton.setOnClickListener {
            (binding.moviesRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
        }
        binding.yearButton.setOnClickListener {
            topMoviesList = topMoviesList.sortedByDescending { t -> t.year }
            rvAdapter.setData(topMoviesList)
            binding.yearButton.isEnabled = false
            binding.rateButton.isEnabled = true
            binding.yearButton.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.toggle_color
                )
            )
            binding.rateButton.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.black
                )
            )
        }
        binding.rateButton.setOnClickListener {
            topMoviesList = topMoviesList.sortedByDescending { t -> t.imDbRating }
            rvAdapter.setData(topMoviesList)
            binding.yearButton.isEnabled = true
            binding.rateButton.isEnabled = false
            binding.rateButton.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.toggle_color
                )
            )
            binding.yearButton.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.black
                )
            )
        }
    }

    private fun showSnack(status: Status) {
        val snack: Snackbar =
            Snackbar.make(binding.snackBar, "Something went wrong", Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.toggle_color))
                .setTextColor(Color.BLACK)
                .setAction("retry") { loadData() }
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        when (status) {
            Status.SUCCESS -> {
                binding.swipe.isRefreshing = false
                binding.progressBar2.visibility = View.GONE
                snack.dismiss()
            }
            Status.RUNNING -> {
                binding.swipe.isRefreshing = true
                binding.progressBar2.visibility = View.VISIBLE
                snack.dismiss()
            }
            else -> {
                binding.swipe.isRefreshing = false
                binding.progressBar2.visibility = View.GONE
                snack.show()
            }
        }

    }

    private fun loadData() {
        viewModel.getTopMovies()
        viewModel.networkState.observe(this, Observer {
            errorMsg = it.status
            showSnack(errorMsg)
        })
        viewModel.response.observe(this@MainActivity, Observer {

            if (it.errorMessage.isNotEmpty()) {
                errorMsg = Status.FAiLD
                showSnack(errorMsg)
            }
            if (!binding.yearButton.isEnabled) {
                topMoviesList = it.items.sortedByDescending { t -> t.year }
                rvAdapter.setData(topMoviesList)
            } else {
                topMoviesList = it.items.sortedByDescending { t -> t.imDbRating }
                rvAdapter.setData(topMoviesList)
            }

        })
    }

    private fun initRV() {
        errorMsg = Status.RUNNING
        topMoviesList = ArrayList()
        binding.moviesRv.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.moviesRv.setHasFixedSize(true)
        binding.moviesRv.setHasFixedSize(true)
        rvAdapter = MoviesRecyclerAdapter()
        binding.moviesRv.adapter = rvAdapter
        rvAdapter.setData(topMoviesList)
    }

}