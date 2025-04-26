class Buscaminas {
    var tablero = mutableListOf<MutableList<String>>()
        get() {
            var tablerovuelta = field.toMutableList()
            return tablerovuelta
        }
    var tablerovisible = mutableListOf<MutableList<String>>()
        get() {
            var tablerovuelta = field.toMutableList()
            return tablerovuelta
        }
    private var numMinas: Int = 0
    private var casrestantes: Int = 0
    private var estadoPartida = "Preparar tablero"

    //Funciones publicas


    fun verPosicion(i: Int, j: Int): String {
        return tablero[i][j]
    }

    fun verPosicionvisible(i: Int, j: Int): String {
        return tablerovisible[i][j]
    }

    fun numMinas(): Int {
        return numMinas
    }

    fun casillasRestantes(): Int {
        return casrestantes
    }

    fun estado(): String {
        return estadoPartida.toString()
    }

    fun setTablerotam(largo: Int, ancho: Int) {
        if (ancho < 1 ) {
            throw Exception("Ancho inferior a 1")
        } else if (largo < 1) {
            throw Exception("Largo inferior a 1")
        } else {
            estadoPartida = "Preparar minas"
            tablero = generartableros(largo, ancho)
            tablerovisible = generartableros(largo, ancho)
        }
    }

    fun setMinas(num: Int) {
        if(num > tablero.size*tablero[0].size) {
            throw Exception("Numero de minas > numero de casillas")
        } else {
            estadoPartida = "Jugando"
            numMinas = num
            var bombaadd = num
            while (bombaadd > 0) {
                val numi = (0 until tablero.size).random()
                val numj = (0 until tablero[numi].size).random()
                if(tablero[numi][numj] != "*") {
                    tablero[numi][numj] = "*"
                    bombaadd--
                }
            }
            calculoNumeros()
        }
    }

    fun inputuserflag(car: Char, i: Int, j: Int) {
        when (car) {
            'f' -> if (tablerovisible[i][j] == "x") {
                tablerovisible[i][j] = "F"
            }
            'q' -> if (tablerovisible[i][j] == "F") {
                tablerovisible[i][j] = "x"
            }
        }
    }

    fun inputuser( i: Int, j: Int) {
        val resmapa = tablero[i][j]
        if (resmapa == "*" && tablerovisible[i][j] != "F") {
            estadoPartida = "PERDER"
        } else {
            descubir(i,j)
        }
    }

    fun recuentocasillas() {
        var casillasrestantes = 0
        for (x in 0 until tablerovisible.size){
            for (y in 0 until tablerovisible[x].size){
                if (tablerovisible[x][y] == "F") casillasrestantes++
                if (tablerovisible[x][y] == "x") casillasrestantes++
            }
        }
        casrestantes = casillasrestantes
        if (casrestantes == numMinas) {
            estadoPartida = "GANAR"
        }
    }

    //Funciones privadas

    private fun generartableros(ancho: Int, largo: Int): MutableList<MutableList<String>> {
        val lista = mutableListOf<MutableList<String>>()
        for (x in 0 until largo){
            val fila = mutableListOf<String>()
            for (y in  0 until ancho){
                fila.add("x")
            }
            lista.add(fila)
        }
        return lista
    }

    private fun calculoNumeros() {
        for(x in 0 until tablero.size) {
            for (y in 0 until tablero[x].size) {
                if (verPosicion(x,y) != "*"){
                    tablero[x][y] = calulominasposicion(x,y).toString()
                }
            }
        }
    }

    private fun calulominasposicion(i: Int, j: Int): Int {
        var catidadbombas = 0
        for (x in i-1 .. i+1) {
            for (y in j-1..j+1) {
                if (x < 0 || x >= tablero.size || y < 0 || y >= tablero[0].size){
                    continue
                }
                if (tablero[x][y]=="*") catidadbombas++
            }
        }
        return catidadbombas
    }

    private fun descubir(i: Int,j: Int){
        if (i < 0 || i >= tablero.size || j < 0 || j >= tablero[0].size ) return
        if (tablero[i][j] == "*") return
        if (tablerovisible[i][j] == " ") return
        if (tablerovisible[i][j] == "F") return
        if (tablero[i][j].toInt() in 1..9 ) {
            tablerovisible[i][j] = verPosicion(i,j)
            return
        }
        if (tablero[i][j] == "0") {
            tablerovisible[i][j] = " "
            descubir(i-1,j-1)
            descubir(i-1,j)
            descubir(i-1,j+1)
            descubir(i,j-1)
            descubir(i,j+1)
            descubir(i+1,j-1)
            descubir(i+1,j)
            descubir(i+1,j+1)
        }
    }

}
