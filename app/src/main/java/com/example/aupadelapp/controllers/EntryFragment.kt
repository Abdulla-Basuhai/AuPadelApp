package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aupadelapp.R
import com.example.aupadelapp.databinding.FragmentEntryBinding
import com.example.aupadelapp.databinding.FragmentHomeBinding

class EntryFragment: Fragment() {
    // view binding will generate a binding class (FragmentHomeBinding) that you can use to inflate and bind your layout (fragment_home.xml)

    private var _binding:FragmentEntryBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    // you configure the fragment instance in the onCreate function, But you don't inflate the fragment's view here.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // onCreateView function is where you create and configure the fragment's view (layout).
    // so here we will inflate and bind the home_fragment.xml
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryBinding.inflate(layoutInflater,container,false)
        return binding.root
        // once you return the root view within the binding, you are ready to start wiring up the views.

        /*
        $ This function is where you inflate (convert XML elements to View Objects) and bind the layout for the fragment's view
        and return the inflated view to the hosting activity (Main Activity).

        $ The LayoutInflater and ViewGroup parameters are necessary to inflate and bind the layout.
        $ the Bundle will contain the data that this function can use to re-create the view from a saved state.

        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)

        $ In this line of code, the FragmentHomeBinding.inflate method is used to create the user interface elements (Views)
         for a particular fragment.
        $ The layoutInflater is responsible for taking the layout described in XML and turning it into actual View objects that
          can be used in the app.
        $ The container is like a parent box that holds these Views.
        $ The false at the end means that these Views shouldn't be immediately added to this container.

        So, in simpler terms, this line is saying,
         "Hey, take the layout defined in FragmentHomeBinding (home_fragment.xml) and create the actual visual elements
         (like buttons or text) from it. Don't put them into their parent box just yet."

        After this line, these created elements are accessible through the binding variable,
        allowing you to work with and manipulate the UI of your fragment in your code.

        Lastly, the onCreateView method returns binding.root to the hosting activity.
         The binding.root represents the root view of the fragment's user interface, and by returning it,
        the hosting activity can then display and manage the UI elements defined in the fragment within its own layout.
         */
    }

    // The onViewCreated (...) lifecycle callback is invoked immediately after onCreateView(...) and it is perfect spot tp wire up your views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {

            // navigate to the Login Screen
            findNavController().navigate(R.id.action_entryFragment_to_loginFragment)
        }
        binding.signUpTextView.setOnClickListener {

            // navigate to the Registration Screen
            findNavController().navigate(R.id.action_entryFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}