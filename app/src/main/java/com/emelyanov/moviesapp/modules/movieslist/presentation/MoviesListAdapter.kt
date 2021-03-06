package com.emelyanov.moviesapp.modules.movieslist.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.databinding.MoviesListGenreItemBinding
import com.emelyanov.moviesapp.databinding.MoviesListHeaderItemBinding
import com.emelyanov.moviesapp.databinding.MoviesListMovieItemBinding
import com.emelyanov.moviesapp.modules.movieslist.domain.models.MoviesRecyclerItem



class MoviesListAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: List<MoviesRecyclerItem>
        get() = builder.build()

    var builder: MoviesRecyclerItemsBuilder = MoviesRecyclerItemsBuilder()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(
                binding = MoviesListHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            GENRE_VIEW_TYPE -> GenreViewHolder(
                binding = MoviesListGenreItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            MOVIE_VIEW_TYPE -> MovieViewHolder(
                binding = MoviesListMovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Wrong view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = items[position]) {
            is MoviesRecyclerItem.Header -> {
                val header = holder as HeaderViewHolder
                header.binding.title.text = context.getString(item.resId)
            }
            is MoviesRecyclerItem.Genre -> {
                val genre = holder as GenreViewHolder
                genre.binding.genreName.text = item.name
                val background = if(item.isSelected) R.drawable.genre_item_background_selected else R.drawable.genre_item_background
                genre.binding.root.setBackgroundResource(background)
                genre.binding.root.setOnClickListener {
                    item.onClick()
                }
            }
            is MoviesRecyclerItem.Movie -> {
                val movie = holder as MovieViewHolder
                movie.binding.movieName.text = item.name
                movie.binding.root.setOnClickListener {
                    item.onClick()
                }
                movie.binding.movieImage.load(item.imageUrl) {
                    this.listener(
                        onStart = { _, ->
                            movie.binding.movieImageError.visibility = View.GONE
                        },
                        onError = { _, _ ->
                            movie.binding.movieImageError.visibility = View.VISIBLE
                        }
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = builder.getItemsCount()

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is MoviesRecyclerItem.Header -> HEADER_VIEW_TYPE
            is MoviesRecyclerItem.Genre -> GENRE_VIEW_TYPE
            is MoviesRecyclerItem.Movie -> MOVIE_VIEW_TYPE
        }
    }

    class HeaderViewHolder(val binding: MoviesListHeaderItemBinding): RecyclerView.ViewHolder(binding.root)
    class GenreViewHolder(val binding: MoviesListGenreItemBinding): RecyclerView.ViewHolder(binding.root)
    class MovieViewHolder(val binding: MoviesListMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    class MoviesRecyclerItemsBuilder {
        private val genres: MutableList<MoviesRecyclerItem.Genre> = mutableListOf()
        private val movies: MutableList<MoviesRecyclerItem.Movie> = mutableListOf()

        fun build(): List<MoviesRecyclerItem> = ArrayList<MoviesRecyclerItem>(genres.size+movies.size+2).apply {
            add(MoviesRecyclerItem.Header(R.string.genres_header))
            addAll(genres)
            add(MoviesRecyclerItem.Header(R.string.movies_header))
            addAll(movies)
        }

        fun addGenre(name: String, isSelected: Boolean, onClick: () -> Unit) = genres.add(MoviesRecyclerItem.Genre(name, isSelected, onClick))
        fun addMovie(name: String, imageUrl: String, onClick: () -> Unit) = movies.add(MoviesRecyclerItem.Movie(name, imageUrl, onClick))

        internal fun getItemsCount() = genres.size + movies.size + 2
    }

    companion object {
        const val HEADER_VIEW_TYPE = 1
        const val GENRE_VIEW_TYPE = 2
        const val MOVIE_VIEW_TYPE = 3
    }
}