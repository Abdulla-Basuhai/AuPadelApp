package com.example.aupadelapp.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.example.aupadelapp.model.User


object UserRepository{

    //Declare an instance of FirebaseAuth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    // register a new user
    fun registerNewUser(user: User, callback: (Boolean, String) -> Unit) {
        // Assuming 'auth' is the Firebase authentication instance
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { registrationTask ->
                if (registrationTask.isSuccessful) {
                    // Registration successful
                    Log.d("registerNewUser","registrationTask is Successful")
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            // Email verification sent successfully
                            Log.d("registerNewUser","Email verification sent successfully")
                            user.userId = auth.currentUser?.uid ?: ""
                            Log.d("FirebaseUser","registerNewUser.instance of FirebaseAuth is: ${auth.currentUser}")
                            callback(true, "Registration and verification tasks are successful")
                        } else {
                            // Handle verification email sending failure
                            Log.d("registerNewUser","Email verification failed")
                            callback(false, "Verification email failed: ${verificationTask.exception?.message}")
                        }
                    }
                } else {
                    // Handle registration failure
                    Log.d("registerNewUser","registrationTask failed")
                    callback(false, "Registration failed: ${registrationTask.exception?.message}")
                }
            }
    }
    /*
    fun registerUser(user: User, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    user.userId = userId
                    callback(true, "Registration successful.")
                    }
                else {
                    callback(false, "Registration failed. ${task.exception?.message}")
                }
            }
    }
     */

    /*
     In simple words, a callback is like leaving a phone number for someone to call you back when they are done with a task.
     In programming, it's a function (or a piece of code) that you provide as an argument to another function.
     This allows the other function to call or execute your code when a specific action or event is completed.

     In the context of Android development, the registerNewUser function is asking for a callback function as an input.
     It's saying, "Hey, when the user registration is done, I'll call the function you provided so you can decide
     what to do next." The callback is a way for you to customize the behavior after the registration task is finished.
     */

    /*
    (Boolean, String) -> Unit is a function type declaration for the callback parameter. Let's break it down:

    * (Boolean, String) specifies the types of parameters that the callback function should accept.
      In this case, it's a function that takes two parameters: a Boolean and a String.

    * -> is a separator indicating the transition from parameter types to the return type.

    * Unit is the return type. In Kotlin, Unit is similar to void in other languages,
      indicating that the function doesn't return any meaningful value.

      Putting it all together, (Boolean, String) -> Unit describes a callback function that takes
       a Boolean and a String as parameters and doesn't return anything significant (Unit).

      This matches the signature of the callback used in the registerNewUser function,
      where the callback is expected to handle the result of the user registration and doesn't need to return a value.
     */


    // Sign in existing users
    fun loginUser(email:String,password:String, callback: (Boolean, String) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{loginTask ->
                if (loginTask.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true){
                        Log.d("FirebaseUser","Sign in existing user.instance of FirebaseAuth is: ${auth.currentUser}")
                        callback(true, "Login successful.")
                    }
                    else{
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful){
                                    callback(false, "Kindly verify your email")
                            }
                        else{
                            callback(false,"Login failed: ${verificationTask.exception?.message}")
                        }
                    }
                }
                } else {
                    callback(false, "Login failed. ${loginTask.exception?.message}")
                }
            }
    }
// Log out the current user
    fun signOutUser() {
        auth.signOut()
    Log.d("FirebaseUser","SignOut Current User.instance of FirebaseAuth is: ${auth.currentUser}")
    }
}