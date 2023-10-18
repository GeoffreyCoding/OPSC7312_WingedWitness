package com.example.opsc7312_wingedwitness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.model.LatLng
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class HotSpotHelper {

    ///------------------------------------------------------------------------------------------///


    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    ///------------------------------------------------------------------------------------------///


    fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    ///------------------------------------------------------------------------------------------///


    fun addBirdPointAnnotation(mapView: MapView, bird: ToolBox.Birds, onBirdClick: (LatLng) -> Unit) {
        bitmapFromDrawableRes(mapView.context, R.drawable.baseline_location_on_24)?.let { bitmap ->
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()

            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withIconSize(1.5)
                .withTextField(bird.name)
                .withTextSize(0.0)
                .withPoint(Point.fromLngLat(bird.longitude.toDouble(), bird.latitude.toDouble()))
                .withIconImage(bitmap)

            pointAnnotationManager.create(pointAnnotationOptions)
            pointAnnotationManager.addClickListener { pointAnnotation ->
                val markerCoordinates = LatLng(
                    pointAnnotation.geometry.latitude(),
                    pointAnnotation.geometry.longitude()
                )

                onBirdClick(markerCoordinates)

                true
            }
        }
    }

    ///------------------------------------------------------------------------------------------///
}