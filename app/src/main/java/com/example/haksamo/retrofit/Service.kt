package com.example.haksamo.retrofit


import com.example.haksamo.AuthnMailDto
import com.example.haksamo.EmailDTO
import com.example.haksamo.UniversityListDto
import com.example.haksamo.UserDto
import com.example.haksamo.UserLoginDto
import com.example.haksamo.UserTokenDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Service {

    // 회원 가입 버튼 클릭 -> 유저정보 모두 전달
    @POST("haksamo/sign-up")
    fun signUpController(@Body userDto: UserDto?) : Call<String>

    // 인증번호, 이메일을 서버로 전송
    @POST("haksamo/authn/email")
    fun sendAuthnMailController(@Body emailDTO: EmailDTO): Call<Boolean>

    @POST("authn/email/check")
    fun authnCodeCheckController(@Body authnMailDto: AuthnMailDto): Call<Boolean>

    // 로그인 할 때 JWT 토큰반환
    @POST("haksamo/authn/login")
    fun login (@Body loginDTO: UserLoginDto) : Call <UserTokenDto>

    @GET("api/v1/usrs/universities")
    fun getUniversities(): Call<UniversityListDto>

}