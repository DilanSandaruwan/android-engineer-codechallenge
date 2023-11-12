/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.TagConstant
import jp.co.yumemi.android.code_check.databinding.FragmentRepoDetailsBinding
import jp.co.yumemi.android.code_check.util.components.CustomDialogFragment
import jp.co.yumemi.android.code_check.viewmodels.RepoDetailsViewModel

/**
 * Fragment for displaying repository details.
 */
@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepoDetailsBinding

    /**
     * Initializes the ViewModel and NavArgs.
     */
    private val viewModel: RepoDetailsViewModel by viewModels()
    private val args: RepoDetailsFragmentArgs by navArgs()

    /**
     * Inflates the layout and initializes the ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(layoutInflater, container, false).apply {
            detailsVM = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    /**
     * Sets up the user interface and observes changes in the repository details.
     *
     * @param view The root view for the fragment.
     * @param savedInstanceState The saved instance state, if any.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in the repository details and update the UI
        viewModel.gitHubRepoDetails.observe(viewLifecycleOwner) { gitHubAccount ->
            binding.ownerIconView.load(gitHubAccount.owner?.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_repository)
                transformations(CircleCropTransformation())
                listener(onError = { _, _ ->
                    placeholder(R.drawable.placeholder_repository)
                })
            }

            // Add an OnClickListener to the floating action button
            binding.navFloatingActionButton.setOnClickListener {
                val url = gitHubAccount.htmlUrl
                if (gitHubAccount.htmlUrl.isNullOrBlank()) {
                    showErrorDialog(getString(R.string.url_not_found))
                } else {
                    openUrlInBrowser(url!!)
                }
            }
        }

        // Set the repository details in the ViewModel based on arguments passed to the fragment
        viewModel.setRepoDetails(args.repository)
    }

    /**
     * Opens a URL in a web browser.
     *
     * @param url The URL to open.
     */
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val chooser = Intent.createChooser(intent, getString(R.string.select_a_browser))
        startActivity(chooser)
    }

    /**
     * Displays an error dialog with a given error message.
     *
     * @param errMsg The error message to be displayed in the dialog.
     */
    private fun showErrorDialog(errMsg: String) {
        // Create a custom dialog fragment with the provided error details.
        val dialog = CustomDialogFragment.newInstance(
            title = getString(R.string.error_title),
            message = errMsg,
            positiveText = getString(R.string.response_ok),
            negativeText = "",
            positiveClickListener = { },
            negativeClickListener = { },
            iconResId = R.drawable.ic_dialog_error
        )
        // Show the error dialog using the child fragment manager and a defined tag.
        dialog.show(childFragmentManager, TagConstant.ERROR_DIALOG_DETAILS_TAG)

    }

    /**
     * Called when the view is about to be destroyed.
     * Removes observers to prevent potential memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        // Remove observers for GitHub repository details to avoid potential memory leaks.
        viewModel.gitHubRepoDetails.removeObservers(viewLifecycleOwner)
    }
}
