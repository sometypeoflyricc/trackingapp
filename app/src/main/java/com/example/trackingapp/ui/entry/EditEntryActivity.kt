package com.example.trackingapp.ui.entry

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingapp.R
import com.example.trackingapp.data.Entry
import com.example.trackingapp.databinding.ActivityEditEntryBinding
import com.example.trackingapp.repo.ServiceLocator
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.common.EntryVMFactory
import java.time.LocalDate

/**
 * Screen for editing an existing entry.
 * Reads fields from Intent extras and applies updates via [EntryViewModel].
 */
class EditEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditEntryBinding

    /** ViewModel for CRUD on an existing [Entry]. */
    private val vm: EntryViewModel by viewModels {
        EntryVMFactory(TrackerRepository(ServiceLocator.db(this)))
    }

    private var entryId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar Up navigation
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Read existing values, fail fast if id is missing
        entryId = intent.getLongExtra("entry_id", -1L)
        if (entryId == -1L) {
            Toast.makeText(this, R.string.missing_entry_id, Toast.LENGTH_SHORT).show()
            finish(); return
        }
        val oldAmount = intent.getIntExtra("amount", 0)
        val oldNote = intent.getStringExtra("note")
        val oldDate = intent.getStringExtra("date")?.let { LocalDate.parse(it) } ?: LocalDate.now()

        // Prefill controls
        binding.etAmount.setText(oldAmount.takeIf { it > 0 }?.toString().orEmpty())
        binding.etNote.setText(oldNote.orEmpty())

        // Save changes
        binding.btnUpdate.setOnClickListener {
            val newAmount = binding.etAmount.text.toString().toIntOrNull()
            if (newAmount == null || newAmount <= 0) {
                Toast.makeText(this, R.string.enter_valid_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newNote = binding.etNote.text.toString().takeIf { it.isNotBlank() }

            vm.update(Entry(id = entryId, date = oldDate, amount = newAmount, note = newNote))

            Toast.makeText(this, R.string.entry_updated, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
