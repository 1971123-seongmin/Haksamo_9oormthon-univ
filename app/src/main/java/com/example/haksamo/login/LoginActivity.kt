package com.example.haksamo.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.haksamo.MainActivity
import com.example.haksamo.databinding.ActivityLoginBinding
import com.example.haksamo.UserLoginDto
import com.example.haksamo.retrofit.RetrofitClient
import com.example.haksamo.retrofit.Service
import com.example.haksamo.UserTokenDto
import com.example.haksamo.token.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var service: Service
    lateinit var call: Call<UserTokenDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)


        FirebaseApp.initializeApp(this)
        getFCMToken()

        setLoginButton()

//        binding.nextBtn.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            //Toast.makeText(this, "로그인 성공.",Toast.LENGTH_SHORT).show()
//            startActivity(intent)
//        }

        setContentView(binding.root)
    }

    private fun getFCMToken(){
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("로그", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            Log.d("로그", "Fetching FCM registration token failed $token",)
            App.token_prefs.fcmToken = token
        })
    }

    private fun setLoginButton() {
        RetrofitClient.getInstance()
        service = RetrofitClient.getUserRetrofitInterface()

        binding.nextBtn.setOnClickListener{
            val email = binding.emailEdit.text.toString()
            val password =  binding.passwordEdit.text.toString()
            val token = App.token_prefs.fcmToken!!

            val loginDTO = UserLoginDto(email, password, token)
            call = service.login(loginDTO)
            Log.d("로그", "$loginDTO")

            call.enqueue(object : Callback<UserTokenDto>{

                override fun onResponse(call: Call<UserTokenDto>, response: Response<UserTokenDto>) {
                    if (response.isSuccessful) {
                        val res = response.body()!!.result
                        Log.d("로그", "res: $res")
                        val jwtToken = "$res.grantType $res.accessToken"
                        App.token_prefs.accessToken = res.accessToken
                        App.token_prefs.accessTokenExpireDate = res.accessTokenExpiresIn.toInt()
                        App.token_prefs.grantType = res.grantType
                        App.token_prefs.refreshToken = res.refreshToken
                        App.token_prefs.jwtToken = jwtToken

                    } else {
                        Log.d("로그", "로그인 실패")
                    }
                }

                override fun onFailure(call: Call<UserTokenDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

            val intent = Intent(this, MainActivity::class.java)
            //Toast.makeText(this, "로그인 성공.",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

    }
}