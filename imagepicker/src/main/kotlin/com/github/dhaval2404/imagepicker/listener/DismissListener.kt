package com.github.dhaval2404.imagepicker.listener

/**
 * Interface used to allow the creator of a dialog to run some code when the
 * dialog is dismissed.
 *
 * @author Dhaval Patel
 * @version 1.8
 * @since 19 December 2020
 */
interface DismissListener {

    /**
     * This method will be invoked when the dialog is dismissed.
     */
    fun onDismiss()
}
