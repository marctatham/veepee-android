package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.vp.data.favorites.Favorite
import com.vp.data.favorites.FavoriteRepository
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: LiveData<Boolean> = details.switchMap {
        favoriteRepository.observeById(it.id).map { favorite: Favorite? ->
            favorite != null
        }
    }

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(movieId:String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun toggleFavorite() {
        details.value?.let { movieDetail ->
            isFavorite().value?.let { isFavorite ->
                if (isFavorite) {
                    favoriteRepository.deleteFavorite(movieDetail.id)
                } else {
                    val favorite = Favorite(movieDetail.id, movieDetail.title, movieDetail.poster)
                    favoriteRepository.addFavorite(favorite)
                }
            }
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}
