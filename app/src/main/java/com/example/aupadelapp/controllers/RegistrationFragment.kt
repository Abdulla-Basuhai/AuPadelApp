package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aupadelapp.databinding.FragmentRegistrationBinding

class RegistrationFragment: Fragment(){
    //private lateinit var binding:FragmentRegistrationBinding
    private var _binding:FragmentRegistrationBinding? = null
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
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            // validate the form
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}