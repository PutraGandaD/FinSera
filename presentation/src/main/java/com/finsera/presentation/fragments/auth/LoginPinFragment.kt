package com.finsera.presentation.fragments.auth

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.DisableTouchEvent
import com.finsera.common.utils.DisableTouchEvent.setInteractionDisabled
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentLoginPinBinding
import com.finsera.presentation.fragments.auth.viewmodels.LoginPinViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.util.concurrent.Executor

class LoginPinFragment : Fragment() {
    private var _binding: FragmentLoginPinBinding? = null
    private val binding get() = _binding!!

    private val loginPinViewModel : LoginPinViewModel by inject()

    private lateinit var etPin1 : EditText
    private lateinit var etPin2 : EditText
    private lateinit var etPin3 : EditText
    private lateinit var etPin4 : EditText
    private lateinit var etPin5 : EditText
    private lateinit var etPin6 : EditText

    private lateinit var currentFocusEditText : EditText
    private lateinit var prevFilledEditText : EditText

    private var hasAnnouncedScreen = false

    // fingerprint
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // vibrator
    private lateinit var vibrator: Vibrator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPinBinding.inflate(inflater, container, false)
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initFingerprint()
        handleCustomKeyboard()
        observe()
        attachTextWatcher()

        if(isBiometricSupported()) {
            biometricPrompt.authenticate(promptInfo)
            binding.btnFingerprint.visibility = View.VISIBLE
        } else {
            binding.btnFingerprint.visibility = View.INVISIBLE
        }

        binding.btnGantiAkun.setOnClickListener {
            gantiAkun()
        }

        binding.btnLupaPin.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.loginPinFragment) {
                findNavController().navigate(R.id.action_loginPinFragment_to_forgetAppPinFragment)
            }
        }

        if(!hasAnnouncedScreen) {
            view.announceForAccessibility("Login dengan PIN Aplikasi atau Sidik jari")
            hasAnnouncedScreen = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFingerprint() {
        executor = ContextCompat.getMainExecutor(requireActivity())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    when(errorCode) {
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                            Toast.makeText(requireActivity(),
                                "Silahkan Login dengan PIN Aplikasi anda.", Toast.LENGTH_SHORT)
                                .show()
                        }

                        BiometricPrompt.ERROR_USER_CANCELED -> {
                            Toast.makeText(requireActivity(),
                                "Silahkan Login dengan PIN Aplikasi anda.", Toast.LENGTH_SHORT)
                                .show()
                        }

                        BiometricPrompt.ERROR_CANCELED -> {
                            Toast.makeText(requireActivity(),
                                "Silahkan Login dengan PIN Aplikasi anda.", Toast.LENGTH_SHORT)
                                .show()
                        }

                        BiometricPrompt.ERROR_LOCKOUT -> {
                            Toast.makeText(requireActivity(),
                                "Terlalu banyak upaya percobaan sidik jari. Silahkan login dengan PIN Aplikasi.", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            Toast.makeText(requireActivity(),
                                "Authentication error: $errString", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    startVibration()
                    loginPinViewModel.loginWithFingerprint()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireActivity(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login dengan Sidik Jari Aplikasi FinSera")
            .setSubtitle("Tempelkan jari anda ke sensor fingerprint di Handphone anda untuk login ke aplikasi FinSera.")
            .setNegativeButtonText("Gunakan PIN Aplikasi")
            .build()
        
    }

    private fun gantiAkun() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Ganti Akun")
            .setMessage(getString(R.string.finsera_app_ganti_akun_desc))
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(requireActivity(), "Anda membatalkan untuk ganti akun.", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("Ya") { dialog, which ->
                loginPinViewModel.logoutFromAccount()
                Toast.makeText(requireActivity(), "Logout berhasil. Silahkan login kembali.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginPinFragment_to_loginFragment)
            }
            .show()
    }

    private fun init() {
        etPin1 = binding.etPin1
        etPin2 = binding.etPin2
        etPin3 = binding.etPin3
        etPin4 = binding.etPin4
        etPin5 = binding.etPin5
        etPin6 = binding.etPin6

        currentFocusEditText = etPin1
        prevFilledEditText = etPin1
    }

    private fun attachTextWatcher() {
        etPin1.addTextWatcher()
        etPin2.addTextWatcher()
        etPin3.addTextWatcher()
        etPin4.addTextWatcher()
        etPin5.addTextWatcher()
        etPin6.addTextWatcher()
    }

    private fun handleCustomKeyboard() {
        binding.btnPin0.setOnClickListener {
            currentFocusEditText.setText("0")
        }
        binding.btnPin1.setOnClickListener {
            currentFocusEditText.setText("1")
        }
        binding.btnPin2.setOnClickListener {
            currentFocusEditText.setText("2")
        }
        binding.btnPin3.setOnClickListener {
            currentFocusEditText.setText("3")
        }
        binding.btnPin4.setOnClickListener {
            currentFocusEditText.setText("4")
        }
        binding.btnPin5.setOnClickListener {
            currentFocusEditText.setText("5")
        }
        binding.btnPin6.setOnClickListener {
            currentFocusEditText.setText("6")
        }
        binding.btnPin7.setOnClickListener {
            currentFocusEditText.setText("7")
        }
        binding.btnPin8.setOnClickListener {
            currentFocusEditText.setText("8")
        }
        binding.btnPin9.setOnClickListener {
            currentFocusEditText.setText("9")
        }

        binding.btnFingerprint.setOnClickListener {
            it.announceForAccessibility(binding.btnFingerprint.contentDescription)
            if(isBiometricSupported()) {
                biometricPrompt.authenticate(promptInfo)
            }
        }

        binding.btnDeletePin.setOnClickListener {
            prevFilledEditText.setText("")
            it.announceForAccessibility("Hapus PIN")
        }
    }

    private fun EditText.addTextWatcher() {
        this.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    this@addTextWatcher.announceForAccessibility(s.toString()) // trigger talkback
                    when (this@addTextWatcher) {
                        etPin1 -> handleInput(etPin1, etPin1, etPin2, binding.ivPin1Filled, binding.ivPin1Unfilled)
                        etPin2 -> handleInput(etPin1, etPin2, etPin3, binding.ivPin2Filled, binding.ivPin2Unfilled)
                        etPin3 -> handleInput(etPin2, etPin3, etPin4, binding.ivPin3Filled, binding.ivPin3Unfilled)
                        etPin4 -> handleInput(etPin3, etPin4, etPin5, binding.ivPin4Filled, binding.ivPin4Unfilled)
                        etPin5 -> handleInput(etPin4, etPin5, etPin6, binding.ivPin5Filled, binding.ivPin5Unfilled)
                        etPin6 -> handleLastInput(etPin6, binding.ivPin6Filled, binding.ivPin6Unfilled)
                    }
                }
            }
        )
    }

    private fun handleInput(prev: EditText, current: EditText, next: EditText, filled: View, unfilled: View) {
        if (current.text.isNotEmpty()) {
            prevFilledEditText = current
            unfilled.visibility = View.INVISIBLE
            filled.visibility = View.VISIBLE
            currentFocusEditText = next
        }

        if(current.text.isEmpty()) {
            prevFilledEditText = prev
            unfilled.visibility = View.VISIBLE
            filled.visibility = View.INVISIBLE
            currentFocusEditText = current
        }
    }

    private fun handleLastInput(current: EditText, filled: View, unfilled: View) {
        if (current.text.toString().isNotEmpty()) {
            unfilled.visibility = View.INVISIBLE
            filled.visibility = View.VISIBLE
            val getPin = etPin1.text.toString() + etPin2.text.toString() + etPin3.text.toString() +
                    etPin4.text.toString() + etPin5.text.toString() + etPin6.text.toString()
            loginPinViewModel.loginWithMpin(getPin)
        }
    }

    private fun resetPinFields() {
        etPin1.setText(null)
        etPin2.setText(null)
        etPin3.setText(null)
        etPin4.setText(null)
        etPin5.setText(null)
        etPin6.setText(null)
        binding.ivPin1Unfilled.visibility = View.VISIBLE
        binding.ivPin1Filled.visibility = View.INVISIBLE
        binding.ivPin2Unfilled.visibility = View.VISIBLE
        binding.ivPin2Filled.visibility = View.INVISIBLE
        binding.ivPin3Unfilled.visibility = View.VISIBLE
        binding.ivPin3Filled.visibility = View.INVISIBLE
        binding.ivPin4Unfilled.visibility = View.VISIBLE
        binding.ivPin4Filled.visibility = View.INVISIBLE
        binding.ivPin5Unfilled.visibility = View.VISIBLE
        binding.ivPin5Filled.visibility = View.INVISIBLE
        binding.ivPin6Unfilled.visibility = View.VISIBLE
        binding.ivPin6Filled.visibility = View.INVISIBLE
        currentFocusEditText = etPin1
        prevFilledEditText = etPin1
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginPinViewModel.loginPinScreenUIState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                        loginPinViewModel.userMessageShown()
                    }

                    if(uiState.isPinCorrect) {
                        if(findNavController().currentDestination?.id == R.id.loginPinFragment) {
                            Toast.makeText(requireActivity(), "Berhasil Login", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_loginPinFragment_to_homeFragment)
                        }
                    } else {
                        resetPinFields()
                    }

                    if(uiState.isLoading) {
                        binding.tvLoginStatus.text = "Sedang mengautentikasi..."
                        view?.announceForAccessibility(binding.tvLoginStatus.text)
                        setInteractionDisabled(requireActivity(), true)
                    } else {
                        binding.tvLoginStatus.text = "Selamat Datang Kembali"
                        setInteractionDisabled(requireActivity(), false)
                    }
                }
            }
        }
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(requireActivity())
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // The user can authenticate with biometrics, continue with the authentication process
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Handle the error cases as needed in your app
                return false
            }

            else -> {
                // Biometric status unknown or another error occurred
                return false
            }
        }
    }

    private fun startVibration() {
        val vibrationDuration = 1000L

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(vibrationDuration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(vibrationDuration)
        }
    }

}
