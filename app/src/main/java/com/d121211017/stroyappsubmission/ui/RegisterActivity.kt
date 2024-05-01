package com.d121211017.stroyappsubmission.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.databinding.ActivityRegisterBinding
import com.d121211017.stroyappsubmission.viewmodel.RegisterViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    companion object {
        private const val TAG = "Register Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = getViewModel(this)

        viewModel.isButtonEnabled.observe(this){
            binding.registerButton.isEnabled = it
        }

        viewModel.isEmailValid.observe(this){
            binding.emailInput.error = if(!it) getString(R.string.email_invalid) else null
        }

        viewModel.isPasswordValid.observe(this){
            binding.passwordInput.error = if(!it) getString(R.string.password_invalid) else null
        }

        viewModel.loadingRegistration.observe(this){
            binding.apply {
                if(it){
                    binding.registerButton.visibility = View.INVISIBLE
                    binding.pgBar.visibility = View.VISIBLE
                } else {
                    binding.registerButton.visibility = View.VISIBLE
                    binding.pgBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.registrationSuccess.observe(this){
            showToast(it)
        }

        binding.apply {
            nameInput.addTextChangedListener(
                onTextChanged = {text, _, _, _ ->
                    viewModel.nameValidation(text.toString().trim())
                }
            )

            emailInput.addTextChangedListener(
                afterTextChanged = { editable ->
                    viewModel.emailValidation(editable.toString())
                }
            )
            passwordInput.addTextChangedListener(
                afterTextChanged = { editable ->
                    viewModel.passwordValidation(editable.toString())
                }
            )

            registerButton.setOnClickListener {
                viewModel.postRegistration()
            }
        }
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity) : RegisterViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, factory)[RegisterViewModel::class.java]
    }

    private fun showToast(success : Boolean){
        if(success){
            Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
        }
    }
}