package com.nkmr.mygoogledrivecalendar

import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveFolder
import com.google.android.gms.drive.MetadataChangeSet

class MyGoogleDrive(application: MyApplication): MyGoogleApiBase(application) {

    var googleApi: GoogleApiClient
    init {
        googleApi = application.googleApiClient!!
        if (googleApi == null) {
            throw Exception("GoogleApi Not Valid.")
        }
        if (!canAuthorized()) {
            throw Exception("GoogleApi Not Authorized.")
        }
    }

    fun canAuthorized(): Boolean {
        Log.d("canAuthorized", "koko1")
        val optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApi)
        Log.d("canAuthorized", "koko2")
        return optionalPendingResult.isDone
    }

    fun folderExists(id: String, onSuccess: (exists: Boolean) -> Unit) {
        Log.d("folderExists", "koko1")
        val folder = Drive.DriveApi.fetchDriveId(googleApi, id)
        folder.setResultCallback { onSuccess(it.driveId != null) }
    }

    fun createRootFolder(name: String, onSuccess: (DriveFolder.DriveFolderResult) -> Unit) {
        Log.d("createFolder", "koko1")

        val changeSet = MetadataChangeSet.Builder()
            .setTitle(name).build()

        Drive.DriveApi.getRootFolder(googleApi)
            .createFolder(googleApi, changeSet)
            .setResultCallback { onSuccess(it) }
    }

    fun createFolderInFolder(name: String, parentFolderId: String, onSuccess: (DriveFolder.DriveFolderResult) -> Unit) {
        folderExists(parentFolderId) {
            if (it) {
                Drive.DriveApi.fetchDriveId(googleApi, parentFolderId)
                    .setResultCallback {
                        val changeSet = MetadataChangeSet.Builder()
                            .setTitle(name).build()
                        val parentFolder = it.driveId.asDriveFolder()
                        parentFolder.createFolder(googleApi, changeSet)
                            .setResultCallback { onSuccess(it) }
                    }
            } else {
                throw Exception("Parent folder not found.  PARENT_ID: ${parentFolderId}")
            }
        }
    }
}