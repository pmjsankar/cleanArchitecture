package com.pmj.cleanarchitecture.splash

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.FragmentSplashBinding

/**
 * Fragment class to show the splash screen.
 */
class SplashFragment : Fragment() {

    companion object {
        const val SPLASH_DELAY: Long = 800
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.splash_to_home)
        }, SPLASH_DELAY)

        return binding.root
    }
}
