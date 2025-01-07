package com.example.snake.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.fixedRateTimer

class GameViewModel: ViewModel() {
    val body = MutableLiveData<List<Position>>()
    var gameState = MutableLiveData<GameState>()
    var apple = MutableLiveData<Position>()
    var scope = MutableLiveData<Int>()
    var point = 0
    private lateinit var applePosition: Position
    private var snakeBody = mutableListOf<Position>()
    private var direction = Direction.LEFT

    fun start() {
        scope.postValue(point)
        snakeBody.apply {
            add(Position(10, 10))
            add(Position(11, 10))
            add(Position(12, 10))
            add(Position(13, 10))
        }.also {
            body.value = it
        }
        generateApple()
        fixedRateTimer("timer1", true, 1000, 200) {
            val newPos = snakeBody.first().copy().apply {
                when(direction) {
                    Direction.LEFT -> x--
                    Direction.RIGHT -> x++
                    Direction.TOP -> y--
                    else -> y++
                }
                if (snakeBody.contains(this) || x < 0 || x >= 20 || y < 0 || y >= 20) {
                    cancel()
                    gameState.postValue(GameState.GAME_OVER)
                }
            }
            snakeBody.add(0, newPos)
            if (newPos != applePosition) {
                snakeBody.removeAt(snakeBody.lastIndex)
            } else {
                point++
                scope.postValue(point)
                generateApple()
            }
            body.postValue(snakeBody)
        }
    }

    private fun generateApple() {
        //先取到全部位置
        val otherPosition = mutableListOf<Position>().apply {
            for (i in 0..19) {
                for (j in 0..19) {
                    add(Position(i, j))
                }
            }
        }
        //移除目前snake身上的位置
        otherPosition.removeAll(snakeBody)
        //打亂取第一個，就不會將Apple位置產生在Snake身上
        otherPosition.shuffle()
        applePosition = otherPosition.first()
        apple.postValue(applePosition)
    }

    fun reset() {
        snakeBody.clear()
        direction = Direction.LEFT
        point = 0
        start()
    }

    fun move(dire: Direction) {
        //限制不能同時上下or左右
        when(dire) {
            Direction.TOP -> if (direction != Direction.DOWN) direction = dire
            Direction.DOWN -> if (direction != Direction.TOP) direction = dire
            Direction.LEFT -> if (direction != Direction.RIGHT) direction = dire
            Direction.RIGHT -> if (direction != Direction.LEFT) direction = dire
        }
    }
}

data class Position(var x: Int, var y: Int)

enum class Direction {
    TOP, LEFT, DOWN, RIGHT
}

enum class GameState {
    ONGOING, GAME_OVER
}