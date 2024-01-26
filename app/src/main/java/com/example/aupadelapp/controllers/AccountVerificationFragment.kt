package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.aupadelapp.databinding.FragmentAccountVerificationBinding

class AccountVerificationFragment: Fragment() {
    private val args:AccountVerificationFragmentArgs by navArgs()
    private var _binding:FragmentAccountVerificationBinding? = null
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
        _binding = FragmentAccountVerificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageTextView.text = "We have sent a link to ${args.email}, please check your email for next steps.\n"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}