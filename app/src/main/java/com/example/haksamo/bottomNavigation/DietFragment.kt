package com.example.haksamo.bottomNavigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.haksamo.R
import com.example.haksamo.databinding.FragmentDietBinding
import com.example.haksamo.webViewPage.AlarmPageActivity
import com.example.haksamo.webViewPage.MyPageActivity


class DietFragment : Fragment() {
    private lateinit var binding: FragmentDietBinding
    private lateinit var webView: WebView
    private lateinit var webSettings: WebSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDietBinding.inflate(layoutInflater)

        //setButton()
        initWebView()

        return binding.root
    }

    private fun initWebView() {
        webView = binding.webView
        webSettings = webView.settings
        webSettings.setJavaScriptEnabled(true)
        webView.webViewClient = MyWebViewClient()
        webView.loadUrl("http://haksamo.site/meal")
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // 페이지를 로드할 때마다 호출되며, 페이지 이동을 허용할 것인지 여부를 결정
            if (url.contains("http://haksamo.site/post/")) {
                // 페이지가 hacsamo.com/mypage인 경우 EditText 숨기기
            } else {
                // 다른 페이지인 경우 EditText 보이기
            }
            return false
        }
    }

//    private fun setButton() {
//        binding.alarm.setOnClickListener {
//            val intent = Intent(requireContext(), AlarmPageActivity::class.java)
//            startActivity(intent)
//        }
//        binding.mypage.setOnClickListener {
//            val intent = Intent(requireContext(), MyPageActivity::class.java)
//            startActivity(intent)
//        }
//    }

}