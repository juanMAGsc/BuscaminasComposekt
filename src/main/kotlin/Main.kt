import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

fun obtenerHoraActual(): String {
    val formato = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formato.format(Date())
}

@Composable
@Preview
fun App() {
    val controlestado = remember { EstadoJuego() }
    var bandera by remember { mutableStateOf(false) }
    var horaActual by remember { mutableStateOf(obtenerHoraActual()) }
    LaunchedEffect(Unit) {
        while (true) {
            horaActual = obtenerHoraActual()
            delay(1000L)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera: reloj y controles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🕒 $horaActual",
                style = MaterialTheme.typography.body1
            )

            TextField(
                value = controlestado.ancho,
                onValueChange = { controlestado.ancho = it },
                label = { Text("Ancho") },
                modifier = Modifier.width(100.dp)
            )

            TextField(
                value = controlestado.largo,
                onValueChange = { controlestado.largo = it },
                label = { Text("Largo") },
                modifier = Modifier.width(100.dp)
            )

            TextField(
                value = controlestado.numminas,
                onValueChange = { controlestado.numminas = it },
                label = { Text("Minas") },
                modifier = Modifier.width(100.dp)
            )

            Button(
                onClick = {
                    controlestado.settablero()
                    controlestado.numBanderas = 0
                    controlestado.numminascalculadas = controlestado.numminas.toInt()
                          },
                enabled = controlestado.estado != "Jugando",
                modifier = Modifier.height(56.dp)
            ) {
                Text("Preparar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Estado y switch de banderas
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).background(Color.LightGray),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var colorfinal = when(controlestado.estado) {
                    "GANAR" -> Color.Green
                    "PERDER" -> Color.Red
                    else -> Color.LightGray
                }

                Text(text = "Estado actual: ${controlestado.estado}",
                    modifier = Modifier.background(colorfinal))

                Spacer(modifier = Modifier.height(8.dp))

                Text("PUNTUACION Banderas colocadas: "+controlestado.numBanderas+" Minas Restantes: "+(controlestado.numminascalculadas-controlestado.numBanderas))

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Modo Bandera")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = bandera,
                        onCheckedChange = { bandera = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column( horizontalAlignment = Alignment.CenterHorizontally ) {
            controlestado.tablerovisible.forEachIndexed { x, fila ->
                Row( horizontalArrangement = Arrangement.Center)
                {
                    fila.forEachIndexed { y, celda ->
                        val color = when (celda) {
                            "x" -> Color.Gray
                            "F" -> Color.White
                            "1" -> Color.Cyan
                            "2", "3" -> Color.Yellow
                            "4", "5", "6", "7" -> Color.Red
                            "8" -> Color.Black
                            else -> Color.Green
                        }

                        Button(
                            onClick = {
                                if (bandera) {
                                    if (controlestado.tablerovisible[x][y] == "F") {
                                        controlestado.colocarbandera('q', x, y)
                                    } else {
                                        controlestado.colocarbandera('f', x, y)
                                    }
                                } else {
                                    controlestado.reveal(x, y)
                                }
                            },
                            enabled = controlestado.estado != "PERDER" && controlestado.estado != "GANAR",
                            modifier = Modifier.padding(2.dp).size(40.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = color)
                        ) {
                            Text( text = celda)
                        }
                    }
                }
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = "Buscaminas") {
        App()
    }
}
