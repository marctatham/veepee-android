package com.vp.list.viewmodel

import com.vp.list.helpers.MainDispatcherRule
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()

    private var searchService: SearchService = mockk()

    private lateinit var viewModel: ListViewModel
    private lateinit var viewStates: MutableList<SearchResult>

    @Before
    fun setup() {
        viewStates = mutableListOf()

        viewModel = ListViewModel(searchService)
        viewModel.observeMovies().observeForever { viewStates.add(it) }
    }

    @Test
    fun `should set the inProgress state when searching for movies`() {
        // Given
        val title = "The Matrix"
        val page = 1

        // When
        viewModel.searchMoviesByTitle(title, page)
        coVerify { searchService.search(title, page) }

        // Then
        Assert.assertEquals(SearchResult.InProgress, viewStates[0])
    }

    @Test
    fun `should set the Loaded state when searching for movies completed successfully`() {
        // Given
        val title = "The Matrix"
        val page = 1
        val response = SearchResponse("", emptyList(), 0)
        coEvery { searchService.search(title, page) } returns response

        // When
        viewModel.searchMoviesByTitle(title, page)
        coVerify { searchService.search(title, page) }

        // Then
        Assert.assertEquals(SearchResult.InProgress, viewStates[0])
        Assert.assertTrue(viewStates[1] is SearchResult.Success)
    }

    @Test
    fun `should set the Error state when searching for movies failed`() {
        // Given
        val title = "The Matrix"
        val page = 1
        coEvery { searchService.search(title, page) } throws IOException()

        // When
        viewModel.searchMoviesByTitle(title, page)
        coVerify { searchService.search(title, page) }

        // Then
        Assert.assertEquals(SearchResult.InProgress, viewStates[0])
        Assert.assertTrue(viewStates[1] is SearchResult.Error)
    }


    @Test
    fun `should set the Loaded state when refreshing search results is successful`() {
        // Given
        val response = SearchResponse("", emptyList(), 0)
        coEvery { searchService.search(any(), any()) } returns response

        // When
        viewModel.refreshSearch()

        // Then
        Assert.assertTrue(viewStates[0] is SearchResult.Success)
    }
}