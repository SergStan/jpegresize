package com.stanserg.jpegresizeapp.presenter.result

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel
class ResultFragment : Fragment(R.layout.fragment_result) {

    private val viewModel: ResultViewModel by viewModel()

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

        viewModel.uiState.collectWhenStarted(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                originalSizeTextView.text = "Загрузка..."
                compressedSizeTextView.text = "Загрузка..."
            } else {
                originalImageView.setImageBitmap(state.originalBitmap)
                compressedImageView.setImageBitmap(state.compressedBitmap)
                originalSizeTextView.text = state.originalSizeText
                compressedSizeTextView.text = state.compressedSizeText
            }

            state.errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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