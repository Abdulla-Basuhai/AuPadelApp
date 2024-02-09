package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.aupadelapp.R
import com.example.aupadelapp.databinding.FragmentProfileBinding
import com.example.aupadelapp.repositories.UserRepository

class ProfileFragment: Fragment() {

    private var _binding : FragmentProfileBinding? = null
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
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SignOutTextView.setOnClickListener {
            // navigate to the Entry Screen
            UserRepository.signOutUser()
            view.findNavController().navigate(R.id.action_profileFragment_to_entryFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}