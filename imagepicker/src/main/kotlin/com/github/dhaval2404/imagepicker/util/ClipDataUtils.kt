package com.github.dhaval2404.imagepicker.util

import android.content.ClipData
import android.net.Uri


fun ClipData.forEach(callback: (uri: Uri) -> Unit) {
    for (i in 0 until this.itemCount) {
        val uri: Uri = this.getItemAt(i).uri
        callback(uri)
    }
}

fun ClipData.getUris():List<Uri> {
    val uris = mutableListOf<Uri>()
    for (i in 0 until this.itemCount) {
        uris.add(this.getItemAt(i).uri)
    }
    return uris
}