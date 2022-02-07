package com.bogdanov.tutu.presentation.user_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogdanov.tutu.R
import com.bogdanov.tutu.databinding.UserDetailFragmentBinding
import com.bogdanov.tutu.domain.models.User
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserDetailFragment : Fragment(R.layout.user_detail_fragment) {

    private val binding: UserDetailFragmentBinding by viewBinding(UserDetailFragmentBinding::bind)

    private val args: UserDetailFragmentArgs by navArgs()

    private val viewModel: UserDetailViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        initialObserv()

        getUserInfo(args.username)





    }

    private fun initialObserv() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userInfo.collect {
                setUserInfo(it)
            }
        }
    }

    private fun getUserInfo(username: String){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUserInfo(username)
        }

    }

    private fun setUserInfo(user: User?){
        with(binding){
            username.text = user?.username
            name.text = user?.name
            company.text = user?.company
            email.text = user?.email
            location.text = user?.location
            followers.text = user?.followers.toString()
            following.text = user?.following.toString()
            if (user?.isCashed == true)
            {
                isCashed.text = "✅"
            }else {
                isCashed.text = "✖"
            }

        }

        view?.let {
            Glide.with(it)
                .load(user?.avatar)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.avatar)
        }

    }
}