package com.nothing.secad.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nothing.secad.databinding.FragmentMeetingBinding
import com.nothing.secad.meeting.ZoomMainActivity
import com.nothing.secad.meeting.user.UseMeetingMainActivity

class MeetingFragment : Fragment() {

    private var _binding: FragmentMeetingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example of navigating to MainActivity when a button is clicked
        binding.connectWithUserMeeting.setOnClickListener {
            val intent = Intent(requireActivity(), UseMeetingMainActivity::class.java)
            startActivity(intent)
        }
        binding.connectWithSecMeeting.setOnClickListener {
            val intent = Intent(requireActivity(), ZoomMainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
