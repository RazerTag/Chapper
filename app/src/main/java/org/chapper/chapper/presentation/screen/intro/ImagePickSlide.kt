package org.chapper.chapper.presentation.screen.intro

import agency.tango.materialintroscreen.SlideFragment
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mvc.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import org.chapper.chapper.R
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.repository.SettingsRepository
import kotlin.properties.Delegates


class ImagePickSlide : SlideFragment() {
    private var mPhoto: CircleImageView by Delegates.notNull()
    private var mPickButton: Button by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image_pick_slide, container, false)

        mPhoto = view.findViewById(R.id.selectedPhotoImageView)
        mPickButton = view.findViewById(R.id.pickButton)

        mPickButton.setOnClickListener {
            pick()
        }

        return view
    }

    override fun backgroundColor(): Int = R.color.colorPrimary

    override fun buttonsColor(): Int = R.color.colorAccent

    override fun onPause() {
        super.onPause()

        saveData()
    }

    private fun saveData() {
        SettingsRepository.setFirstStart(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            val bitmap: Bitmap = ImagePicker.getImageFromResult(activity.applicationContext, requestCode, resultCode, data)!!

            mPhoto.setImageBitmap(bitmap)
            SettingsRepository.setProfilePhoto(activity.applicationContext, bitmap)
        }
    }

    private fun pick() {
        val permissionCheck = ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.WRITE_EXTERNAL_STORAGE)
            return
        }

        ImagePicker.pickImage(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pick()
                }
            }
        }
    }
}