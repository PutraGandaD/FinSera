package com.finsera.presentation.fragments.transfer.sesama_bank

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.domain.model.TransferSesama
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferSesamaBankSuksesBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class TransferSesamaBankSuksesFragment : Fragment() {
    private var _binding: FragmentTransferSesamaBankSuksesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferSesamaBankSuksesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataTransferBerhasil = arguments?.getParcelable<TransferSesama>(Constant.TRANSFER_SESAMA_BERHASIL_BUNDLE)?.let {
            binding.cardTransaksiBerhasil.tvDate.text = it.transactionDate
            binding.cardTransaksiBerhasil.tvNominal.text = it.nominal
            binding.cardTransaksiBerhasil.tvBiayaAdmin.text = "Gratis"
            binding.cardTransaksiBerhasil.tvNomorTransaksi.text = it.transactionNum

            binding.cardTransaksiBerhasil.tvBankTujuan.text = "Bank BCA"
            binding.cardTransaksiBerhasil.tvNamaPenerima.text = it.nameRecipient
            binding.cardTransaksiBerhasil.tvRekeningTujuan.text = it.accountnumRecipient
            binding.cardTransaksiBerhasil.tvCatatan.text = it.note

            binding.cardTransaksiBerhasilScreenshot.tvDate.text = it.transactionDate
            binding.cardTransaksiBerhasilScreenshot.tvNominal.text = it.nominal
            binding.cardTransaksiBerhasilScreenshot.tvBiayaAdmin.text = "Gratis"
            binding.cardTransaksiBerhasilScreenshot.tvNomorTransaksi.text = it.transactionNum

            binding.cardTransaksiBerhasilScreenshot.tvBankTujuan.text = "Bank BCA"
            binding.cardTransaksiBerhasilScreenshot.tvNamaPenerima.text = it.nameRecipient
            binding.cardTransaksiBerhasilScreenshot.tvRekeningTujuan.text = it.accountnumRecipient
            binding.cardTransaksiBerhasilScreenshot.tvCatatan.text = it.note
        }

        binding.cardTransaksiBerhasil.btnBackToMenu.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        val captureButton = binding.cardTransaksiBerhasil.btnDownload
        captureButton.setOnClickListener {
            saveToGalleryMode()
            val bitmap = getBitmapFromUiView(requireActivity(), binding.transferSesamaBankBerhasil)
            saveBitmapImage(bitmap)
        }
    }

    fun getBitmapFromUiView(context: Context, myView: View) : Bitmap {
        val bitmap = Bitmap.createBitmap(
            myView.width,
            myView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        myView.draw(canvas)

        return bitmap
    }

    /**Save Bitmap To Gallery
     * @param bitmap The bitmap to be saved in Storage/Gallery*/
    private fun saveBitmapImage(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name))
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = requireActivity().contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e(TAG, "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    requireActivity().contentResolver.update(uri, values, null, null)

                    Snackbar.make(requireView(), "Bukti Transaksi berhasil disimpan di Galeri.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name))
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Snackbar.make(requireView(), "Bukti Transaksi berhasil disimpan di Galeri.", Toast.LENGTH_SHORT).show()
                normalMode()
            } catch (e: Exception) {
                Log.e(TAG, "saveBitmapImage: ", e)
            }
        }
    }

    private fun saveToGalleryMode() {
        binding.transferSesamaBankBerhasil.apply {
            binding.cardTransaksiBerhasil.root.visibility = View.INVISIBLE
            binding.cardTransaksiBerhasilScreenshot.root.visibility = View.VISIBLE
        }
    }

    private fun normalMode() {
        binding.transferSesamaBankBerhasil.apply {
            binding.cardTransaksiBerhasil.root.visibility = View.VISIBLE
            binding.cardTransaksiBerhasilScreenshot.root.visibility = View.INVISIBLE
        }
    }

}