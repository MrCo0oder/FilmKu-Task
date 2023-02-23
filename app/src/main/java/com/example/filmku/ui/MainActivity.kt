package com.example.filmku.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmku.R
import com.example.filmku.adapter.MoviesRecyclerAdapter
import com.example.filmku.databinding.ActivityMainBinding
import com.example.filmku.pojo.MovieModel
import com.example.filmku.utils.Status
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
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        setContentView(binding.root)
        initRV()
        loadData()
        setupChipGroup()
        binding.swipe.setOnRefreshListener {
            loadData()
            binding.swipe.isRefreshing = false
        }
    }

    private fun setupChipGroup() {
        binding.chipGroup.check(R.id.year_chip)
        binding.yearChip.setOnClickListener {
            topMoviesList = topMoviesList.sortedByDescending { t -> t.year }
            rvAdapter.setData(topMoviesList)
            (binding.moviesRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
        }

        binding.rateChip.setOnClickListener {
            topMoviesList = topMoviesList.sortedByDescending { t -> t.imDbRating }
            rvAdapter.setData(topMoviesList)
            (binding.moviesRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
        }
        binding.nameChip.setOnClickListener {
            topMoviesList = topMoviesList.sortedBy { t -> t.title }
            rvAdapter.setData(topMoviesList)
            (binding.moviesRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
        }
    }

    private fun showSnack(status: Status, errorMessage: String) {
        val snack: Snackbar =
            Snackbar.make(binding.snackBar, errorMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.toggle_color))
                .setTextColor(Color.BLACK).setAction("retry") { loadData() }
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
        viewModel.networkState.observe(this) {
            errorMsg = it.status
            showSnack(errorMsg, it.msg)
        }
        viewModel.response.observe(this@MainActivity) {

            if (it.errorMessage.isNotEmpty()) {
                errorMsg = Status.FAILED
                showSnack(errorMsg, it.errorMessage)
            }
            when (binding.chipGroup.checkedChipId) {
                R.id.rate_chip -> {
                    topMoviesList = it.items.sortedByDescending { t -> t.imDbRating }
                    rvAdapter.setData(topMoviesList)
                }
                R.id.name_chip -> {
                    topMoviesList = it.items.sortedBy { t -> t.title }
                    rvAdapter.setData(topMoviesList)
                }
                else -> {
                    topMoviesList = it.items.sortedByDescending { t -> t.year }
                    rvAdapter.setData(topMoviesList)
                }
            }

        }
    }

    private fun initRV() {
        errorMsg = Status.RUNNING
        topMoviesList = ArrayList()
        binding.moviesRv.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.moviesRv.setHasFixedSize(true)
        rvAdapter = MoviesRecyclerAdapter()
        binding.moviesRv.adapter = rvAdapter
        rvAdapter.setData(topMoviesList)
    }

}