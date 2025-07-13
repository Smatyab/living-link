package com.nothing.secad.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nothing.secad.databinding.FragmentChatBinding
import com.nothing.secad.simple.PayToAdmin

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            //TODO:add chat activity
            val i = Intent(requireContext(), PayToAdmin::class.java)
            startActivity(i)

            // close this fragment
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }, 200)
    }
}
