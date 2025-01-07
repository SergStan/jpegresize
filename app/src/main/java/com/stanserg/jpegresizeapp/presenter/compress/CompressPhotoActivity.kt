package com.stanserg.jpegresizeapp.presenter.compress

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.stanserg.jpegresizeapp.databinding.ActivityCompressBinding
import com.stanserg.jpegresizeapp.presenter.result.ResultActivity
import com.stanserg.jpegresizeapp.utils.EXTRA_COMPRESSED_FILE
import com.stanserg.jpegresizeapp.utils.EXTRA_COMPRESSION_LEVEL
import com.stanserg.jpegresizeapp.utils.EXTRA_ORIGINAL_FILE
import com.stanserg.jpegresizeapp.utils.EXTRA_ORIGINAL_FILE_PATH
import com.stanserg.jpegresizeapp.utils.EXTRA_PHOTO_FILE_PATH
import com.stanserg.jpegresizeapp.utils.INIT_COMPRESSION_LEVEL
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CompressPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompressBinding
    private val viewModel: CompressPhotoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val originalFilePath = intent.getStringExtra(EXTRA_PHOTO_FILE_PATH)?: savedInstanceState?.getString(EXTRA_ORIGINAL_FILE_PATH)

        val compressionLevel = savedInstanceState?.getInt(EXTRA_COMPRESSION_LEVEL, INIT_COMPRESSION_LEVEL) ?: INIT_COMPRESSION_LEVEL

        val photoFile = File(originalFilePath ?: "")
        viewModel.setOriginalUri(photoFile, compressionLevel)

        binding.textInfoOrigin.text = "Размер оригинала: ${photoFile.length()/1024} KB"
        with (binding.sliderCompression){
            setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    viewModel.setCompressionLevel(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        binding.buttonNext.setOnClickListener {
            val compressedFile = viewModel.getCompressedFile()
            compressedFile?.let {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(EXTRA_ORIGINAL_FILE, photoFile.absolutePath)
                intent.putExtra(EXTRA_COMPRESSED_FILE, it.absolutePath)
                startActivity(intent)
            }
        }

        viewModel.uiState.collectWhenStarted(this) { state ->
            if (state.isLoading) {
                binding.textInfo.text = "Загрузка..."
            } else {
                Glide.with(this)
                    .load(state.previewFile)
                    .into(binding.imagePreview)
                binding.textInfo.text = "Размер сжатого файла: ${state.fileSizeInfo / 1024} KB"
                binding.textInfoCompare.text = "Размер сжатого меньше на: ${(photoFile.length() - state.fileSizeInfo)/1024} KB"
            }

            state.errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.uiState.value.previewFile?.let { file ->
            outState.putString(EXTRA_ORIGINAL_FILE_PATH, file.absolutePath)
        }
        outState.putInt(EXTRA_COMPRESSION_LEVEL, binding.sliderCompression.progress)
    }
}