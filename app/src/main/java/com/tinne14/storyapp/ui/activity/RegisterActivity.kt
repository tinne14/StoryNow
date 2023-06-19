package com.tinne14.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tinne14.storyapp.R
import com.tinne14.storyapp.databinding.ActivityRegisterBinding
import com.tinne14.storyapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.isLoading.observe(this, Observer {
            showLoading(it)
        })

        binding.passwordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString().trim()).matches()
                val isValidPassword =  s.toString().length >= 8
                if(isValidEmail && isValidPassword){
                    binding.signupButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString().trim()).matches()
                val isValidPassword =  s.toString().length >= 8
                if(isValidEmail && isValidPassword){
                    binding.signupButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.name_notempty)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_notempty)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.pass__notempty)
                }
                else -> {
                    viewModel.postRegister(name, email, password, this)
                    viewModel.registerResponse.observe(this, Observer {
                        if (it == false) {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    })
                }
            }

        }


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}