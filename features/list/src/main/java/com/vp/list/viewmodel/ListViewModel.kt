package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject internal constructor(
    private val searchService: SearchService
) : ViewModel() {

    private val liveData = MutableLiveData<SearchResult>()
    private var currentTitle = ""
    private val aggregatedItems: MutableList<ListItem> = ArrayList()

    fun observeMovies(): LiveData<SearchResult> = liveData

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
        }

        liveData.value = SearchResult.InProgress
        initiateSearchAsync(page)
    }

    fun refreshSearch() {
        aggregatedItems.clear()
        initiateSearchAsync(1)
    }

    private fun initiateSearchAsync(page: Int) {
        viewModelScope.launch {
            try {
                val response: SearchResponse = searchService.search(currentTitle, page)
                aggregatedItems.addAll(response.search)
                liveData.value = SearchResult.Success(aggregatedItems, response.totalResults)
            } catch (e: Exception) {
                liveData.value = SearchResult.Error
            }
        }
    }
}