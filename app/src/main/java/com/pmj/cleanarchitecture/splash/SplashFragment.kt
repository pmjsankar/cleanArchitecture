package com.pmj.cleanarchitecture.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragment class to show the splash screen.
 */
class SplashFragment : Fragment() {

    companion object {
        const val SPLASH_DELAY: Long = 1300
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main.immediate) {
            delay(SPLASH_DELAY)
            if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
                findNavController().navigate(R.id.splash_to_home)
            } else {
                findNavController().navigate(R.id.splash_to_signIn)
            }
        }
        return binding.root
    }
}
