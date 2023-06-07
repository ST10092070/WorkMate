package com.opsc.workmate.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Image
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorWheelDialog
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream


class CreateCategoryFragment : Fragment(), SimpleDialog.OnDialogResultListener {

    //Variables
    private lateinit var btnChooseColour: Button
    private lateinit var btnCreate: Button
    private lateinit var imgCategoryImage: ImageView
    private lateinit var btnUploadImg: Button
    private lateinit var txtCategoryName: EditText
    @ColorInt var colour = 0


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
        btnCreate = view.findViewById(R.id.btnCreateCategory)
        imgCategoryImage = view.findViewById(R.id.imgCategoryImage)
        btnUploadImg = view.findViewById(R.id.btnUploadImgCategory)
        txtCategoryName = view.findViewById(R.id.txtCategoryName)

        //Implement colour picker
        btnChooseColour.setOnClickListener {
            showColorPickerDialog()
        }

        // Handle image upload button click
        btnUploadImg.setOnClickListener {
            ImagePicker.with(this)
                .crop()                     //crop image(optional), check customization for more options
                .compress(1024)             //final image size will be less than 1 MB
                .maxResultSize(1080,1080)   //final image resolution will be less than 1080 x 1080
                .start()
        }

        //Implement create category button
        btnCreate.setOnClickListener {
            var isCreated : Boolean = createCategory()
            if (isCreated) {
                //Navigate to dashboard if successful
                // Get the NavController
                val navController = Navigation.findNavController(view)
                // Navigate to the relevant fragment
                navController.navigate(R.id.action_createCategoryFragment_to_dashboardFragment)
            }
        }

        return view
    }

    private fun createCategory(): Boolean {
        //Variables
        var name : String
        var imageData : String

        //Get data from page
        name = txtCategoryName.text.toString()

        imageData = convertImageToBase64(imgCategoryImage).toString()

        if (name == "") {
            Toast.makeText(activity, "Enter a name", Toast.LENGTH_SHORT).show()
            return false
        }

        val category = Category(
            Global.currentUser?.username ?: "",
            name,
            colour,
            imageData
        )
        Global.categories.add(category)
        Toast.makeText(activity, "Category Created!", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun convertImageToBase64(imageView: ImageView): String? {
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
        return null
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            imgCategoryImage.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showColorPickerDialog() {

        //Code Attribution
        //The below code has been derived from GitHub
        //https://github.com/eltos/SimpleDialogFragments/wiki/SimpleColorWheelDialog
        //Eltos
        //https://github.com/eltos

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
                colour = extras.getInt(SimpleColorWheelDialog.COLOR)
                return true // Dialog result was handled
            }
        }
        return false
    }
}