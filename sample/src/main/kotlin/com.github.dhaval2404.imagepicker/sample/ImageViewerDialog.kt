package com.github.dhaval2404.imagepicker.sample

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.dhaval2404.imagepicker.sample.databinding.DialogImageviewerBinding

/**
 * Dialog to View Image
 *
 * @author Dhaval Patel
 * @version 1.6
 * @since 05 January 2019
 */
class ImageViewerDialog : DialogFragment(R.layout.dialog_imageviewer) {

    companion object {

        private const val EXTRA_IMAGE_RESOURCE = "extra.image_resource"

        @JvmStatic
        fun newInstance(resource: Int) = ImageViewerDialog().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_IMAGE_RESOURCE, resource)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogImageviewerBinding.bind(view)
        binding.codeImg.setImageResource(arguments?.getInt(EXTRA_IMAGE_RESOURCE, 0) ?: 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}
