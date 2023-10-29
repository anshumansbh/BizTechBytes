package com.biztechbytes.biztechbytes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    interface OnToggleViewVisibilityListener {
        fun onToggleViewVisibility(isVisible: Boolean)
    }

    // Listener reference
    private var toggleViewVisibilityListener: OnToggleViewVisibilityListener? = null


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private val RC_SIGN_IN = 10012

    private lateinit var btGoogleSignIn: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity as Activity, gso)

        // Check if the user is already signed in
        if (firebaseAuth?.currentUser != null) {
            toggleViewVisibilityListener?.onToggleViewVisibility(true)
        }else{
            // Call the callback method to toggle view visibility
            toggleViewVisibilityListener?.onToggleViewVisibility(false) // Pass your desired visibility status here
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        btGoogleSignIn = view.findViewById(R.id.btGoogleLogin)
        btGoogleSignIn.setOnClickListener { startSignInProcess() }
        return view
    }

    private fun startSignInProcess()
    {
        val intent = mGoogleSignInClient?.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign-In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    // Google Sign-In failed, handle the error
                    Snackbar.make(
                        btGoogleSignIn,
                        "Login failed: ${e.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun setOnToggleViewVisibilityListener(listener: OnToggleViewVisibilityListener) {
        toggleViewVisibilityListener = listener
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    val user = firebaseAuth?.currentUser
                    toggleViewVisibilityListener?.onToggleViewVisibility(true)
                } else {
                    Snackbar.make(btGoogleSignIn, "Firebase Authentication failed", Snackbar.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, "")
                    putString(ARG_PARAM2, "")
                }
            }
    }
}