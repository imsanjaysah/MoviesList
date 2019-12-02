/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.movieslist.R
import com.sanjay.movieslist.constants.State
import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.utils.Utility.formatDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_footer.view.*
import kotlinx.android.synthetic.main.item_list_movie.view.*
import kotlinx.android.synthetic.main.item_list_movie.view.txt_title


class MoviesListAdapter(private val onItemClick : (Movie, ImageView) -> Unit, private val retry: () -> Unit): PagedListAdapter<Movie, RecyclerView.ViewHolder>(diffCallback) {



    private var state = State.LOADING

    companion object {
        val DATA_VIEW_TYPE = 1
        val FOOTER_VIEW_TYPE = 2
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    private val onItemClickListener = View.OnClickListener {
        val movie = it.tag as Movie
        onItemClick.invoke(movie, it.iv_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) MovieViewHolder.create(parent) else ListFooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as MovieViewHolder).bind(getItem(position)!!, onItemClickListener)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}

/**
 * ViewHolder to display movie information
 */
class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        movie: Movie,
        onItemClickListener: View.OnClickListener
    ) {
        itemView.txt_title.text = movie.title
        if (movie.poster?.isNotEmpty() == true) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300_and_h450_bestv2${movie.poster}").into(itemView.iv_poster);
        } else {
            itemView.iv_poster.setImageResource(R.drawable.ic_launcher_background)
        }
        itemView.txt_release_year.text = formatDate(movie.releaseDate)

        itemView.tag = movie
        itemView.setOnClickListener(onItemClickListener)
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_movie, parent, false)
            return MovieViewHolder(view)
        }
    }
}

/**
 * ViewHolder to display loader at the bottom of the list while fetching next paged data
 */
class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility = if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == State.ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }
}