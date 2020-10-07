package com.github.arykoz.imagepicker.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_imageviewer.*

/**
 * Dialog to View Image
 *
 * @author Dhaval Patel
 * @version 1.6
 * @since 05 January 2019
 */
class ImageViewerDialog : DialogFragment() {

    companion object {

        private const val EXTRA_IMAGE_RESOURCE = "extra.image_resource"

        fun newInstance(resource: Int) = ImageViewerDialog().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_IMAGE_RESOURCE, resource)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_imageviewer, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        codeImg.setImageResource(arguments?.getInt(EXTRA_IMAGE_RESOURCE, 0) ?: 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }
}
