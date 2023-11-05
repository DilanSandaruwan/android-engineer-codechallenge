/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

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
import jp.co.yumemi.android.code_check.databinding.FragmentRepoDetailsBinding
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

        // Set the repository details in the ViewModel based on arguments passed to the fragment
        viewModel.setRepoDetails(args.repository)

        // Observe changes in the repository details and update the UI
        viewModel.gitHubRepoDetails.observe(viewLifecycleOwner) {
            binding.ownerIconView.load(it.owner?.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_repository)
                transformations(CircleCropTransformation())
            }
        }
    }
}
