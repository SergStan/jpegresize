package com.stanserg.jpegresizeapp.presenter.compress

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.model.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import com.stanserg.jpegresizeapp.presenter.MainActivity
import com.stanserg.jpegresizeapp.presenter.ViewModelFactory
import com.stanserg.jpegresizeapp.presenter.result.ResultFragment
import com.stanserg.jpegresizeapp.utils.calculateFileSize
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import com.stanserg.jpegresizeapp.utils.compressImage
import com.stanserg.jpegresizeapp.utils.loadImage
import java.io.File

class CompressPhotoFragment : Fragment(R.layout.fragment_compress) {

    private val viewModel: CompressPhotoViewModel by viewModels {
        ViewModelFactory(
            compressPhotoUseCase = CompressPhotoUseCase { uri, quality ->
                compressImage(
                    inputStream = activity?.contentResolver?.openInputStream(uri)!!,
                    quality = quality,
                    tempFileProvider = { File.createTempFile("compressed_", ".jpg", File("/path/to/cache")) }
                )
            },
            loadImageUseCase = LoadImageUseCase { source ->
                when (source) {
                    is Uri -> loadImage(activity?.contentResolver?.openInputStream(source)!!)
                    is File -> loadImage(source.inputStream())
                    else -> throw IllegalArgumentException("Unsupported source type")
                }
            },
            calculateFileSizeUseCase = CalculateFileSizeUseCase { source ->
                when (source) {
                    is Uri -> {
                        val cursor =
                            activity?.contentResolver?.query(source, null, null, null, null)
                        val sizeIndex =
                            cursor?.getColumnIndex(android.provider.OpenableColumns.SIZE) ?: -1
                        val size = if (cursor?.moveToFirst() == true && sizeIndex != -1) {
                            cursor.getLong(sizeIndex)
                        } else {
                            0L
                        }
                        cursor?.close()
                        size
                    }

                    is File -> calculateFileSize(source)
                    else -> throw IllegalArgumentException("Unsupported source type")
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagePreview = view.findViewById<ImageView>(R.id.image_preview)
        val infoTextView = view.findViewById<TextView>(R.id.text_info)
        val slider = view.findViewById<SeekBar>(R.id.slider_compression)
        val nextButton = view.findViewById<Button>(R.id.button_next)

        val uri = arguments?.getParcelable<Uri>(ARG_PHOTO_URI)
        uri?.let { viewModel.setOriginalUri(it) }

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.setCompressionLevel(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewModel.previewBitmap.collectWhenStarted(viewLifecycleOwner) { bitmap ->
            bitmap?.let { imagePreview.setImageBitmap(it) }
        }

        viewModel.fileSizeInfo.collectWhenStarted(viewLifecycleOwner) { info ->
            infoTextView.text = info
        }

        nextButton.setOnClickListener {
            val compressedFile = viewModel.getCompressedFile()
            compressedFile?.let {
                (activity as MainActivity).navigateToFragment(
                    ResultFragment.newInstance(uri!!, it)
                )
            }
        }
    }

    companion object {
        private const val ARG_PHOTO_URI = "photo_uri"

        fun newInstance(uri: Uri): CompressPhotoFragment {
            val fragment = CompressPhotoFragment()
            val args = Bundle()
            args.putParcelable(ARG_PHOTO_URI, uri)
            fragment.arguments = args
            return fragment
        }
    }

}