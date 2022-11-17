package com.github.dhaval2404.imagepicker.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Permission utility class
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
object PermissionUtil {

    /**
     * Check if Permission is granted
     *
     * @return true if specified permission is granted
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(context, permission)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if Specified Permissions are granted or not. If single permission is denied then
     * function will return false.
     *
     * @param context Application Context
     * @param permissions Array of Permission to Check
     *
     * @return true if all specified permission is granted
     */
    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.filter {
            isPermissionGranted(context, it)
        }.size == permissions.size
    }

    /**
     * Check if Specified Permission is defined in AndroidManifest.xml file or not.
     * If permission is defined in manifest then return true else return false.
     *
     * @param context Application Context
     * @param permission String Permission Name
     *
     * @return true if permission defined in AndroidManifest.xml file, else return false.
     */
    fun isPermissionInManifest(context: Context, permission: String): Boolean {
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val permissions = packageInfo.requestedPermissions

        if (permissions.isNullOrEmpty())
            return false

        for (perm in permissions) {
            if (perm == permission)
                return true
        }

        return false
    }
}
