package com.unina.natourkt.presentation.post_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unina.natourkt.R
import com.unina.natourkt.presentation.base.fragment.BaseFragment

class ViewerPostDetailsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewer_post_details, container, false)
    }



}