package com.stanserg.jpegresizeapp.presenter.result

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.model.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import com.stanserg.jpegresizeapp.presenter.ViewModelFactory
import com.stanserg.jpegresizeapp.utils.calculateFileSize
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import com.stanserg.jpegresizeapp.utils.compressImage
import com.stanserg.jpegresizeapp.utils.loadImage
import java.io.File

class ResultFragment: Fragment(R.layout.fragment_result) {

    private val viewModel: ResultViewModel by viewModels {
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
            }        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val originalImageView = view.findViewById<ImageView>(R.id.img_original)
        val compressedImageView = view.findViewById<ImageView>(R.id.img_compressed)
        val originalSizeTextView = view.findViewById<TextView>(R.id.text_orig_size)
        val compressedSizeTextView = view.findViewById<TextView>(R.id.text_compres_size)

        val originalUri = arguments?.getParcelable<Uri>(ARG_ORIGINAL_URI)
        val compressedFile = arguments?.getSerializable(ARG_COMPRESSED_FILE) as? File

        if (originalUri != null && compressedFile != null) {
            viewModel.loadImages(originalUri, compressedFile)
        }

        viewModel.originalBitmap.collectWhenStarted(viewLifecycleOwner) { bitmap ->
            bitmap?.let { originalImageView.setImageBitmap(it) }
        }

        viewModel.compressedBitmap.collectWhenStarted(viewLifecycleOwner) { bitmap ->
            bitmap?.let { compressedImageView.setImageBitmap(it) }
        }

        viewModel.fileSizes.collectWhenStarted(viewLifecycleOwner) { sizes ->
            sizes?.let {
                originalSizeTextView.text = it.first
                compressedSizeTextView.text = it.second
            }
        }
    }

    companion object {
        private const val ARG_ORIGINAL_URI = "original_uri"
        private const val ARG_COMPRESSED_FILE = "compressed_file"

        fun newInstance(originalUri: Uri, compressedFile: File): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putParcelable(ARG_ORIGINAL_URI, originalUri)
            args.putSerializable(ARG_COMPRESSED_FILE, compressedFile)
            fragment.arguments = args
            return fragment
        }
    }

}
