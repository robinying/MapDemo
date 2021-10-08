package com.robin.mapdemo.ui.recycleview

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel

import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseVBActivity
import com.robin.mapdemo.databinding.ActivityRecycleViewBinding

class RecycleViewActivity : BaseVBActivity<BaseViewModel, ActivityRecycleViewBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
        val navController = Navigation.findNavController(this, R.id.nav)
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(binding.navView.menu, binding.drawer)
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.subtitle =
                (destination as FragmentNavigator.Destination).className.substringAfterLast('.')
        }

        binding.navView.setupWithNavController(navController)

    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawers()
        } else super.onBackPressed()
    }
}