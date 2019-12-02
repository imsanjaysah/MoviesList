/*
 * MoviesResponse.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.repository.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Sanjay.Sah
 */

data class MoviesResponse(
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_results") val totalCount: Int,
    @SerializedName("page") val page: Int
)

@Parcelize
data class Movie(
    val id: String,
    @SerializedName("poster_path") val poster: String?,
    val title: String,
    @SerializedName("overview") val description: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("release_date") val releaseDate: String
) : Parcelable
