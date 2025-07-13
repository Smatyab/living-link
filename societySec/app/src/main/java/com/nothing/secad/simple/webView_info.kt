package com.nothing.secad.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityInformationBinding
import com.nothing.secad.databinding.ActivityWebViewInfoBinding

class webView_info : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var value= intent.getStringExtra("value")

        binding.webView.webViewClient = WebViewClient()
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            if (value == "meeting")
                //ok
                loadUrl("https://lathiyaparth.github.io/new_meeting/")
            if (value == "appConcept")
                //ok
                loadUrl("https://lathiyaparth.github.io/how_to_use_app/")
            if (value == "chat")
                //ok
                loadUrl("https://lathiyaparth.github.io/new_event/")
            if (value == "payment")
                //ok
                loadUrl("https://lathiyaparth.github.io/new_transaction/")
            if (value == "wallet")
                loadUrl("https://lathiyaparth.github.io/how-to-use-wallet/")
            if (value == "complain")
                //ok
                loadUrl("https://lathiyaparth.github.io/new_complain/")
            if (value == "note")
                //ok
                loadUrl("https://lathiyaparth.github.io/notes/")
            if (value == "customerCare")
                //ok
                loadUrl("https://lathiyaparth.github.io/custromer_care2/")
            if (value == "privatePolicy")
                //ok
                loadUrl("https://lathiyaparth.github.io/policy/")


        }
    }
}