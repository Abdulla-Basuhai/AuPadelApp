package com.example.aupadelapp.controllers

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aupadelapp.databinding.FragmentRegistrationBinding
import com.example.aupadelapp.model.User
import com.example.aupadelapp.repositories.UserRepository
import com.google.android.material.textfield.TextInputLayout

class RegistrationFragment: Fragment(){
    //private late init var binding:FragmentRegistrationBinding
    private var _binding:FragmentRegistrationBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var selectedGender: String? = null
    private var selectedRole: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.selectGenderBtn.setOnClickListener {
            showGenderSelectionDialog()
        }

        binding.selectRoleBtn.setOnClickListener {
            showRoleSelectionDialog()
        }


        binding.registerButton.setOnClickListener {
            // validate the form
            validateAndRegisterUser()
        }
    }

    private fun showRoleSelectionDialog() {
        // Define available role options
        val roleOptions = arrayOf("Student", "Faculty", "Staff")

        // Determine the index of the currently selected role (if any)
        val selectRoleIndex = if (selectedRole != null) {
            roleOptions.indexOf(selectedRole)
        } else {
            -1
        }

        // Build and show the AlertDialog for role selection
        AlertDialog.Builder(requireContext())
            .setTitle("Select Role")
            // Set up a single-choice list for role options
            .setSingleChoiceItems(roleOptions, selectRoleIndex) { _, which ->
                // Update selectedRole when an option is chosen
                selectedRole = roleOptions[which]
            }
            // Handle positive button click (Ok)
            .setPositiveButton("Ok") { dialog, _ ->
                // Update UI with the selected role, dismiss the dialog, and hide error message
                binding.selectRoleBtn.text = selectedRole
                dialog.dismiss()
                binding.selectRoleError.visibility = View.GONE
            }
            // Handle negative button click (Cancel)
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showGenderSelectionDialog() {
            // Define available gender options
        val genderOptions = arrayOf("Male","Female")
            // Determine the index of the currently selected role (if any)
        val selectGenderIndex = if (selectedGender!= null){
            genderOptions.indexOf(selectedGender)
        }
        else{
            -1
        }
            // Build and show the AlertDialog for role selection
        AlertDialog.Builder(requireContext())
            .setTitle("Select Gender")
                // Set up a single-choice list for role options
            .setSingleChoiceItems(genderOptions,selectGenderIndex){_, which ->
                    // Update selectedRole when an option is chosen
                selectedRole = genderOptions[which]
            }
                // Handle positive button click (Ok)
            .setPositiveButton("OK"){dialog,_ ->
                    // Update UI with the selected role, dismiss the dialog, and hide error message
                binding.selectGenderBtn.text = selectedRole
                dialog.dismiss()
                binding.selectGenderError.visibility = View.GONE
            }
        // Handle negative button click (Cancel)
            .setNegativeButton("Cancel",null)
            .show()
    }

    private fun validateAndRegisterUser() {
          // Clear any previous errors
        clearErrors()

        val email = binding.emailInputLayout.editText?.text.toString().trim()
        val password = binding.passwordInputLayout.editText?.text.toString().trim()
        val name = binding.userNameInputLayout.editText?.text.toString().trim()
        val phoneNumber = binding.PhoneNumberInputLayout.editText?.text.toString().trim()
        val gender = binding.selectGenderBtn.text.toString().trim()
        val role = binding.selectRoleBtn.text.toString().trim()

        if (name.isEmpty()) {
            setError(binding.userNameInputLayout, "User Name is required !")
            return
        }
        if (email.isEmpty()) {
            setError(binding.emailInputLayout, "Email is required !")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(binding.emailInputLayout, "Invalid email format")
            return
        }
        if (password.length < 6) {
            setError(binding.passwordInputLayout, "Password must be at least 6 characters")
            return
        }
        if (password.isEmpty()) {
            setError(binding.passwordInputLayout, "Password is required !")
        }

        if (phoneNumber.isEmpty() || !phoneNumber.isDigitsOnly()) {
            setError(
                binding.PhoneNumberInputLayout,
                "Phone Number is required and must contain digits only !"
            )
            return
        }
        if (gender.isEmpty() || gender == "Select Gender") {
            // Show the error message
            binding.selectGenderError.visibility = View.VISIBLE
            return
        }
        if (role.isEmpty() || role == "Select Role") {
            // Show the error message
            binding.selectRoleError.visibility = View.VISIBLE
            return
        }
        // if we reached here, that's mean we have passed the validation
        // show progress bar
        // Create a new user account & send verification link to the registered email
        val user = User("",name,email,password,role,phoneNumber,gender)
        //The below callback is a piece of code that gets executed when the registration process is complete.
        Log.d("registerNewUser","before call the registerNewUser function")

        UserRepository.registerNewUser(user){ isSuccess, message ->
            if (isSuccess){
                // Registration was successful
                Log.d("registerNewUser","callback called after Registration was successful")
                // navigate the user to the Account Verification Screen
                val action = RegistrationFragmentDirections.actionRegistrationFragmentToAccountVerificationFragment(email)
                findNavController().navigate(action)
            }
            else{
                Log.d("registerNewUser","callback called after Registration failed")
                Toast.makeText(requireContext(), "Registration failed: $message", Toast.LENGTH_LONG).show()
            }
        }

        /*
        In the context of the above code, the callback is initiated when the registerNewUser function of the UserRepository
        is called. The registerNewUser function performs an asynchronous operation, such as registering a new user with
        Firebase Authentication, and it takes some time to complete (Network Request).

        The callback itself is provided as a parameter to the registerNewUser function.
        The callback will be invoked (or executed) when the asynchronous operation inside registerNewUser completes,
        either successfully or with an error.In other words, the callback starts when the asynchronous task
        (user registration) finishes,and it is invoked with the result of that task.

          ## If the registration is successful, the callback is called with true and an empty message.
          ## If there is an error during registration, the callback is called with false and an error message.

          The code inside the callback then determines what actions to take based on the success or failure of the registration
           process.
         */

    }

    private fun setError(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.error = errorMessage
    }

    private fun clearErrors() {
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.PhoneNumberInputLayout.error = null
        binding.userNameInputLayout.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}