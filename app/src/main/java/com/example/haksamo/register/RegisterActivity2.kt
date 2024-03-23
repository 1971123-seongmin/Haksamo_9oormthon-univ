package com.example.haksamo.register

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.haksamo.AuthnMailDto
import com.example.haksamo.EmailDTO
import com.example.haksamo.UserDto
import com.example.haksamo.databinding.ActivityRegister2Binding
import com.example.haksamo.login.LoginHomeActivity
import com.example.haksamo.retrofit.RetrofitClient
import com.example.haksamo.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityRegister2Binding
    private lateinit var timer: CountDownTimer
    private lateinit var call: Call<Boolean>
    private lateinit var callString: Call<String>
    private lateinit var emailDTO: EmailDTO
    private lateinit var service: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setButton()
        setContentView(binding.root)
    }

    fun setButton() {
        RetrofitClient.getInstance()
        service = RetrofitClient.getUserRetrofitInterface()

        // 비밀번호 일치 확인 되면 서버에 전송, 인증번호 인증여부 일치확인 되면 서버 전송
        var passwordCheck = false
        var certificationCheck = false

        // 전송 버튼 -> 이메일로 인증번호 전송
        binding.sendBtn.setOnClickListener {
            setTimer()
            binding.certificationEdit.visibility = View.VISIBLE
            binding.timer.visibility = View.VISIBLE
            binding.okBtn.visibility = View.VISIBLE
            val email = binding.emailEdit.text.toString()
            // 이메일 인증 -> 서버에 이메일 보내면 서버에서 메일로 인증번호 전송
             emailDTO = EmailDTO(email)
            Log.d("로그", "${emailDTO}")
            call = service.sendAuthnMailController(emailDTO)
            call.enqueue(object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        val isSuccess = response.body()!!
                        Log.d("로그", "$isSuccess")
                        if (isSuccess) {
                            //Log.d("로그", "$isSuccess")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("로그", "이메일 서버 전송 실패")
                }

            })
        }

        // 인증번호를 서버에 전송하면 서버에서 일치하면 true 리턴
        binding.okBtn.setOnClickListener {
            val certifiaction_number = binding.certificationEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val authnMailDto = AuthnMailDto(email, certifiaction_number)
            call = service.authnCodeCheckController(authnMailDto)

            call.enqueue(object :Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        val isSuccess = response.body()!!
                        if (isSuccess) {
                            val msg = "인증이 완료되었습니다"
                            binding.certificationEdit.setText(msg)
                            binding.certificationEdit.setTextColor(Color.parseColor("#00700B"))
                            binding.certificationEdit.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00700B"))
                            timer.cancel()
                            binding.timer.visibility = View.INVISIBLE
                            certificationCheck = true
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("로그", "인증번호 서버 전송 실패")
                }
            })
        }

        // 비밀번호 변경 확인 감지 -> 일치하지 않으면 밑줄 흰색, 일치하면 밑줄 빨간색
        binding.passwordCheckEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString() == binding.passwordEdit.text.toString()) {
                    certificationCheck = true
                    binding.passwordCheckEdit.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                } else {
                    Log.d("로그", "ffff")
                    binding.passwordCheckEdit.backgroundTintList = ColorStateList.valueOf(Color.RED)
                }
            }
        })

        // 최종 회원가입 버튼 -> 비밀번호 일치 되면 동작, 인증번호 인증되면
        if (certificationCheck && passwordCheck) {

            binding.signUpBtn.setOnClickListener {
                // univId : long
                val id = 0
                val email = binding.emailEdit.text.toString()
                val name = getIntent().getStringExtra("name").toString()
                val university = getIntent().getStringExtra("university").toString()
                val major = getIntent().getStringExtra("major").toString()

                val password = binding.passwordEdit.text.toString()
                val userDto = UserDto(university, name, email, major, password)
                callString = service.signUpController(userDto)
                Log.d("로그", "$userDto")

                call.enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            Log.d("로그", "${response.isSuccessful}")
                            val isRegister = response.body()!!
                            Log.d("로그", "$isRegister")

                            if (isRegister) {
                                //회원가입 성공
                                val intent =
                                    Intent(this@RegisterActivity2, LoginHomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                //회원가입 실패
                            }
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Log.d("로그", "회원가입 서버 전송 실패")
                    }

                })

            }

        }
    }

    private fun setTimer() {
         timer = object : CountDownTimer(5 * 60 * 1000, 1000) { // 5분 타이머, 1초마다 갱신
            override fun onTick(millisUntilFinished: Long) {
                // 타이머가 갱신될 때마다 호출되는 콜백
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)

                binding.timer.setTextColor(Color.parseColor("#E40606"))
                binding.timer.text = timeString // EditText에 시간 표시
            }

            override fun onFinish() {
                // 타이머가 종료되었을 때 호출되는 콜백
                binding.timer.text ="00:00" // EditText에 시간 종료 메시지 표시
            }
        }

// 타이머 시작
        timer.start()
    }
}