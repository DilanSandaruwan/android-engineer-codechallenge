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
import dagger.hilt.android.AndroidEntryPoint
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
    ): View? {
        binding = FragmentRepoDetailsBinding.inflate(layoutInflater, container, false).apply {
            detailsVM = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    /**
     * Sets up the UI and observes the changes in the repository details.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setRepoDetails(args.repository)

        viewModel.gitHubRepoDetails.observe(viewLifecycleOwner) {
            binding.ownerIconView.load(it.owner?.avatarUrl)
        }
    }
}
