package com.example.trackingapp.ui.entry

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ActivityEntryBinding
import com.example.trackingapp.repo.ServiceLocator
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.common.EntryVMFactory
import java.time.LocalDate

/**
 * “Add entry” screen: captures amount and note for today.
 * Validates input and commits via [EntryViewModel].
 */
class EntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryBinding

    /** ViewModel for creating entries. */
    private val vm: EntryViewModel by viewModels {
        EntryVMFactory(TrackerRepository(ServiceLocator.db(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar Up navigation
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Save action. basic validation, and delegation to VM
        binding.btnSave.setOnClickListener {
            val amount = binding.etAmount.text.toString().toIntOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, R.string.enter_positive_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val note = binding.etNote.text?.toString()?.takeIf { it.isNotBlank() }
            vm.add(LocalDate.now(), amount, note)
            // Close this screen and return to Dashboard
            finish()
        }
    }
}
