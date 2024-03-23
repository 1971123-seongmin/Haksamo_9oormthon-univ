package com.example.haksamo.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.haksamo.EmailDTO
import com.example.haksamo.UniversityDto
import com.example.haksamo.UniversityListDto
import com.example.haksamo.databinding.ActivityRegisterBinding
import com.example.haksamo.retrofit.RetrofitClient
import com.example.haksamo.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    private lateinit var call: Call<UniversityListDto>
    private lateinit var service: Service
    private lateinit var items: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getFetchFromData

        binding.nextBtn.setOnClickListener {
            val name = binding.nameEdit.text.toString()
            val major = binding.majorEdit.text.toString()
            val university = binding.universityEdit.text.toString()

            val intent = Intent(this, RegisterActivity2::class.java)

            intent.putExtra("name", name)
            intent.putExtra("university", university)
            intent.putExtra("major", major)
            startActivity(intent)

        }
    }
    fun getFetchFromData() {
        binding.nextBtn.setOnClickListener {
            RetrofitClient.getInstance()
            service = RetrofitClient.getUserRetrofitInterface()
            call = service.getUniversities()


            call.enqueue(object : Callback<UniversityListDto> {
                override fun onResponse(
                    call: Call<UniversityListDto>,
                    response: Response<UniversityListDto>
                ) {
                    val universityListDto = response.body()
                    val universities: List<UniversityDto>? = universityListDto?.universities
                    universities?.let {
                        items.clear()
                        for (university in it) {
                            val univName = university.univName

                            items.add(univName)
                        }
                    }
                }

                override fun onFailure(call: Call<UniversityListDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}