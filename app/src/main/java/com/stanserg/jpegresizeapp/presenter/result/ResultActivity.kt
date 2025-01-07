package com.stanserg.jpegresizeapp.presenter.result

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.stanserg.jpegresizeapp.databinding.ActivityResultBinding
import com.stanserg.jpegresizeapp.utils.EXTRA_COMPRESSED_FILE
import com.stanserg.jpegresizeapp.utils.EXTRA_COMPRESSED_FILE_PATH
import com.stanserg.jpegresizeapp.utils.EXTRA_ORIGINAL_FILE
import com.stanserg.jpegresizeapp.utils.EXTRA_ORIGINAL_FILE_PATH
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val originalFilePath = intent.getStringExtra(EXTRA_ORIGINAL_FILE)?: savedInstanceState?.getString(EXTRA_ORIGINAL_FILE_PATH)

        val compressedFilePath = intent.getStringExtra(EXTRA_COMPRESSED_FILE)?: savedInstanceState?.getString(EXTRA_COMPRESSED_FILE_PATH)


        val originalFile = File(originalFilePath ?: "")
        val compressedFile = File(compressedFilePath ?: "")
        if (originalFile != null && compressedFile != null) {
            viewModel.loadImages(originalFile, compressedFile)
        }
        viewModel.uiState.collectWhenStarted(this) { state ->
            if (state.isLoading) {
                binding.textOrigSize.text = "Загрузка..."
                binding.textCompresSize.text = "Загрузка..."
            } else {
                Glide.with(this)
                    .load(state.originalBitmap)
                    .into(binding.imgOriginal)

                Glide.with(this)
                    .load(state.compressedBitmap)
                    .into(binding.imgCompressed)

                binding.textOrigSize.text = state.originalSizeText
                binding.textCompresSize.text = state.compressedSizeText
            }

            state.errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            EXTRA_ORIGINAL_FILE_PATH,
            intent.getStringExtra(EXTRA_ORIGINAL_FILE_PATH)
        )
        outState.putString(
            EXTRA_COMPRESSED_FILE_PATH,
            intent.getStringExtra(EXTRA_COMPRESSED_FILE_PATH)
        )
    }
}