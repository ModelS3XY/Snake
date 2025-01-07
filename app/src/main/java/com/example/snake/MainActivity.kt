package com.example.snake

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.snake.databinding.ActivityMainBinding
import com.example.snake.viewmodel.Direction
import com.example.snake.viewmodel.GameState
import com.example.snake.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        viewModel.body.observe(this) { it ->
            binding.containView.gameView.body = it
            binding.containView.gameView.invalidate()
        }

        viewModel.apple.observe(this) {
            binding.containView.gameView.apple = it
            binding.containView.gameView.invalidate()
        }

        viewModel.gameState.observe(this) { state ->
            if (state == GameState.GAME_OVER) {
                AlertDialog.Builder(this).setTitle("Game")
                    .setMessage("Game Over, Get ${viewModel.scope.value} apple")
                    .setPositiveButton("OK") { _, _ ->
                        viewModel.reset()
                    }
                    .show()
            }
        }

        viewModel.scope.observe(this) {
            binding.containView.txtCount.text = it.toString()
        }
        viewModel.start()

        binding.containView.top.setOnClickListener {
            viewModel.move(Direction.TOP)
        }
        binding.containView.right.setOnClickListener {
            viewModel.move(Direction.RIGHT)
        }
        binding.containView.down.setOnClickListener {
            viewModel.move(Direction.DOWN)
        }
        binding.containView.left.setOnClickListener {
            viewModel.move(Direction.LEFT)
        }

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }
}