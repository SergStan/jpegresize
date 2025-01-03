package com.stanserg.jpegresizeapp.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.stanserg.jpegresizeapp.R
import com.stanserg.jpegresizeapp.presenter.choose.ChoosePhotoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         if (savedInstanceState == null) {
            navigateToFragment(ChoosePhotoFragment())
        }
    }

    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }
}