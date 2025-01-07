package com.stanserg.jpegresizeapp.presenter.choose

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.stanserg.jpegresizeapp.databinding.ActivityChooseBinding
import com.stanserg.jpegresizeapp.presenter.compress.CompressPhotoActivity
import com.stanserg.jpegresizeapp.utils.EXTRA_PHOTO_FILE_PATH
import com.stanserg.jpegresizeapp.utils.collectWhenStarted

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ChoosePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding
    private val viewModel: ChoosePhotoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        savedInstanceState?.getString(EXTRA_PHOTO_FILE_PATH)?.let { filePath ->
            val file = File(filePath)
            viewModel.handlePhotoSelection(file)
        }


        binding.buttonChoosePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO)
        }

        binding.buttonNext.setOnClickListener {
             viewModel.uiState.value.selectedPhotoFile?.let {
                val intent = Intent(this, CompressPhotoActivity::class.java)
                intent.putExtra(EXTRA_PHOTO_FILE_PATH, it.absolutePath)
                startActivity(intent)
            }
        }

        viewModel.uiState.collectWhenStarted(this) { state ->
            state.selectedPhotoFile?.let { file ->
                Glide.with(this).load(file).into(binding.imagePreview)
            }
            binding.buttonNext.isEnabled = state.isNextEnabled
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val file = convertUriToFile(uri)
                viewModel.handlePhotoSelection(file)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.uiState.value.selectedPhotoFile?.let { file ->
            outState.putString(EXTRA_PHOTO_FILE_PATH, file.absolutePath)
        }
    }


    private fun convertUriToFile(uri: Uri): File {
        viewModel.deleteFilesContainingWord(cacheDir, NAME_PREDICAT)
        val tempFile = File(cacheDir, "$NAME_PREDICAT${System.currentTimeMillis()}.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.use { output ->
            tempFile.outputStream().use { output.copyTo(it) }
        }
        return tempFile
    }

    companion object {
        private const val REQUEST_CODE_PICK_PHOTO = 100
        private const val NAME_PREDICAT = "selected_photo"
    }
}