package com.pmj.cleanarchitecture.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.pmj.cleanarchitecture.databinding.FragmentDiningDetailsBinding

class DiningDetailsFragment : Fragment() {

    companion object {
        const val ARG_DETAIL = "diningDetail"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = with(FragmentDiningDetailsBinding.inflate(inflater, container, false)) {
        lifecycleOwner = this@DiningDetailsFragment
        item = arguments?.getParcelable(ARG_DETAIL)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        root
    }
}