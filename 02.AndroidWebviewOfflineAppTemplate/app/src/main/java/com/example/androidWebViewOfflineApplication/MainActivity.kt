package com.game.ultimatesnake

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create the WebView
        webView = WebView(this)
        setContentView(webView)

        // Configure WebView Settings
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enables your JS
        webSettings.domStorageEnabled = true 
        webSettings.allowFileAccess = true
        
        // IMPORTANT: This allows your background_music.mp3 to auto-play!
        webSettings.mediaPlaybackRequiresUserGesture = false

        // Keep navigation inside the app
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // Load your local HTML file from the assets folder
        webView.loadUrl("file:///android_asset/index.html")
    }

    // Make the back button work for web history instead of closing the app immediately
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
