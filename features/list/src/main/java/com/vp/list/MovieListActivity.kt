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
    private var searchView: SearchView? = null

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
        searchView?.let {
            it.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            it.isIconified = searchViewExpanded
            it.setQuery(searchViewContent, false)
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchView?.apply {
            outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, isIconified)
            outState.putString(QUERY_STRING, query.toString())
        }
    }

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val QUERY_STRING = "query_string"
    }
}