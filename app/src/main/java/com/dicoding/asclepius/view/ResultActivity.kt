package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prediction = intent.getStringExtra("PREDICTION")
        val confidenceScore = intent.getFloatExtra("CONFIDENCE_SCORE", 0.0f)
        val confidenceScoreInPercent = (confidenceScore * 100).toInt()
        val imageUri = intent.getStringExtra("IMAGE_URI")

        binding.resultText.text = getString(R.string.result_text, prediction, confidenceScoreInPercent)
        binding.resultImage.setImageURI(Uri.parse(imageUri))
    }
}