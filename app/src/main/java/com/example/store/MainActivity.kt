package com.example.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast

class MainActivity : Activity() {
    private val CODE = "61061"
    private val URL = "https://mystore.mediamarktsaturn.com/"
    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
        }
        setContentView(web)

        val prefs = getSharedPreferences("app", Context.MODE_PRIVATE)
        if (prefs.getBoolean("vicenlock", false)) {
            web.loadUrl(URL)
        } else {
            askCode(prefs)
        }
    }

    private fun askCode(prefs: SharedPreferences) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }
        AlertDialog.Builder(this)
            .setTitle("Alto ahí, forastero. ¿Contraseña?")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Entrar") { _, _ ->
                if (input.text.toString() == CODE) {
                    prefs.edit().putBoolean("vicenlock", true).apply()
                    web.loadUrl(URL)
                } else {
                    Toast.makeText(this, "Ese no es, intruso 🕵️", Toast.LENGTH_SHORT).show()
                    askCode(prefs)
                }
            }
            .setNegativeButton("Salir") { _, _ -> finish() }
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (web.canGoBack()) web.goBack() else super.onBackPressed()
    }
}
