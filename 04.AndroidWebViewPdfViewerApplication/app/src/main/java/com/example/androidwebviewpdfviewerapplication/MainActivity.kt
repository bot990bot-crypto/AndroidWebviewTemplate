package com.example.androidwebviewpdfviewerapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView

    private val openPdfLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> openPdf(uri) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val rootLayout = FrameLayout(this)


        webView = WebView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.domStorageEnabled = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
            webViewClient = WebViewClient()
        }

        val fabSize = dpToPx(56)
        val fabMargin = dpToPx(24)

        val fab = TextView(this).apply {
            text = "📂"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD

            layoutParams = FrameLayout.LayoutParams(fabSize, fabSize).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                bottomMargin = fabMargin
                rightMargin = fabMargin
            }

            elevation = dpToPx(6).toFloat()
            outlineProvider = android.view.ViewOutlineProvider.BACKGROUND
            background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(Color.GRAY)
            }
            clipToOutline = true

            setOnClickListener { launchFilePicker() }
        }

        rootLayout.addView(webView)
        rootLayout.addView(fab)
        setContentView(rootLayout)

        val pdfUri: Uri? = intent?.data
        if (pdfUri != null) {
            openPdf(pdfUri)
        } else {
            webView.loadUrl(
                "file:///android_asset/pdf.js/web/viewer.html?file=file:///android_asset/sample_pdf.pdf"
            )
        }
    }

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        openPdfLauncher.launch(intent)
    }

    private fun openPdf(uri: Uri) {
        val input = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "temp.pdf")
        FileOutputStream(file).use { output -> input?.copyTo(output) }
        input?.close()

        webView.loadUrl(
            "file:///android_asset/pdf.js/web/viewer.html?file=${file.absolutePath}"
        )
    }
}