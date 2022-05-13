package com.quwi.testapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.quwi.testapp.R
import com.quwi.testapp.data.models.AuthStatus
import com.quwi.testapp.databinding.LoginFragmentBinding
import com.quwi.testapp.vm.AuthViewModel

class LoginFragment : BaseFragment() {

    private val binding get() = _binding as LoginFragmentBinding

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.authStatus.observe(viewLifecycleOwner) {
            when (it) {
                is AuthStatus.InitialState -> {
                    binding.llLoginForm.visibility = View.GONE
                    binding.progressIndicator.visibility = View.VISIBLE
                    authViewModel.authWithToken()
                }
                is AuthStatus.Authorized -> {
                    findNavController().navigate(R.id.action_mainFragment_to_chatListFragment)
                }
                is AuthStatus.Error -> {
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_LONG).show()
                    binding.llLoginForm.visibility = View.VISIBLE
                    binding.progressIndicator.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
                is AuthStatus.NotAuthorized -> {
                    binding.llLoginForm.visibility = View.VISIBLE
                    binding.progressIndicator.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE
            authViewModel.login(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
        }
    }
}