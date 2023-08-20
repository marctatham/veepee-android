package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

class MovieListActivity : DaggerAppCompatActivity() {

    private var searchViewExpanded = true
    private var searchViewContent: String = ""

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG).commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            savedInstanceState.getString(QUERY_STRING)?.let {
                searchViewContent = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        searchView = menuItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.isIconified = searchViewExpanded
        searchView.setQuery(searchViewContent, false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                supportFragmentManager.findFragmentByTag(ListFragment.TAG).let {
                    if (it is ListFragment) {
                        it.submitSearchQuery(query)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
            putString(QUERY_STRING, searchView.query.toString())
        }
    }

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val QUERY_STRING = "query_string"
    }
}