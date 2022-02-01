package com.bogdanov.tutu.presentation.user_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogdanov.tutu.R
import com.bogdanov.tutu.data.remote.models.UserDto
import com.bogdanov.tutu.databinding.UsersListFragmentBinding
import com.bogdanov.tutu.domain.models.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UsersListFragment : Fragment(R.layout.users_list_fragment), UserPagingAdapter.OnItemClickListener {

    private val viewModel: UsersListViewModel by sharedViewModel()
    private val binding: UsersListFragmentBinding by viewBinding(UsersListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUsersList()
    }

    private fun initUsersList(){

        val adapter = UserPagingAdapter(this)

        binding.list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.list.adapter = adapter

        observeUsers(adapter)

        binding.searchButton.setOnClickListener {
            searchUsers(binding.searchTextInput.text.toString())
        }

    }


    private fun observeUsers(adapter: UserPagingAdapter){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.usersFlow.collectLatest{
                adapter.submitData(it)
            }
        }
    }

    private fun searchUsers(query: String){
        viewModel.searchBy(query)
    }


    override fun onItemClick(item: User) {
        val action = UsersListFragmentDirections.actionUsersListFragmentToUserDetailFragment(item.username)
        findNavController().navigate(action)
    }

}