package com.github.arykoz.imagepicker.provider

import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Toast
import com.github.arykoz.imagepicker.ImagePickerActivity

/**
 * Abstract Provider class
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
abstract class BaseProvider(protected val activity: ImagePickerActivity) : ContextWrapper(activity) {

    /**
     * Cancel operation and Set Error Message
     *
     * @param error Error Message
     */
    protected fun setError(error: String) {
        onFailure()
        activity.setError(error)
    }

    /**
     * Cancel operation and Set Error Message
     *
     * @param errorRes Error Message
     */
    protected fun setError(errorRes: Int) {
        setError(getString(errorRes))
    }

    /**
     * Show Short Toast Message
     *
     * @param messageRes String message resource
     */
    protected fun showToast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    /**
     * Call this method when task is cancel in between the operation.
     * E.g. user hit back-press
     */
    protected fun setResultCancel() {
        onFailure()
        activity.setResultCancel()
    }

    /**
     * This method will be Call on Error, It can be used for clean up Tasks
     */
    protected open fun onFailure() {
    }

    /**
     * Save all appropriate provider state.
     */
    open fun onSaveInstanceState(outState: Bundle) {
    }

    /**
     * Restores the saved state for all Providers.
     *
     * @param savedInstanceState the Bundle returned by {@link #onSaveInstanceState()}
     * @see #onSaveInstanceState()
     */
    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    }
}
