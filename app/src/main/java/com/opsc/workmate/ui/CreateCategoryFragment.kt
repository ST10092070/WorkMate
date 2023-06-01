package com.opsc.workmate.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.opsc.workmate.R
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorWheelDialog


class CreateCategoryFragment : Fragment(), SimpleDialog.OnDialogResultListener {

    //Variables
    private lateinit var btnChooseColour: Button
    private lateinit var btnCreate: Button
    private lateinit var imgCategoryImage: ImageView
    private lateinit var btnUploadImg: Button
    private var selectedColor: Int = Color.WHITE

    companion object {
        private const val REQUEST_IMAGE_PICKER = 100 // Constant for image picker request code
        private const val PERMISSION_REQUEST_CODE = 101 // Constant for permission request code
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_category, container, false)

        //Initialise variables
        btnChooseColour = view.findViewById(R.id.btnChooseColour)
        btnCreate = view.findViewById(R.id.btnCreate)
        imgCategoryImage = view.findViewById(R.id.imgCategoryImage)
        btnUploadImg = view.findViewById(R.id.btnUploadImg)

        //Implement colour picker
        btnChooseColour.setOnClickListener {
            showColorPickerDialog()
        }

        // Handle image upload button click
        btnUploadImg.setOnClickListener {
            checkPermissionsAndOpenImagePicker()
        }

        //rest goes here

        return view

    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            }
        }
    }

    // Check and request necessary permissions for image upload
    private fun checkPermissionsAndOpenImagePicker() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission),
                PERMISSION_REQUEST_CODE
            )
        } else {
            openImagePicker()
        }
    }

    // Open the image picker (gallery)
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imgCategoryImage.setImageURI(imageUri)
        }
    }


    private fun showColorPickerDialog() {
        SimpleColorWheelDialog.build()
            .color(0x80e9a11d.toInt())
            .alpha(false)
            .hideHexInput(true)
            .show(this, "Tag");
    }

    //handle colour picker results
    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (dialogTag == "Tag") {
            val color = extras?.getInt(SimpleColorWheelDialog.COLOR, Color.WHITE)
            if (which == -1 && color != null) {
                // Handle the selected color
                selectedColor = color
                btnChooseColour.setBackgroundColor(selectedColor)
                return true // Dialog result was handled
            }
        }
        return false
    }
}