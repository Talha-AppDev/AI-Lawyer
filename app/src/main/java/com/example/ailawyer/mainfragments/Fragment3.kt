package com.example.ailawyer.mainfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ailawyer.databinding.Fragment3Binding

class Fragment3 : Fragment() {

    private lateinit var binding: Fragment3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  Fragment3Binding.inflate(inflater, container, false)
        return binding.root
    }
}