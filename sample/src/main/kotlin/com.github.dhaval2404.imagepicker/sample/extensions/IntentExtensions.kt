package com.github.dhaval2404.imagepicker.sample.extensions

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.github.dhaval2404.imagepicker.ImagePicker

fun ActivityResult.runWithUri(context: Context, block: (Uri) -> Unit) {
    when (resultCode) {
        Activity.RESULT_OK -> {
            // Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!

            block(fileUri)
        }
        ImagePicker.RESULT_ERROR -> {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
        else -> {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}