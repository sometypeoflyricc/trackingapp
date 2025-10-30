package com.example.trackingapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ActivityDashboardBinding
import com.example.trackingapp.domain.formatTime
import com.example.trackingapp.repo.ServiceLocator
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.common.DashboardVMFactory
import com.example.trackingapp.ui.entry.EntryActivity
import com.example.trackingapp.ui.entry.EntriesActivity
import com.example.trackingapp.ui.goal.GoalActivity
import kotlinx.coroutines.launch

/**
 * Launcher screen showing weekly progress and quick actions.
 * Handles navigation, unit toggling, and congratulatory toasts when the target is exceeded.
 */
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    /** ViewModel powered by a DB-driven [TrackerRepository]. */
    private val viewModel: DashboardViewModel by viewModels {
        val repo = TrackerRepository(ServiceLocator.db(this))
        DashboardVMFactory(repo)
    }

    /** Ensures the “exceeded target” toast is shown only once per session. */
    private var congratsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // adopt edge-to-edge system bars
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation
        binding.btnLog.setOnClickListener { startActivity(Intent(this, EntryActivity::class.java)) }
        binding.btnGoal.setOnClickListener { startActivity(Intent(this, GoalActivity::class.java)) }
        binding.btnViewEntries.setOnClickListener { startActivity(Intent(this, EntriesActivity::class.java)) }

        // Data reset and unit toggle
        binding.btnReset.setOnClickListener {
            viewModel.resetAll() // clears DB state via repository
            congratsShown = false  // allow toast to show again in the future
            Toast.makeText(this, R.string.all_data_cleared, Toast.LENGTH_SHORT).show()
        }
        binding.btnToggleUnits.setOnClickListener { viewModel.toggleUnits() }

        // Secure monitoring of UI status, taking into account the life cycle
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Progress bar expects 0..100, already clamped in ViewModel
                    binding.progress.setProgressCompat(state.percent, true)

                    // Build summary text
                    val totalFormatted = formatTime(state.total, state.showHours)
                    val targetFormatted = formatTime(state.target, state.showHours)
                    val extra = if (state.exceededPercent > 0) " (+${state.exceededPercent}%)" else ""
                    binding.summary.text = "${state.percent}% ($totalFormatted / $targetFormatted)$extra"

                    // One-time toast with congratulations when the goal is exceeded
                    if (state.achieved && state.exceededPercent > 0 && !congratsShown) {
                        congratsShown = true
                        Toast.makeText(
                            this@DashboardActivity,
                            getString(R.string.congrats_exceeded, state.exceededPercent),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // Empty state when no goal is set, hide progress/summary
                    val showEmpty = state.target <= 0
                    binding.emptyState.visibility = if (showEmpty) View.VISIBLE else View.GONE
                    binding.progress.visibility = if (showEmpty) View.GONE else View.VISIBLE
                    binding.summary.visibility = if (showEmpty) View.GONE else View.VISIBLE
                }
            }
        }
    }
}
