package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.Databases
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyRepository: HistoryRepository

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

        val historyDao = Databases.getInstance(this).historyDao()
        historyRepository = HistoryRepository.getInstance(historyDao)

        binding.saveButton.setOnClickListener {
            saveHistory(imageUri, prediction, confidenceScore)
        }
    }

    private fun saveHistory(imageUri: String?, prediction: String?, confidenceScore: Float) {
        if (imageUri != null && prediction != null) {
            val history = HistoryEntity(
                imageUri = imageUri,
                prediction = prediction,
                confidenceScore = confidenceScore,
                date = Date()
            )
            CoroutineScope(Dispatchers.IO).launch {
                historyRepository.insert(history)
                runOnUiThread {
                    Toast.makeText(this@ResultActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}