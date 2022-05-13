package com.quwi.testapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quwi.testapp.R
import com.quwi.testapp.adapters.ChatChannelsPagingAdapter
import com.quwi.testapp.adapters.LoadStateAdapter
import com.quwi.testapp.data.models.AuthStatus
import com.quwi.testapp.databinding.ChatListFragmentBinding
import com.quwi.testapp.vm.AuthViewModel
import com.quwi.testapp.vm.ChatListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle

class ChatListFragment : BaseFragment() {

    private val binding get() = _binding as ChatListFragmentBinding

    private val authViewModel: AuthViewModel by activityViewModels()
    private val chatViewModel: ChatListViewModel by viewModels()

    private lateinit var chatsPagingAdapter: ChatChannelsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.currentUser?.let {
            chatsPagingAdapter = ChatChannelsPagingAdapter(it.id)
            val headerAdapter = LoadStateAdapter()
            val footerAdapter = LoadStateAdapter()
            with(binding.rvChats) {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = chatsPagingAdapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                chatsPagingAdapter.addLoadStateListener {
                    headerAdapter.loadState = it.refresh
                }
            }
        }

        authViewModel.authStatus.observe(viewLifecycleOwner) {
            when (it) {
                is AuthStatus.InitialState,
                is AuthStatus.NotAuthorized,
                is AuthStatus.Error -> {
                    findNavController().navigate(R.id.action_chatListFragment_to_loginFragment)
                }
                else -> {}
            }
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }

        lifecycleScope.launch {
            chatViewModel.chatChannelsF.collectLatest { data ->
                chatsPagingAdapter.submitData(data)
            }
        }
    }
}