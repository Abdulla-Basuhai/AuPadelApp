package com.example.aupadelapp.model

import java.io.Serializable

data class User(
    var userId: String = "",
    var userName:String = "",
    var email: String = "",
    var role: String = "",
    var phoneNumber: String ="",
    var gender: String = ""
): Serializable

  /*
  // Null default values create a no-argument default constructor, which is needed for deserialization from a DataSnapshot.
  In Java:
  public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    In the context of Firebase Realtime Database, when you use snapshot.getValue(User::class.java),
    Firebase relies on the default (no-argument) constructor to create an instance of your data class
    and then populates its properties using the data from the DataSnapshot.

    In this case, Kotlin generates a default (no-argument) constructor behind the scenes, allowing Firebase to
    instantiate the User class during deserialization. The default values specified in the data class serve as
    fallbacks for properties that might be missing or null in the database.

    This makes the process of converting Firebase data to Kotlin objects more seamless and helps in handling
    different scenarios when the structure of the data might vary or be incomplete.


   */

   /*
    In simple words, when a class in Kotlin implements the Serializable interface, it means that objects of that class
     can be converted into a format that can be easily stored, transmitted, or reconstructed later.

     This allows instances of the User class to be converted into a series of bytes, which can then be saved to a file,
     sent over a network, or stored in other ways. Later, these bytes can be reconstructed back into a User object.

     This is particularly useful in Android when you want to pass data between activities, fragments, or store data
     in a way that allows it to survive the app's process being killed and later restored.

     The Serializable interface provides a simple way to make objects serializable, but it's worth noting that for more
      complex scenarios or better performance, other alternatives like the Parcelable interface may be considered.

    */