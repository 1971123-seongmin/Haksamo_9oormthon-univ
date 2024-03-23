package com.example.haksamo.bottomNavigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.haksamo.databinding.FragmentHomeBinding
import com.example.haksamo.webViewPage.AlarmPageActivity
import com.example.haksamo.webViewPage.MyPageActivity


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var webView: WebView
    private lateinit var webSettings: WebSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setButton()
        initWebView()

        return binding.root
    }

    private fun setButton() {
        binding.alarm.setOnClickListener {
            val intent = Intent(requireContext(), AlarmPageActivity::class.java)
            startActivity(intent)
        }
        binding.mypage.setOnClickListener {
            val intent = Intent(requireContext(), MyPageActivity::class.java)
            startActivity(intent)
        }
    }

    // WebView setting
    private fun initWebView() {
        webView = binding.webView
        webSettings = webView.settings
        webSettings.setJavaScriptEnabled(true)
        webView.webViewClient = MyWebViewClient()
        webView.loadUrl("http://haksamo.site/")
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // 페이지를 로드할 때마다 호출되며, 페이지 이동을 허용할 것인지 여부를 결정
            if (url.startsWith("http://haksamo.site/post/")) {
                // 페이지가 hacsamo.com/mypage인 경우 EditText 숨기기
                Log.d("로그", "startswith")

            } else {
                // 다른 페이지인 경우 EditText 보이기

            }
            return false
        }
    }


    fun onBackPressed() {
        // 웹뷰에서 뒤로가기 동작 수행
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            // 웹뷰에서 더 이상 뒤로 갈 페이지가 없을 때, Fragment의 뒤로가기 동작 수행
            requireActivity().finish()
        }
    }
}