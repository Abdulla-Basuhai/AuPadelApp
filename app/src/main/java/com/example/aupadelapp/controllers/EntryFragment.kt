package com.example.aupadelapp.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import com.example.aupadelapp.R
import com.example.aupadelapp.databinding.FragmentEntryBinding

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
        // Override the back navigation behavior for EntryFragment (to avoid go back to the home fragment(start destination))
        // sets up a custom action (closing the app) to be performed when the back button is pressed.
        val callback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed(){
            //finishes the current activity (MainActivity)
            requireActivity().finish()
        }
            /*
           Here, a new instance (callback) of an anonymous class derived from the OnBackPressedCallback class is created.
           This anonymous class overrides the handleOnBackPressed method to provide custom behavior when the back button is pressed.
           The true passed to the constructor indicates that the callback is enabled by default.
             */
    }

     requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        /*
        The above line sets up a listener for when the back button is pressed on the device. Here's what each part does:

        1. requireActivity(): This gets the activity (MainActivity) that contains the current fragment.
        2. .onBackPressedDispatcher: This gets the dispatcher responsible for handling back button presses for the activity.
        3. .addCallback(viewLifecycleOwner, callback): This adds a callback to the back button dispatcher.
           3.1 viewLifecycleOwner: This ensures that the callback is tied to the lifecycle of the current fragment.
           3.2 callback: This is the custom action that will be executed when the back button is pressed.

      In simpler terms, this line of code sets up a way for the app to respond to the back button being pressed
      while the current fragment is visible.
         */
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}