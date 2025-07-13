package com.nothing.secad.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nothing.secad.databinding.FragmentTransactionBinding // Import the generated binding class
import com.nothing.secad.R
import com.nothing.secad.simple.ReceivePaymentUser
import com.nothing.secad.simple.SendPaymentAdmin

class TransactionFragment : Fragment() {


    // Declare a binding variable
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using the generated binding class
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recivePaymentToUserBtn.setOnClickListener(){
            var Intent = Intent(activity, ReceivePaymentUser::class.java)
            startActivity(Intent)

        }

        binding.sendPaymentToAdminBtn.setOnClickListener(){
            var Intent = Intent(activity, SendPaymentAdmin::class.java)
            startActivity(Intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify the binding reference to avoid memory leaks
        _binding = null
    }


}
