package com.example.aupadelapp.controllers

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.aupadelapp.R
import com.example.aupadelapp.databinding.FragmentProfileBinding
import com.example.aupadelapp.model.User
import com.example.aupadelapp.repositories.UserRepository
import com.google.android.material.textfield.TextInputLayout


//In Kotlin, you can define top-level constants outside of any class or function.
private const val ARG_USER = "user"

class ProfileFragment: Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }
    private var cachedUserData: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("ProfileFragment","onCreate is called")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("ProfileFragment","onCreateView is called")
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ProfileFragment","onViewCreated is called")


        binding.SignOutBtn.setOnClickListener {
            // navigate to the Entry Screen
            UserRepository.signOutUser()
            view.findNavController().navigate(R.id.action_profileFragment_to_entryFragment)
        }
        binding.UpdateProfileButton.setOnClickListener {
            // open dialog form
            showUpdateProfileBottomSheet(cachedUserData)
            // if it is valid, create a new User Object
            // get the user id and append it with the user object
            // pass the user object as parameter the update function

        }

        // If savedInstanceState is not null, it means the fragment is being recreated (e.g., due to a configuration change).

        if (savedInstanceState != null){
            // The fragment is being recreated, and savedInstanceState contains the saved state.
            cachedUserData = savedInstanceState.getSerializable(ARG_USER) as User
            updateUI(cachedUserData!!)
            Log.i("ProfileFragment", "retrieve the data from savedInstanceState")
        }
        else{
            // The fragment is being created for the first time (no saved state).
            // get the current user
            val currentUser = UserRepository.getCurrentUser()
            // fetch the user data from the database
            if (currentUser!= null){
                UserRepository.getUserData(currentUser.uid){user ->
                    /*
                    ## Callback function body
                    Since fetching data from the database can take time,
                    the callback function allows you to handle the result (user data or error) once the database operation is complete.
                     */
                    if (user!= null){
                        cachedUserData = user
                        updateUI(cachedUserData!!)
                        Log.i("ProfileFragment", "callback function body user is not null ${user.userName}")
                    }
                    else{
                        // handle errors (no network connection, user not authenticated ...)
                        Log.i("ProfileFragment", "callback function body user is null")
                        Toast.makeText(requireActivity(),"Something Wrong !!",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun updateUI(user:User){
        binding.textViewUserName.text = user.userName
        binding.textViewEmail.text = user.email
        binding.textViewGender.text = user.gender
        binding.textViewRole.text = user.role
        binding.textViewPhoneNumber.text = user.phoneNumber
    }
    private fun showUpdateProfileBottomSheet(user:User?) {

        /*
        Steps to showUpdateProfileBottomSheet:
           1. create a dialog instance
           2. create a layout with drawable background
           3. set the content view of the dialog to the layout defined
           4. manipulate the views in the layout
           5. make the dialog visible
           6. specify the width and height of the dialog
           7. set the background of the dialog window to be transparent
           8. apply a custom animation style
              8.1. create a style in the theme file to define custom window animations for entering and exiting a dialog
              8.2. connect android:windowEnterAnimation attribute to an animation resource (@anim/slide_in_bottom)
              8.3. connect android:windowExitAnimation attribute to an animation resource (@anim/slide_out_bottom)
              8.4. connect the style with the dialog instance
           9. position the dialog at the bottom of the screen.
         */

        val dialog = Dialog(requireContext()) // create a dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) //The dialog is configured to have no title bar
        dialog.setContentView(R.layout.bottom_sheet_update_profile) //the content view of the dialog is set to a layout defined

        // Find views and set initial values from cachedUserData or Firebase
        val emailInputLayout = dialog.findViewById<TextInputLayout>(R.id.emailInputLayout)
        val userNameInputLayout = dialog.findViewById<TextInputLayout>(R.id.userNameInputLayout)
        val phoneNumberInputLayout =
            dialog.findViewById<TextInputLayout>(R.id.PhoneNumberInputLayout)
        val selectRoleButton = dialog.findViewById<Button>(R.id.selectRoleBtn)
        val selectGenderButton = dialog.findViewById<Button>(R.id.selectGenderBtn)
        val updateProfileButton = dialog.findViewById<Button>(R.id.updateProfileButton)
        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)

        if (user == null) {
            Log.i("ProfileFragment", "showUpdateProfileBottomSheet called: User is Null")
            val currentUser = UserRepository.getCurrentUser()
            if (currentUser != null) {
                UserRepository.getUserData(currentUser.uid) {userData ->
                    if (userData != null) {
                        emailInputLayout.editText?.setText(userData.email)
                        userNameInputLayout.editText?.setText(userData.userName)
                        phoneNumberInputLayout.editText?.setText(userData.phoneNumber)
                        selectGenderButton.text = userData.gender
                        selectRoleButton.text = userData.role
                    }
                }
            }
        }
        else{
            Log.i("ProfileFragment", "showUpdateProfileBottomSheet called: User is Not Null")
            emailInputLayout.editText?.setText(user.email)
            userNameInputLayout.editText?.setText(user.userName)
            phoneNumberInputLayout.editText?.setText(user.phoneNumber)
            selectGenderButton.text = user.gender
            selectRoleButton.text = user.role
        }
        closeIcon.setOnClickListener{
            dialog.dismiss()
        }
        selectRoleButton.setOnClickListener {
            val roleOptions = arrayOf("Student", "Faculty", "Staff")
            val checkedItem = roleOptions.indexOf(selectRoleButton.text.toString())

            AlertDialog.Builder(requireContext())
                .setTitle("Select Role")
                .setSingleChoiceItems(roleOptions,checkedItem){_,index ->
                    // Update selectedRole when an option is chosen
                    selectRoleButton.text = roleOptions[index]
                }
                .setPositiveButton("Ok"){dialog,_ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog,_ ->
                 dialog.dismiss()
                }
                .show()
        }

        selectGenderButton.setOnClickListener {
            val genderOptions = arrayOf("Male","Female")
            val checkedItem = genderOptions.indexOf(selectRoleButton.text.toString())

            AlertDialog.Builder(requireContext())
                .setTitle("Select Gender")
                .setSingleChoiceItems(genderOptions,checkedItem){_,index ->
                    // Update selectedRole when an option is chosen
                    selectGenderButton.text = genderOptions[index]
                }
                .setPositiveButton("Ok"){dialog,_ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Handle the "Update Profile" button click
        updateProfileButton.setOnClickListener {
            // Implement logic to update the user's profile with the selected data
            val newUserName = userNameInputLayout.editText?.text.toString()
            val newPhoneNumber = phoneNumberInputLayout.editText?.text.toString()

            // Check if any changes were made
            if (
                newUserName == cachedUserData?.userName &&
                newPhoneNumber == cachedUserData?.phoneNumber &&
                selectRoleButton.text == cachedUserData?.role &&
                selectGenderButton.text == cachedUserData?.gender
            )
            {
                // No changes were made
                Toast.makeText(context, "No changes made to profile", Toast.LENGTH_LONG).show()
                dialog.dismiss()
                //break out of the click listener logic
                return@setOnClickListener
            }
            // check the validation of the changes

            if (newUserName.isEmpty()) {
                Toast.makeText(context, "User Name is required !", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (newPhoneNumber.isEmpty() || !newPhoneNumber.isDigitsOnly()) {
                Toast.makeText(
                    context,
                    "Phone Number is required and must contain digits only !",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }


            // passed the validation, proceed with updating the user's profile
            val currentUser = UserRepository.getCurrentUser()
            val updatedUser: User
            if (currentUser!= null){
                   updatedUser = User(
                    currentUser.uid,
                    newUserName,
                    emailInputLayout.editText?.text.toString(),
                    selectRoleButton.text.toString(),
                    newPhoneNumber,
                    selectGenderButton.text.toString(),
                )
            }
            else {
                Toast.makeText(
                    context,
                    "User is not Logged In !",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            UserRepository.updateUserProfile(updatedUser){  success, message ->
                if (success){
                    Toast.makeText(context, "User data updated successfully !", Toast.LENGTH_LONG).show()
                    updateUI(updatedUser)
                }
                else{
                    Toast.makeText(context, "User data update failed: $message !", Toast.LENGTH_LONG).show()
                }
                // Dismiss the bottom sheet
                dialog.dismiss()
            }
        }

        dialog.show() //used to make the dialog visible
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ) //used to specify the width and height of the dialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //used to set the background of the dialog window to be transparent
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation //apply a custom animation style (R.style.DialogAnimation) to the dialog.
        dialog.window?.setGravity(Gravity.BOTTOM) //used to position the dialog at the bottom of the screen.

    }
    /*
    In Android, the onSaveInstanceState(Bundle) method is called when the hosting activity is being destroyed or
     going through a configuration change (such as a screen rotation).

     The purpose of this method is to allow the activity to save its current state, including the state of
     its attached fragments, so that it can be restored later.

     The data you place in the bundle is retained through configuration changes and process death and recreation
      and is available in your fragment's onCreate(Bundle), onCreateView(LayoutInflater, ViewGroup, Bundle),
      and onViewCreated(View, Bundle) methods.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("ProfileFragment","onSavedInstance is called")
        // Save any relevant data to the savedInstanceState bundle
        outState.putSerializable(ARG_USER,cachedUserData)
    }
    /*
    When a fragment is placed in the back stack and then the user navigates back to it, the onSaveInstanceState(Bundle)
    method is not typically called. The reason for this is that placing a fragment in the back stack does not mean
    the fragment is being destroyed. Instead, it's being stopped or paused, but it retains its instance in memory.
     */

    override fun onStart() {
        super.onStart()
        Log.i("ProfileFragment","onStart is called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("ProfileFragment","onResume is called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ProfileFragment","onPause is called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ProfileFragment","onStop is called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("ProfileFragment","onDestroyView is called")
        _binding = null
    }

    /*
       ### Navigating to the Profile Fragment:
       1. `onCreate(Bundle?)`, `onCreateView(...)`, `onCreatedView(...)` are called.
            - Move the fragment from the Non-Existent/Destroyed State to the Created State.
       2. `onStart()` and `onResume()` are called.
            - Move the fragment to the Started and Resumed states respectively.

      ### Pressing Home Screen Button:
       - `onPause()`, `onStop()`, and `onSavedInstance()` are called.
               - Note: `onDestroyView()` is not called.
       - Returning to the Fragment:
               - `onStart()` and `onResume()` are called.

     ### Pressing Overview Button (Recent Activities and Apps):
       - `onPause()`, `onStop()`, and `onSavedInstance()` are called.

    ### Navigating to Another Fragment:
     1. `onPause()`, `onStop()`, `onDestroyView()` are called.
            - Before `onDestroyView()`, `onSavedInstance()` is called to save the data.
     2. Returning to the Fragment:
            - `onCreate`, `onCreate(Bundle?)`, `onCreateView(...)`, `onCreatedView(...)` are called.
            - Retrieve data from the bundle in the `savedInstanceState`.
            - `onResume()` and `onPause()` are called.
     */


}