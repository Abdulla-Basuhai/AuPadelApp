package com.example.aupadelapp.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.example.aupadelapp.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object UserRepository{

    //Declare an instance of FirebaseAuth.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // To read or write data from the database, you need an instance of DatabaseReference
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://au-padel-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
    /*
    To get a reference to a database other than a us-central1 default database,you must pass the database URL to getInstance().
    for us-central1 default database ,you can call getInstance()without arguments
     */


    // register a new user
    fun registerNewUser(user: User,password: String, callback: (Boolean, String) -> Unit) {
        // Assuming 'auth' is the Firebase authentication instance
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { createNewAccountTask ->
                if (createNewAccountTask.isSuccessful) {
                    // Registration successful
                    Log.d("registerNewUser","registrationTask is Successful")
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            // Email verification sent successfully
                            Log.d("registerNewUser","Email verification sent successfully")
                            user.userId = auth.currentUser?.uid ?: ""
                            // To perform a write operation on the database, first access the desired child node (under the Users node)
                            // then use the setValue method to set the new value.
                            // Finally, ensure that the operation is completed successfully.
                            databaseReference.child(user.userId).setValue(user)
                                .addOnCompleteListener{ databaseRegistrationTask ->
                                    if (databaseRegistrationTask.isSuccessful){
                                        Log.d("FirebaseUser","registerNewUser.instance of FirebaseAuth is: ${auth.currentUser}")
                                        callback(true, "Registration and verification tasks are successful")
                                    }
                                    else{
                                        // Handle verification email sending failure
                                        Log.d("registerNewUser","database Registration Task failed: ${databaseRegistrationTask.exception?.message}")
                                        callback(false, "database Registration Task failed: ${databaseRegistrationTask.exception?.message}")
                                    }
                                }
                        }
                        else {
                            // Handle verification email sending failure
                            Log.d("registerNewUser","Email verification failed")
                            callback(false, "Verification email failed: ${verificationTask.exception?.message}")
                        }
                    }
                } else {
                    // Handle createNewAccountTask failure
                    Log.d("registerNewUser","createNewAccountTask failed")
                    callback(false, "create New Account Task failed: ${createNewAccountTask.exception?.message}")
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

    // Get the current logged-in user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /*
     this function uses Firebase Realtime Database to get user data for a given UID.
     It takes a UID and a callback function as parameters. The callback function is called with the retrieved
     user object if successful or null if there is an error.

     we want to fetch User data only when the getUserData function is explicitly called and not continuously listen
     for real-time updates, so we used the addListenerForSingleValueEvent method, This method retrieves the data once
     and does not continue listening for changes after the initial fetch, making it suitable for scenarios
     where you want to fetch the data on-demand (user update the profile, user navigate to the profile screen).

     The addListenerForSingleValueEvent method is part of Firebase's Realtime Database API,
     and it is used to fetch data from a specific location in the database. Here's the method signature:

     <<<<<      fun addListenerForSingleValueEvent(valueEventListener: ValueEventListener): Unit  >>>>>

     You need to pass an object that implements the ValueEventListener interface, specifying how your app should
     respond to changes in the data.
     when you see Unit as the return type, it essentially means that the function doesn't return a value (void),
     and its purpose lies in its side effects or actions rather than a result that needs to be used elsewhere in the code.

     The ValueEventListener interface defines two callback methods: onDataChange and onCancelled.
     These methods allow you to respond to successful data retrieval (onDataChange) or handle errors (onCancelled)
      during the database operation.

      ##  addListenerForSingleValueEvent VS addValueEventListener ##
      While addListenerForSingleValueEvent fetches data only once, Firebase also provides addValueEventListener
      for continuous real-time updates. By implementing ValueEventListener, you can later choose to switch to
      addValueEventListener if your requirements change and you need real-time updates.
     */

    fun getUserData(uid: String,callback: (User?) -> Unit) {
        databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 'snapshot' now represents the data at 'users/userID' which is under a specific user ID node
                // Extract the User object from the snapshot using getValue
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    callback(user)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepo", "Error fetching user data: ${error.message}")
                callback(null)
            }
        })

    }

}