package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class FavoriteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // retrieve viewModel
        val viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        // setup simple recyclerview
        setContentView(R.layout.activity_favorite)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = FavoritesAdapter(this) { favorite ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("app://movies/detail/?imdbID=${favorite.id}")
            }

            startActivity(intent)
        }
        val layoutManager = GridLayoutManager(
            this,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        // observe favorites
        viewModel.favorites().observe(this) {
            adapter.favorites = it
        }
    }


}
