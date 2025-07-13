package com.nothing.societyuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nothing.societyuser.databinding.FragmentMeetingBinding
import com.nothing.societyuser.meeting.MeetingConsforanceActivity
import java.util.UUID

class MeetingFragment : Fragment() {

    // Declare a nullable binding variable
    private var _binding: FragmentMeetingBinding? = null

    // Use the non-null assert to access the binding object
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using the binding object
        _binding = FragmentMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example of accessing views and setting up click listeners
        binding.joinBtn.setOnClickListener {
            // Perform action when the join meeting button is clicked
            val meetingId = binding.meetingIdInput.text.toString()
            val name = binding.meetingNameInput.text.toString()

            // Example validation
            if (meetingId.isEmpty()) {
                binding.meetingIdInput.error = "Meeting ID cannot be empty"
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                binding.meetingNameInput.error = "Name cannot be empty"
                return@setOnClickListener
            }

            // Call function to start the meeting
            startMeeting(meetingId, name)
        }
    }

    private fun startMeeting(meetingId: String, name: String) {
        // Here you can handle logic to start the meeting
        // For example, you can create an Intent to navigate to the meeting activity
        val userID = UUID.randomUUID().toString()
        val intent = Intent(requireContext(), MeetingConsforanceActivity::class.java)
        intent.putExtra("meeting_ID", meetingId)
        intent.putExtra("user_ID", userID)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object when the view is destroyed to avoid memory leaks
        _binding = null
    }
}
