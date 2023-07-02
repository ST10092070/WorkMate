package com.opsc.workmate.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import com.opsc.workmate.R
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Image
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.opsc.workmate.data.DataManager
import java.util.Calendar
import java.util.Locale

class NewEntryFragment : Fragment() {

    //Variables
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnDate: Button
    private lateinit var txtDescription: EditText
    private lateinit var imgEntryImage: ImageView
    private lateinit var btnUploadImg: FloatingActionButton
    private lateinit var btnCreate: Button
    private lateinit var btnCategoryPicker: Button
    private lateinit var categoryNames: List<String>


    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1 // Constant for image picker request code
        private const val PERMISSION_REQUEST_CODE = 101 // Constant for permission request code
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_entry, container, false)

        btnStartTime = view.findViewById(R.id.btnStartTimePicker)
        btnEndTime = view.findViewById(R.id.btnEndTimePicker)
        btnDate = view.findViewById(R.id.btnDatePicker)
        txtDescription = view.findViewById(R.id.txtDescription)
        imgEntryImage = view.findViewById(R.id.imgEntryImage)
        btnUploadImg = view.findViewById(R.id.btnUploadEntryImg)
        btnCreate = view.findViewById(R.id.btnCreate)

        btnStartTime.setOnClickListener {
            showTimePickerDialog(btnStartTime)
        }

        btnEndTime.setOnClickListener {
            showTimePickerDialog(btnEndTime)
        }

        btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnUploadImg.setOnClickListener {
            ImagePicker.with(this)
                .crop()                     //crop image(optional), check customization for more options
                .compress(1024)             //final image size will be less than 1 MB
                .maxResultSize(1080,1080)   //final image resolution will be less than 1080 x 1080
                .start()
        }

        btnCreate.setOnClickListener {
            addEntry()
        }

        btnCategoryPicker = view.findViewById(R.id.btnCategoryPicker)
        categoryNames = Global.categories.map { it.name } // Retrieve category names from Global.categories

        btnCategoryPicker.setOnClickListener {
            showCategoryPickerDialog()
        }
        return view
    }

    private fun showCategoryPickerDialog() {

        //Code Attribution
        //The below code was derived from StackOverflow
        //https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        //David Hedlund
        //https://stackoverflow.com/users/133802/david-hedlund

        val categoryArray = categoryNames.toTypedArray()
        val selectedCategoryIndex = categoryArray.indexOf(btnCategoryPicker.text.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Select Category")
            .setSingleChoiceItems(categoryArray, selectedCategoryIndex) { dialog, which ->
                btnCategoryPicker.text = categoryArray[which]
                dialog.dismiss()
            }
            .show()
    }


    private fun addEntry() {
        val startTime = btnStartTime.text.toString()
        val endTime = btnEndTime.text.toString()
        val date = btnDate.text.toString()
        val description = txtDescription.text.toString()
        val categoryName = btnCategoryPicker.text.toString()
        val uid = Global.currentUser!!.uid.toString()
        val imageData = Image.convertImageToBase64(imgEntryImage).toString()

        if (startTime.isNotEmpty() && endTime.isNotEmpty() && date != "dd/mm/yyyy" && categoryName.lowercase() != "category") {
            val entry = Entry(
                uid,
                categoryName,
                date,
                startTime,
                endTime,
                imageData,
                description
            )

            DataManager.addEntry(entry) { success ->
                if (success) {
                    //Update local entries list
                    DataManager.getEntries(Global.currentUser!!.uid.toString()) { entries ->
                        Global.entries = entries
                        Toast.makeText(requireContext(), "Entry added successfully!", Toast.LENGTH_SHORT).show()

                        //rewarding the user with 2 work coins for creating a new entry
                        val topup = Global.currentUser!!.workcoins!! + 2
                        DataManager.setWorkcoins(topup) { isSuccess ->
                            if (isSuccess){
                                Toast.makeText(activity, "You've been rewarded with 2 Work Coins!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        
                        //Navigate to dashboard if successful
                        // Get the NavController
                        val navController = Navigation.findNavController(requireView())
                        // Navigate to the relevant fragment
                        navController.navigate(R.id.action_newEntryFragment_to_dashboardFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            // Use Uri object instead of File to avoid storage permissions
            imgEntryImage.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showTimePickerDialog(button: Button) {

        //Code Attribution
        //The below code was derived from StackOverflow
        //https://stackoverflow.com/questions/47498014/android-kotlin-dialogfragment-timepicker
        //flokol120
        //https://stackoverflow.com/users/7276394/flokol120

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
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                btnDate.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

}