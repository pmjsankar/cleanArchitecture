package com.pmj.cleanarchitecture.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.FragmentDiningBinding
import com.pmj.cleanarchitecture.home.DiningDetailsFragment.Companion.ARG_DETAIL
import com.pmj.cleanarchitecture.utils.gone
import com.pmj.cleanarchitecture.utils.visible
import com.pmj.domain.model.Dining
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DiningFragment : Fragment() {

    private val diningViewModel: DiningViewModel by viewModels()
    private val firebaseDbViewModel: FirebaseDbViewModel by viewModels()
    private lateinit var binding: FragmentDiningBinding
    private lateinit var adapter: DiningAdapter
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDiningBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diningViewModel.getCurrentMode()
        firebaseDbViewModel.fetchDining()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        binding.ibToggleNightMode.setOnClickListener {
            lifecycleScope.launch {
                diningViewModel.toggleNightMode()
            }
        }

        adapter = DiningAdapter(onItemClick)
        binding.rvDiningList.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            hideSnackBar()
            firebaseDbViewModel.fetchDining()
        }

        firebaseDbViewModel.diningViewState.observe(viewLifecycleOwner) { diningState ->
            when (diningState) {
                is DiningViewState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.shimmerLayout.visible()
                    binding.rvDiningList.gone()
                }

                is DiningViewState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.shimmerLayout.gone()
                    binding.rvDiningList.visible()
                    adapter.submitList(diningState.data)
                }

                is DiningViewState.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.shimmerLayout.gone()
                    binding.rvDiningList.visible()
                    showError(diningState.message) { firebaseDbViewModel.fetchDining() }
                }
            }
        }

        diningViewModel.isNightMode.observe(viewLifecycleOwner) { nightMode ->
            AppCompatDelegate.setDefaultNightMode(
                if (nightMode == true) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }

    /**
     * @property onItemClick to handle the dining item click.
     */
    private val onItemClick: (dining: Dining, view: View) -> Unit =
        { dining, view ->
            val extras = FragmentNavigatorExtras(view to dining.imageUrl.toString())
            findNavController().navigate(
                R.id.home_to_detail,
                bundleOf(ARG_DETAIL to dining),
                null,
                extras
            )
        }

    private fun showError(msg: String, onRetry: () -> Unit) {
        view?.let {
            snackBar = Snackbar.make(it, msg, Snackbar.LENGTH_INDEFINITE)
            snackBar?.setAction(getString(R.string.retry)) {
                hideSnackBar()
                onRetry.invoke()
            }
            snackBar?.show()
        }
    }

    private fun hideSnackBar() = snackBar?.dismiss()

    override fun onPause() {
        hideSnackBar()
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()
    }
}