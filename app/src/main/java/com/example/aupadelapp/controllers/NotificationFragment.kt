package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.aupadelapp.databinding.FragmentNotificationBinding
import com.example.aupadelapp.databinding.FragmentProfileBinding

class NotificationFragment: Fragment() {

    private var _binding : FragmentNotificationBinding? = null
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
        _binding = FragmentNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}