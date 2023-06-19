package com.tinne14.storyapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import com.tinne14.storyapp.R
import com.tinne14.storyapp.databinding.ActivityLoginBinding
import com.tinne14.storyapp.ui.ViewModelFactory
import com.tinne14.storyapp.ui.viewmodel.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.isLoading.observe(this, Observer {
            showLoading(it)
        })

        binding.passwordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString().trim()).matches()
                val isValidPassword =  s.toString().length >= 8
                if(isValidEmail && isValidPassword){
                    binding.loginButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.loginButton.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString().trim()).matches()
                val isValidPassword =  s.toString().length >= 8
                if(isValidEmail && isValidPassword){
                    binding.loginButton.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.loginButton.setOnClickListener {
            var email = binding.emailEditText.text.toString().trim()
            var password = binding.passwordEditText.text.toString().trim()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_empty)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.pass_empty)
                }
                else -> {
                    viewModel.postLogin(email, password, this)
                    viewModel.loginResponse.observe(this, Observer {
                        if (it == false) {
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
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