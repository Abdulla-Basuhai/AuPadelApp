package com.example.aupadelapp.controllers

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aupadelapp.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment: Fragment() {
     // Using nullable _binding to handle the possibility of it being null
    private var _binding :FragmentLoginBinding? = null

    // Non-nullable binding property with custom getter to handle nullability
    /*
    * If _binding is not null: The checkNotNull function doesn't throw an exception + The getter returns the non-null value of _binding.
    * If _binding is null: The checkNotNull function throws an IllegalStateException with the specified error message.
    */
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // Inflate the layout and assign the result to _binding
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        // Return the root view of the inflated layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // At this point, the UI is created and visible. Access and manipulate UI elements using binding.
        // For example, you can perform form validation here.
        binding.loginButton.setOnClickListener {
            formValidationAndLogIn()
        }
    }

    private fun formValidationAndLogIn() {
        // Clear any previous errors
        clearErrors()

        val email = binding.emailInputLayout.editText?.text.toString().trim()
        val password = binding.passwordInputLayout.editText?.text.toString().trim()

        if (email.isEmpty()) {
            setError(binding.emailInputLayout, "Email is required!")
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
            setError(binding.passwordInputLayout, "Password is required!")
        }

        // If we reached here, that means we have passed the validation
        Toast.makeText(requireContext(),"you have passed the validation",Toast.LENGTH_SHORT).show()

    }


    private fun setError(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.error = errorMessage
    }

    private fun clearErrors() {
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
    }


    /*
    * When you navigate from this fragment to other fragment, the LoginFragment's view (layout) will disappear from
      the user's view. Because the user can navigate back to this screen, the LoginFragment is retained in memory
      so it is ready to be used when the user presses the Back button.
    *
    * BUT WHAT ABOUT ITS VIEW (FRAGMENT_LOGIN.XML)???
      Because the previous fragment (LoginFragment) is not being displayed, the system does not need to keep its
      view in memory.Fragment has a lifecycle method called onDestroyView(...).

    * onDestroyView(...) is Called when the fragment's view (layout) is no longer needed.
    * Here you have to Null out any references to views, to avoid memory leaks when the view is destroyed.
    * If you don't make the view binding null, then your view is not being freed from memory, because you are holding
      a reference to it via the binding property. The system sees that there is a chance you might access the
      view later and prevents the system from cleaning its memory.

      THIS WASTES RESOURCES, since the view is being held in memory even though it is not used - and even though
      the view will be re-created when the fragment becomes visible again.

      The system cannot free the memory associated with your old view (Login Layout) until either the view is
      re-created by calling onCreateView(..) again or the entire fragment is destroyed.

    * When the Fragment becomes visible again, its onCreateView(..) method will be called again to re-create the view.

    * THE SOLUTION IS TO NULL OUT ANY REFERENCES TO VIEWS IN THE onDestroyView(...) LIFECYCLE CALLBACK.
     */

    override fun onDestroyView() {
        super.onDestroyView()
        // make _binding null to avoid memory leaks when the view is destroyed
        _binding = null
    }
}