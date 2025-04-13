package com.example.ailawyer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Review(val reviewerName: String, val rating: Float, val comment: String, val imgKey: Char)

class ReviewAdapter(private val reviewList: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewerName: TextView = itemView.findViewById(R.id.reviewerName)
        val ratingBar: RatingBar = itemView.findViewById(R.id.reviewRatingBar)
        val comment: TextView = itemView.findViewById(R.id.reviewComment)
        val profile: ImageView = itemView.findViewById(R.id.reviewerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]

        // Set text and rating
        holder.reviewerName.text = review.reviewerName
        holder.ratingBar.rating = review.rating
        holder.comment.text = review.comment

        // Dynamically load image from drawable using imgKey (like 'a', 'b', etc.)
        val context = holder.profile.context
        val imageName = review.imgKey.lowercaseChar().toString() // 'a', 'b', etc.
        val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

        if (resId != 0)
            holder.profile.setImageResource(resId)
    }



    override fun getItemCount(): Int = reviewList.size
}
