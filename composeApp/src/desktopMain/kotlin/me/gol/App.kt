package me.gol

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {

    val cellMatrix = remember { CellMatrix() }

    MaterialTheme {

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            cellMatrix.toButton()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { cellMatrix.start() }, colors = cellMatrix.startColor()) { Text("Start") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { cellMatrix.stop() }) { Text("Stop") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { cellMatrix.reset() }) { Text("Reset") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { cellMatrix.randomize() }) { Text("Randomize") }
            }
        }
    }
}

