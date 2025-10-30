package com.example.trackingapp.ui.goal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ActivityGoalBinding
import com.example.trackingapp.repo.ServiceLocator
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.common.GoalVMFactory

/**
 * Allows the user to set a positive target against which the dashboard compares.
 */
class GoalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoalBinding

    /** ViewModel for storing the goal value. */
    private val vm: GoalViewModel by viewModels {
        GoalVMFactory(TrackerRepository(ServiceLocator.db(this)))
    }

    /**
     * Wires the toolbar, input field, and Save action.
     *
     * Invalid or non-positive input shows a brief toast and keeps the user on the screen.
     * On success, the goal is saved and the Activity finishes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Save goal
        binding.btnSaveGoal.setOnClickListener {
            val target = binding.etTarget.text.toString().toIntOrNull()
            if (target == null || target <= 0) {
                // Require a positive integer
                Toast.makeText(this, R.string.enter_positive_target, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.saveGoal(target) // Save a single row goal (id = 0)
            finish()
        }
    }
}
