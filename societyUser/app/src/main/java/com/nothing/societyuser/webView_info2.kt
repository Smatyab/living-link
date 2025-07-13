package com.nothing.societyuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nothing.societyuser.databinding.ActivityEventBinding
import com.nothing.societyuser.databinding.ActivityWebViewInfo2Binding

class webView_info2 : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewInfo2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewInfo2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var value= intent.getStringExtra("value")

        binding.webView.webViewClient = WebViewClient()
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            if (value == "meeting")
                loadUrl("https://lathiyaparth.github.io/meeting/")
            if (value == "appConcept")
                loadUrl("https://lathiyaparth.github.io/how_to_use_app/")
            if (value == "chat")
                loadUrl("https://lathiyaparth.github.io/chatting/")
            if (value == "payment")
                loadUrl("https://lathiyaparth.github.io/-pay_maintence/")
            if (value == "wallet")
                loadUrl("https://lathiyaparth.github.io/how-to-use-wallet/")
            if (value == "complain")
                loadUrl("https://lathiyaparth.github.io/create_complain/")
            if (value == "note")
                loadUrl("https://lathiyaparth.github.io/notes/")
            if (value == "customerCare")
                loadUrl("https://lathiyaparth.github.io/custromer_care2/")
            if (value == "privatePolicy")
                loadUrl("https://lathiyaparth.github.io/policy/")


        }



    }
}