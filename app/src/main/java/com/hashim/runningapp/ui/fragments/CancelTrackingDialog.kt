/*
 * Copyright (c) 2020/  8/ 31.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hashim.runningapp.R

class CancelTrackingDialog : DialogFragment() {

    private var hCancelListener: (() -> Unit)? = null

    fun hSetListener(listener: () -> Unit) {
        hCancelListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialogTheme
        )
            .setTitle("Cancel the run")
            .setMessage("Are you sure to cancel the current run")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("yes") { _, _ ->
                hCancelListener?.let { yes ->
                    yes()
                }
            }
            .setNegativeButton("No") { dialoginterface, _ ->
                dialoginterface.cancel()

            }
            .create()
    }
}