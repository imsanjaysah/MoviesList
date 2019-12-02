/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.ui.movies

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.sanjay.movieslist.R
import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.ui.BaseActivity
import com.sanjay.movieslist.utils.Utility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.content_movie_detail.*
import kotlinx.android.synthetic.main.content_movie_detail.txt_release_year
import kotlinx.android.synthetic.main.content_movie_header.*


/**
 * Activity to display detail data
 */
class MovieDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collapse_tool_bar.setExpandedTitleColor(Color.TRANSPARENT);

        (intent.getParcelableExtra("Movie") as Movie).apply {
            setData(this)
        }

    }

    private fun setData(movie: Movie) {
        supportActionBar?.title = movie.title

        txt_release_year.text = Utility.formatDate(movie.releaseDate)
        txt_title.text = movie.title
        txt_description.text = movie.description
        txt_rating.text = movie.rating.toString()
        if (movie.poster?.isNotEmpty() == true) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300_and_h450_bestv2${movie.poster}").into(iv_poster)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    companion object {
        fun start(activity: Activity, movie: Movie, photoImageView: ImageView? = null) {

            if (photoImageView != null) {
                val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    photoImageView,
                    "photo"
                )
                activity.startActivity(Intent(activity, MovieDetailActivity::class.java).apply {
                    putExtra("Movie", movie)
                }, options.toBundle())
            } else {
                activity.startActivity(Intent(activity, MovieDetailActivity::class.java).apply {
                    putExtra("Movie", movie)
                })
            }

        }
    }
}