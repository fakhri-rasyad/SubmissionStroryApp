package com.d121211017.stroyappsubmission.ui

import android.os.Bundle
import android.os.Message
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

    private fun showToast(response: Pair<Boolean, String>){
        Log.d(TAG, "${response.first}, ${response.second}")
        if(!response.first){
            Toast.makeText(this, response.second, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, response.second, Toast.LENGTH_SHORT).show()
        }
    }
}