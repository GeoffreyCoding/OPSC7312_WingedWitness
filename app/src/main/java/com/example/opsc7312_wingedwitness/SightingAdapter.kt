package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.IOException
//---------------------------------------------------------------------------------------------------------------------//


class SightingAdapter(private val sightings: List<SightingData>) :
    RecyclerView.Adapter<SightingAdapter.SightingViewHolder>() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Inner Class
    inner class SightingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //-----------------------------------------------------------------------------------------------------------------//
        //Declarations
        val txtSightingName: TextView = itemView.findViewById(R.id.txtSightingName)
        val txtSightingSpecies: TextView = itemView.findViewById(R.id.txtSightingSpecies)
        val txtSightingDate: TextView = itemView.findViewById(R.id.txtSightingDate)
        val txtSightingLocation: TextView = itemView.findViewById(R.id.txtSightingLocation)
        val ivBirdImage: ImageView = itemView.findViewById(R.id.ivBirdImage)
        val playButton: ImageButton = itemView.findViewById(R.id.play)
        val infoButton : ImageButton = itemView.findViewById(R.id.info)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //On View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sighting_item, parent, false)
        return SightingViewHolder(view)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Binding
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
        }
        else {
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

        holder.infoButton.setOnClickListener{
            var url = "https://en.wikipedia.org/wiki/"
            url += currentSighting.sightingSpecies
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            holder.itemView.context.startActivity(intent)
        }
    }


    //-----------------------------------------------------------------------------------------------------------------//
    //Get Count
    override fun getItemCount(): Int {
        return sightings.size
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//