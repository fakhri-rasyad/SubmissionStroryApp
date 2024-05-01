package com.d121211017.stroyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.databinding.ActivityLoginBinding
import com.d121211017.stroyappsubmission.viewmodel.LoginViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = getViewModel(this)
        setContentView(binding.root)

        viewModel.isButtonEnabled.observe(this){
            binding.loginButton.isEnabled = it
        }

        viewModel.isLoadingLogin.observe(this){
            binding.apply {
                if(it){
                    loginButton.visibility = View.INVISIBLE
                    pgBar.visibility = View.VISIBLE
                } else {
                    loginButton.visibility = View.VISIBLE
                    pgBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.errorResponse.observe(this){
            if(it.second == "Invalid password"){
                binding.passwordInput.error = "Invalid password"
            } else if (it.second == "User not found"){
                binding.emailInput.error = "User not found"
            }
        }

        viewModel.isLoginSuccess.observe(this){
            if(it){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.apply {
            emailInput.addTextChangedListener (
                afterTextChanged = {editable ->
                    viewModel.emailValidation(editable.toString())
                }
            )

            passwordInput.addTextChangedListener(
                afterTextChanged = {editable ->
                    viewModel.passwordValidation(editable.toString())
                }
            )

            loginButton.setOnClickListener {
                viewModel.postLogin()
            }
        }
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity) : LoginViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, factory)[LoginViewModel::class.java]
    }
}