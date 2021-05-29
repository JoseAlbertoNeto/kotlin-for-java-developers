package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun mapOfPositions(string: String): MutableMap<Char, MutableList<Int>>{
    val result = mutableMapOf<Char, MutableList<Int>>()
    string.forEachIndexed { index, it ->
        if(result[it] == null)
            result[it] = mutableListOf()
        result[it]?.add(index)
    }
    return result
}

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var right = 0
    var wrong = 0

    val secretMap = mapOfPositions(secret)
    val guessMap = mapOfPositions(guess)

    for(mapa in guessMap){
        if(mapa.key in secretMap){
            val intersect = secretMap[mapa.key]?.intersect(guessMap[mapa.key]!!)
            right += intersect!!.size
            wrong += min(guessMap[mapa.key]?.size!!, secretMap[mapa.key]?.size!!) - intersect.size
        }
    }

    return Evaluation(right , wrong)
}
