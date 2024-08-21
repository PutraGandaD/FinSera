package com.finsera.presentation.fragments.qris

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Base64
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.presentation.fragments.qris.viewmodel.QrisScanQRViewModel
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.finsera.common.utils.Constant.Companion.NAMA_KOTA_MERCHANT_QRIS
import com.finsera.common.utils.Constant.Companion.NAMA_MERCHANT_QRIS
import com.finsera.common.utils.Constant.Companion.NOMOR_TRX_MERCHANT_QRIS
import com.finsera.common.utils.permission.HandlePermission.openAppPermissionSettings
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentCekRekeningAntarBankFormBinding
import com.finsera.presentation.databinding.FragmentQrisScanQRBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.phanna.emv_qr_code.MerchantPresentedDecoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrisScanQRFragment : Fragment() {
    private var _binding: FragmentQrisScanQRBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private val qrisScanQRViewModel : QrisScanQRViewModel by inject()

    private var noRekening = ""

    private lateinit var vibrator: Vibrator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrisScanQRBinding.inflate(inflater, container, false)
        cameraExecutor = Executors.newSingleThreadExecutor()
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestCameraPermission()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewViewCamera.surfaceProvider)
                }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(Size(1280, 720))
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(imageProxy: ImageProxy) {
        val img = imageProxy.image
        if (img != null) {
            val inputImage = InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    processBarcode(barcodes, scanner)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnCompleteListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(3000)
                        imageProxy.close()
                    }
                }
        }
    }

    private fun processBarcode(barcodeList: List<Barcode>, scanner: BarcodeScanner) {
        if (barcodeList.isNotEmpty()) {
            with (barcodeList.first()) { // take only first item from the barcodeList
                val rawValue = this.rawValue.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val jsonObject = JSONObject(rawValue)
                        val accountNum = jsonObject.getString("accountNumber")
                        withContext(Dispatchers.Main) {
                            if(!accountNum.isNullOrEmpty()) {
                                noRekening = accountNum
                                qrisScanQRViewModel.cekRekeningSesama(noRekening)
                            }
                        }
                    } catch(e: JSONException) { // catch if qr code isn't json
                        val merchantName = MerchantPresentedDecoder.decode(rawValue, false).merchantName.replace("+", " ")
                        val merchantCity = MerchantPresentedDecoder.decode(rawValue, false).merchantCity.replace("+", " ")
                        val merchantAccountNo = MerchantPresentedDecoder.decode(rawValue, false).merchantAccountInformation.replace("+", " ")

                        withContext(Dispatchers.Main) {
                            if(!merchantName.isNullOrEmpty()) {
                                startVibration()
                                processQrisMerchant(merchantName, merchantCity, merchantAccountNo)
                            }
                        }
                    } catch(e: Exception) {
                        Log.d("Exception", e.message.toString())
                    } catch(t: Throwable) {
                        Log.d("Throwable", t.message.toString())
                    } finally {
                        scanner.close()
                    }
                }
            }
        }
    }

    private fun processQrisMerchant(merchantName: String, merchantCity: String, merchantAccountNo: String) {
        if(findNavController().currentDestination?.id == R.id.qrisScanQRFragment) {
            Toast.makeText(requireActivity(), "Merchant QRIS Ditemukan", Toast.LENGTH_SHORT)
                .show()
            val bundle = Bundle().apply {
                putString(NAMA_MERCHANT_QRIS, merchantName)
                putString(NAMA_KOTA_MERCHANT_QRIS, merchantCity)
                putString(NOMOR_TRX_MERCHANT_QRIS, merchantAccountNo)
            }
            findNavController().navigate(R.id.action_qrisScanQRFragment_to_transferQrisMerchantFormFragment, bundle)
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                qrisScanQRViewModel.qrisScanQRUiState.collectLatest { uiState ->
                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        qrisScanQRViewModel.messageShown()
                    }

                    if(uiState.isValidFinsera) {
                        Toast.makeText(requireActivity(), "Rekening Ditemukan", Toast.LENGTH_LONG).show()
                        if(findNavController().currentDestination?.id == R.id.qrisScanQRFragment) {
                            val dataRekening = CekRekeningSesamaBundle(uiState.dataRekeningFinsera?.recipientName!!, uiState.dataRekeningFinsera?.accountnumRecipient!!)
                            val bundle = Bundle().apply {
                                putParcelable(Constant.DATA_REKENING_SESAMA_BUNDLE, dataRekening)
                            }

                            findNavController().navigate(R.id.action_qrisScanQRFragment_to_transferSesamaBankFormFragment, bundle)
                            qrisScanQRViewModel.resetUiState()
                        }
                    }

                    if(uiState.isLoading) {
                        Toast.makeText(requireActivity(), "QRIS Ditemukan. Sedang memproses...", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.VISIBLE
                        startVibration()
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                permissionCameraDialog()
            }

            else -> {
                // Request the permission
                requestPermissionCameraResult.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermissionCameraResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            // Explain to the user that the feature is unavailable because the
            // features require a permission that the user has denied.
            permissionCameraDialog()
        }
    }

    private fun permissionCameraDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Izin Aplikasi FinSera")
            .setMessage(resources.getString(R.string.izin_kamera_aplikasi_finsera_desc))
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                Snackbar.make(requireView(), "Fitur tidak dapat dijalankan karena izin kamera pada aplikasi FinSera tidak diizinkan", Snackbar.LENGTH_SHORT).show()
            }
            .setPositiveButton("Ya") { dialog, which ->
                requireActivity().openAppPermissionSettings()
            }
            .show()
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
