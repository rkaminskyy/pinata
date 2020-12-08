package com.example.pinatafarm.domain.provider

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class SimpleFaceProvider(private val resources: Resources) {

    private val detector = FaceDetection.getClient()

    suspend fun provideForImage(imagePath: String?): List<Face> {
        val image = convertToBitmap(imagePath)
        image ?: return emptyList()

        val inputImage = InputImage.fromBitmap(image, 0)
        return detector.process(inputImage).await()
    }

    private fun convertToBitmap(img: String?): Bitmap? {
        return try {
            val stream = resources.assets.open("img/$img")
            BitmapFactory.decodeStream(stream)
        } catch (e: Exception) {
            Log.e("Error", e.toString())
            null
        }
    }
}

suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener {
        val e = exception
        when {
            e != null -> cont.resumeWithException(e)
            isCanceled -> cont.cancel()
            else -> cont.resume(it.result!!, {})
        }
    }
}