package com.pmj.cleanarchitecture.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.FragmentSignInBinding
import com.pmj.cleanarchitecture.utils.gone
import com.pmj.cleanarchitecture.utils.googleSignInClient
import com.pmj.cleanarchitecture.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSignInBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    override fun onPause() {
        hideSnackBar()
        super.onPause()
    }

    private fun subscribeUi() {
        FirebaseApp.initializeApp(requireContext())
        mGoogleSignInClient = requireActivity().googleSignInClient
        binding.btnGoogleSignIn.setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInResultLauncher.launch(signInIntent)
        binding.pbSignIn.visible()
        binding.btnGoogleSignIn.isEnabled = false
    }

    private var signInResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            binding.pbSignIn.gone()
            binding.btnGoogleSignIn.isEnabled = true
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleResult(task)
            }
        }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            showMsg(getString(R.string.an_error_occurred))
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToHome()
            } else {
                showMsg(getString(R.string.an_error_occurred))
            }
        }
    }

    private fun showMsg(message: String) {
        view?.let {
            snackBar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            snackBar?.show()
        }
    }

    private fun hideSnackBar() = snackBar?.dismiss()

    private fun navigateToHome() = findNavController().navigate(R.id.signIn_to_home)
}