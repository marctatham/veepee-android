package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listViewModel: ListViewModel
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    private var currentQuery: String = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        savedInstanceState?.getString(CURRENT_QUERY)?.let {
            currentQuery = it
        }

        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(
            viewLifecycleOwner
        ) { searchResult: SearchResult? ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        }
        listViewModel.searchMoviesByTitle(currentQuery, 1)
        showProgressBar()
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("app://movies/detail/?imdbID=$it")
            }
            startActivity(intent)
        }
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView.addOnScrollListener(gridPagingScrollListener)

        // Swipe down to Refresh
        swipeRefreshLayout.setOnRefreshListener { refreshSearch() }
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(swipeRefreshLayout)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorTextView)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult) {
            SearchResult.Error -> showError()
            SearchResult.InProgress -> showProgressBar()
            is SearchResult.Success -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
        }
        gridPagingScrollListener.markLoading(false)
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.listItems = searchResult.items
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    private fun refreshSearch() {
        gridPagingScrollListener.markLoading(true)
        listViewModel.refreshSearch()
    }

    override fun onItemClick(imdbID: String) {
        // this module intentionally contains no dependency on the "Detail" feature-module
        // therefore, we instead rely on the Implicit resolution of any activity that can
        // handle the intent to VIEW this URI, as is defined in the  Detail feature-module's
        // AndroidManifest file
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("app://movies/detail/?imdbID=$imdbID")
        }
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}