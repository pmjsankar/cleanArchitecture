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
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningFragment : Fragment() {

    private val diningViewModel: DiningViewModel by viewModels()
    private var binding: FragmentDiningBinding? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        diningViewModel.run {
            fetchDining()
            getCurrentMode()
        }
    }

    private fun subscribeUi() {
        binding?.let {

            it.ibToggleNightMode.setOnClickListener {
                lifecycleScope.launch {
                    diningViewModel.toggleNightMode()
                }
            }

            adapter = DiningAdapter(onItemClick)
            it.rvDiningList.adapter = adapter
            it.swipeRefresh.setOnRefreshListener {
                hideSnackBar()
                diningViewModel.fetchDining()
            }
        }

        with(diningViewModel) {

            diningList.observe(viewLifecycleOwner) { result ->
                binding?.swipeRefresh?.isRefreshing = when (result.status) {
                    Output.Status.SUCCESS -> {
                        result.data?.let { list ->
                            adapter.submitList(list)
                        }
                        false
                    }
                    Output.Status.ERROR -> {
                        result.message?.let {
                            showError(it) {
                                diningViewModel.fetchDining()
                            }
                        }
                        false
                    }
                    Output.Status.LOADING -> true
                }
            }

            isNightMode.observe(viewLifecycleOwner) { nightMode ->
                AppCompatDelegate.setDefaultNightMode(
                    if (nightMode == true) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
            }
        }
    }

    /**
     * @property onItemClick to handle the dining item click.
     */
    private val onItemClick: (dining: Dining, view: View) -> Unit =
        { dining, view ->
            val extras = FragmentNavigatorExtras(
                view to dining.imageUrl.toString()
            )
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

    override fun onStop() {
        hideSnackBar()
        super.onStop()
    }
}