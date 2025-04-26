import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class EstadoJuego() {
    private var buscaminas = Buscaminas()

    var tablero by mutableStateOf( buscaminas.tablero)
    var tablerovisible by mutableStateOf( buscaminas.tablerovisible)
    var ancho by mutableStateOf("")
    var largo by mutableStateOf("")
    var numminas by mutableStateOf("")
    var numminascalculadas by mutableStateOf(0)
    var numBanderas by mutableStateOf(0)
    var estado by mutableStateOf("Preparando")

    fun  settablero() {
       try {
           buscaminas.setTablerotam(ancho.toInt(),largo.toInt())
           buscaminas.setMinas(numminas.toInt())
       } catch (e: Exception) {
           estado = e.message.toString()
       }
        tablero = buscaminas.tablero
        tablerovisible = buscaminas.tablerovisible
        estado = buscaminas.estado()
    }

    fun reveal(i: Int, j: Int) {
        buscaminas.inputuser(i,j)
        tablerovisible = buscaminas.tablerovisible
        buscaminas.recuentocasillas()
        estado = buscaminas.estado()
    }

    fun colocarbandera(ban: Char,i: Int, j: Int) {
        buscaminas.inputuserflag(ban,i,j)
        if (ban == 'f'){
            numBanderas++
        } else {
            numBanderas--
        }
        val nuevotab = buscaminas.tablerovisible
        tablerovisible = nuevotab
        buscaminas.recuentocasillas()
        estado = buscaminas.estado()
    }

}