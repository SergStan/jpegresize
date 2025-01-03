package com.stanserg.jpegresizeapp.presenter.choose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.presenter.MainActivity
import com.stanserg.jpegresizeapp.presenter.compress.CompressPhotoFragment
import com.stanserg.jpegresizeapp.utils.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel
class ChoosePhotoFragment : Fragment(R.layout.fragment_choose) {

    private val viewModel: ChoosePhotoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val choosePhotoButton = view.findViewById<Button>(R.id.button_choose_photo)
        val nextButton = view.findViewById<Button>(R.id.button_next)
        val imagePreview = view.findViewById<ImageView>(R.id.image_preview)

        choosePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO)
        }

        viewModel.uiState.collectWhenStarted(viewLifecycleOwner) { state ->
            imagePreview.setImageURI(state.selectedPhotoUri)
            nextButton.isEnabled = state.isNextEnabled
        }

        nextButton.setOnClickListener {
            viewModel.uiState.value.selectedPhotoUri?.let { uri ->
                (activity as MainActivity).navigateToFragment(
                    CompressPhotoFragment.newInstance(uri)
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.handlePhotoSelection(data?.data)
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK_PHOTO = 100
    }
}