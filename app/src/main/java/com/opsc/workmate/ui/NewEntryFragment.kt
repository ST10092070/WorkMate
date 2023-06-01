package com.opsc.workmate.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.opsc.workmate.R
import java.util.Calendar
import java.util.Locale

class NewEntryFragment : Fragment() {

    //Variables
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnDate: Button
    private lateinit var imgEntryImage: ImageView
    private lateinit var btnUploadImg: Button

    private val calendar: Calendar = Calendar.getInstance()

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_entry, container, false)

        //Implement timepicker
        btnStartTime = view.findViewById(R.id.btnStartTimePicker)
        btnStartTime.setOnClickListener {
            showTimePickerDialog(btnStartTime)
        }

        //Implement timepicker
        btnEndTime = view.findViewById(R.id.btnEndTimePicker)
        btnEndTime.setOnClickListener {
            showTimePickerDialog(btnEndTime)
        }

        //Implement datepicker
        btnDate = view.findViewById(R.id.btnDatePicker)
        btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        //Implemente image upload
        imgEntryImage = view.findViewById(R.id.imgEntryImage)
        btnUploadImg = view.findViewById(R.id.btnUploadImg)

        btnUploadImg.setOnClickListener {
            checkPermissionsAndOpenImagePicker()
        }

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
            imgEntryImage.setImageURI(imageUri)
        }
    }

    private fun showTimePickerDialog(button: Button) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                // Update button text with the selected time
                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
                button.text = formattedTime
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                // Update button text with the selected date
                val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)
                btnDate.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

}