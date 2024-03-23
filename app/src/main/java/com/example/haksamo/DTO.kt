package com.example.haksamo

import com.google.gson.annotations.SerializedName

// 로그인 -------------------------------------------
data class UserLoginDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("fcmToken") val fcmToken: String,
)
data class UserTokenDto(
    val result: TokenResultDto
)

data class TokenResultDto(
    @SerializedName("id") val id: Long,
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("accessTokenExpiresIn") val accessTokenExpiresIn: Long
)
// ------------------------------------------- 회원가입

data class UniversityListDto(
    @SerializedName ("universities") val universities : List<UniversityDto>
)
data class UniversityDto(
    @SerializedName ("email") val email: String,
    @SerializedName  ("univId") val univId: Long,
    @SerializedName  ("univName") val univName: String,
)

data class UserDto(
    @SerializedName("univName") val univName: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("major") val major: String,
    @SerializedName("password") val password: String
)

data class EmailDTO(
    @SerializedName("email") val email: String
)
data class AuthnMailDto(
    // 인증번호 확인, 이메일, 코드 모두보냄
    @SerializedName("email") val grantType: String,
    @SerializedName("AuthCode") val accessToken: String,
)