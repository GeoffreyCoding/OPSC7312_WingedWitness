package com.example.opsc7312_wingedwitness

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.IOException

class SightingAdapter(private val sightings: List<SightingData>) :
    RecyclerView.Adapter<SightingAdapter.SightingViewHolder>() {

    inner class SightingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
        val txtSightingSpecies: TextView = itemView.findViewById(R.id.txtSightingSpecies)
        val txtSightingDate: TextView = itemView.findViewById(R.id.txtSightingDate)
        val txtSightingLocation: TextView = itemView.findViewById(R.id.txtSightingLocation)
        val ivBirdImage: ImageView = itemView.findViewById(R.id.ivBirdImage)
        val playButton: ImageButton = itemView.findViewById(R.id.play)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sighting_item, parent, false)
        return SightingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SightingViewHolder, position: Int) {
        // Bind data to UI elements in the ViewHolder
        val currentSighting = sightings[position]

        // Update TextViews
        holder.txtSightingName.text = "Name: ${currentSighting.sightingName}"
        holder.txtSightingSpecies.text = "Species: ${currentSighting.sightingSpecies}"
        holder.txtSightingDate.text = "Date: ${currentSighting.sightingDate}"
        holder.txtSightingLocation.text = "Location: ${currentSighting.sightingLocation}"

        // Load image using Glide
        if (!currentSighting.imageFilePath.isNullOrBlank()) {
            Log.d("ImageFilePath", "Path: ${currentSighting.imageFilePath}")

            val uri = Uri.parse(currentSighting.imageFilePath)
            Glide.with(holder.itemView)
                .load(currentSighting.imageFilePath)
                .placeholder(R.drawable.plus)
                .error(R.drawable.info)
                .into(holder.ivBirdImage)
        } else {
            Log.d("ImageFilePath", "Image file path is null or empty")

            holder.ivBirdImage.setImageResource(R.drawable.icon)

            // holder.imageView.visibility = View.GONE
        }

        holder.playButton.setOnClickListener {
            val currentSighting = sightings[position]
            val audioFilePath = currentSighting.audioFilePath

            if (audioFilePath != null) {
                // Initialize MediaPlayer
                val mediaPlayer = MediaPlayer()

                try {
                    mediaPlayer.setDataSource(audioFilePath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("MediaPlayer", "Error setting data source", e)
                }

                // Set a listener for completion and release resources
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                }
            }
        }




    }


    override fun getItemCount(): Int {
        return sightings.size
    }
}