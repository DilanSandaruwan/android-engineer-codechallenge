/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.databinding.FragmentRepoDetailsBinding
import jp.co.yumemi.android.code_check.viewmodels.RepoDetailsViewModel

@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepoDetailsBinding
    lateinit var viewModel: RepoDetailsViewModel
    private val args: RepoDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepoDetailsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[RepoDetailsViewModel::class.java]
        binding.detailsVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setRepoDetails(args.repository)

        viewModel.gitHubRepoDetails.observe(viewLifecycleOwner){
            binding.ownerIconView.load(it.owner?.avatarUrl);
            binding.nameView.text = it.name;
            binding.languageView.text = it.language;
            binding.starsView.text = "${it.stargazersCount} stars";
            binding.watchersView.text = "${it.watchersCount} watchers";
            binding.forksView.text = "${it.forksCount} forks";
            binding.openIssuesView.text = "${it.openIssuesCount} open issues";
        }
    }
}
