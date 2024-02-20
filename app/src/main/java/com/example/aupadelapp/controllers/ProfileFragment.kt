package com.example.aupadelapp.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.aupadelapp.R
import com.example.aupadelapp.databinding.FragmentProfileBinding
import com.example.aupadelapp.model.User
import com.example.aupadelapp.repositories.UserRepository


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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SignOutBtn.setOnClickListener {
            // navigate to the Entry Screen
            UserRepository.signOutUser()
            view.findNavController().navigate(R.id.action_profileFragment_to_entryFragment)
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
                    // Callback function body
                    /*
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
        Log.i("ProfileFragment","Override onSavedInstance")
        // Save any relevant data to the savedInstanceState bundle
        outState.putSerializable(ARG_USER,cachedUserData)
    }
    /*
    When a fragment is placed in the back stack and then the user navigates back to it, the onSaveInstanceState(Bundle)
    method is not typically called. The reason for this is that placing a fragment in the back stack does not mean
    the fragment is being destroyed. Instead, it's being stopped or paused, but it retains its instance in memory.
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}