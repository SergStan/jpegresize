package com.stanserg.jpegresizeapp.presenter.compress

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.presenter.MainActivity
import com.stanserg.jpegresizeapp.presenter.result.ResultFragment
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompressPhotoFragment : Fragment(R.layout.fragment_compress) {

    private val viewModel: CompressPhotoViewModel by viewModel()

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

        viewModel.uiState.collectWhenStarted(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                infoTextView.text = "Загрузка..."
            } else {
                state.previewBitmap?.let { imagePreview.setImageBitmap(it) }
                infoTextView.text = state.fileSizeInfo
            }

            state.errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
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