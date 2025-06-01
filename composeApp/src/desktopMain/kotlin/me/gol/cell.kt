package me.gol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlin.random.Random.Default.nextInt


/**
 * Stores the state and future state of a cell.
 */
class Cell {
    var alive = mutableStateOf(false)
    var nextState = mutableStateOf(false)
}

/**
 * Stores cells in a 32 by 32 list and implements functions in this matrix.
 */
class CellMatrix {

    var cellList = List(32) { List(32) { Cell() } }

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    var isActive = mutableStateOf(false)

    /**
     * Assigns to each cell:
     * alive = true with about a 20% chance,
     * alive = false otherwise.
     */
    fun randomize() {
        cellList.forEach { cellArray ->
            cellArray.forEach { cell ->
                cell.alive.value = nextInt(99) < 20
            }
        }
    }

    /**
     * Stops the simulation if it's ongoing.
     * Sets each cell to alive = false.
     */
    fun reset() {
        stop()
        cellList.forEach { cellArray ->
            cellArray.forEach { cell ->
                cell.alive.value = false
            }
        }
    }

    /**
     * Returns the number of alive neighbours a Cell (given by indices) has.
     */
    private fun aliveNeighbours(x : Int, y : Int) : Int {
        if (x >= 32 || y >= 32 || x < 0 || y < 0) throw IndexOutOfBoundsException()  // this should not happen
        var count = 0

        var northern = x - 1; var southern = x + 1; var western = y - 1; var eastern = y + 1

        if (northern < 0) northern = 31                      // out of bounds checks
        if (southern == 32) southern = 0
        if (western < 0) western = 31
        if (eastern == 32) eastern = 0

        if (cellList[northern][y].alive.value) count++              // northern neighbour
        if (cellList[southern][y].alive.value) count++              // southern neighbour
        if (cellList[x][western].alive.value) count++               // western ..
        if (cellList[x][eastern].alive.value) count++               // eastern ..
        if (cellList[northern][western].alive.value) count++        // north-western ..
        if (cellList[northern][eastern].alive.value) count++        // north-eastern ..
        if (cellList[southern][western].alive.value) count++        // south-western ..
        if (cellList[southern][eastern].alive.value) count++        // south-eastern ..

        return count
    }

    /**
     * Updates the state of the matrix.
     * While the check is under way only the nextSate property is changed,
     * so that the matrix stays consistent while the remaining cells are checked.
     */
    private fun update() {
        cellList.forEachIndexed { row, cellRow -> cellRow.forEachIndexed { column, cell ->
            val n = aliveNeighbours(row, column)
            if (cell.alive.value) {                                           // logic for live cells
                when (n) {
                    0, 1, 4, 5, 6, 7, 8 -> cell.nextState.value = false       // over-/underpopulation
                    2, 3 -> cell.nextState.value = true                       // survives
                }
            } else cell.nextState.value = (n == 3)                // logic for dead cell, either reproduces or stays dead
        }
        }
        // actual update happens here using the previously updated property of the Cell class
        cellList.forEach { it.forEach { cell -> cell.alive.value = cell.nextState.value } }
    }

    /**
     * If it's not already active starts the simulation in a coroutineScope.
     */
    fun start() {
        if (isActive.value) { return }
        coroutineScope.launch {
            this@CellMatrix.isActive.value = true
            while (isActive) {
                delay(100)
                update()
            }
        }
    }

    /**
     * Stops the simulation.
     */
    fun stop() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        isActive.value = false
    }

}

/**
 * Returns what color a cell should have based on whether it's alive or not.
 */
@Composable
fun Cell.color() : ButtonColors {
    if (alive.value) { return ButtonDefaults.buttonColors() }
    return ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
}

/**
 * Creates a button for each cell in the matrix.
 */
@Composable
fun CellMatrix.toButton() {
    cellList.forEach { cellRow ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            cellRow.forEach { cell ->
                Button(
                    onClick = { cell.alive.value = !cell.alive.value },
                    modifier = Modifier.size(16.dp),
                    shape = RectangleShape,
                    colors = cell.color(),
                    contentPadding = PaddingValues(0.dp),
                ) {}
            }
        }
    }
}

/**
 * Returns what color the start button should have based on whether it's active or not.
 */
@Composable
fun CellMatrix.startColor() : ButtonColors {
    if (isActive.value) { return ButtonDefaults.buttonColors(backgroundColor = Color.LightGray) }
    return ButtonDefaults.buttonColors()
}
