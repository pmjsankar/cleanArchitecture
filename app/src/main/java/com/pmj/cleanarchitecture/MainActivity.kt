package com.pmj.cleanarchitecture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pmj.cleanarchitecture.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Launcher Activity (Entry point) of this application
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}