package com.pmj.cleanarchitecture.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.FragmentDiningBinding
import com.pmj.cleanarchitecture.home.DiningDetailsFragment.Companion.ARG_DETAIL
import com.pmj.cleanarchitecture.utils.gone
import com.pmj.cleanarchitecture.utils.googleSignInClient
import com.pmj.cleanarchitecture.utils.setImageUrl
import com.pmj.cleanarchitecture.utils.visible
import com.pmj.domain.model.Dining
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiningFragment : Fragment() {

    private val diningViewModel: DiningViewModel by viewModels()
    private val firebaseDbViewModel: FirebaseDbViewModel by viewModels()
    private lateinit var binding: FragmentDiningBinding
    private lateinit var adapter: DiningAdapter
    private var snackBar: Snackbar? = null
    private var selectedThemeIndex: Int = 0

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
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        GoogleSignIn.getLastSignedInAccount(requireContext())?.let {
            setImageUrl(binding.ivUserImage, it.photoUrl.toString())
            val name = "Hi ${it.givenName}!"
            binding.tvDiningUserName.text = name
        }
    }

    private fun subscribeUi() {
        binding.ibToggleNightMode.setOnClickListener {
            showThemeSelectionDialog()
        }

        binding.ivUserImage.setOnClickListener {
            showLogoutDialog()
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

        diningViewModel.selectedTheme.observe(viewLifecycleOwner) { selectedIndex ->
            selectedThemeIndex = selectedIndex
            setTheme(selectedIndex)
        }
    }

    private fun setTheme(selectedThemeIndex: Int) {
        when (selectedThemeIndex) {
            0 -> {
                val nightModeFlags = resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    Configuration.UI_MODE_NIGHT_NO,
                    Configuration.UI_MODE_NIGHT_UNDEFINED ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun showThemeSelectionDialog() {
        var selChoice = selectedThemeIndex
        val themes = resources.getStringArray(R.array.theme_array)
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.choose_theme))
            .setSingleChoiceItems(themes, selectedThemeIndex) { _, which ->
                selChoice = which
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                selectedThemeIndex = selChoice
                diningViewModel.toggleNightMode(selectedTheme = selectedThemeIndex)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setMessage(getString(R.string.logout_msg))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                requireActivity().googleSignInClient.signOut().addOnCompleteListener {
                    findNavController().navigate(R.id.home_to_signIn)
                }
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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