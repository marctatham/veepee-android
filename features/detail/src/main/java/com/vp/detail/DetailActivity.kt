package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private lateinit var menuFavorite: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)

        // always observe title
        detailViewModel.title().observe(this) {
            supportActionBar?.title = it
        }

        // always observe isFavorite state
        detailViewModel.isFavorite().observe(this) {
            menuFavorite.icon = AppCompatResources.getDrawable(
                this,
                (if (it) android.R.drawable.btn_star else R.drawable.ic_star)
            )
        }

        // initiate fetch
        val movieId: String? = intent?.data?.getQueryParameter("imdbID")
        movieId?.let { detailViewModel.fetchDetails(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menu?.let {
            menuFavorite = it.findItem(R.id.star)
            menuFavorite.setOnMenuItemClickListener {
                detailViewModel.toggleFavorite()
                true
            }
        }
        return true
    }
}
