package board

import board.Direction.*
import java.lang.IllegalArgumentException

open class SquareBoardImpl(override val width: Int) : SquareBoard{
    private val squareBoard: ArrayList<ArrayList<Cell>>  by lazy{
        val result = ArrayList<ArrayList<Cell>>()
        for(i in 0 until width){
            result.add(ArrayList())
            for(j in 0 until width) {
                result[i].add(Cell(i+1,j+1))
            }
        }
        result
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if(i > 0 && i < width+1 && j > 0 && j < width+1) squareBoard[i-1][j-1] else null
    }

    override fun getCell(i: Int, j: Int): Cell {
        return if(i > 0 && i < width+1 && j > 0 && j < width+1) squareBoard[i-1][j-1] else throw IllegalArgumentException()
    }

    override fun getAllCells(): Collection<Cell> {
        return squareBoard.let { arrayList ->
            val lista = arrayListOf<Cell>()
            arrayList.forEach{
                it.forEach{it2 -> lista.add(it2)}
            }
            lista
        }
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val list:MutableList<Cell> = mutableListOf()
        for(j in jRange){
            if(j > 0 && j < width+1)
                list.add(squareBoard[i-1][j-1])
        }
        return list
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val list:MutableList<Cell> = mutableListOf()
        for(i in iRange){
            if(i > 0 && i < width+1)
                list.add(squareBoard[i-1][j-1])
        }
        return list
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction){
            LEFT -> getCellOrNull(i, j-1)
            RIGHT -> getCellOrNull(i, j+1)
            UP -> getCellOrNull(i-1, j)
            DOWN -> getCellOrNull(i+1,j)
        }
    }
}

class GameBoardImpl<T>(override val width: Int): SquareBoardImpl(width), GameBoard<T>{
    private val gameBoard: MutableMap<Cell, T?>  by lazy{
        val mutableMap = mutableMapOf<Cell, T?>()
        for(cell in getAllCells())
            mutableMap[cell] = null
        mutableMap
    }

    override fun get(cell: Cell): T? {
        return gameBoard[cell]
    }

    override fun set(cell: Cell, value: T?) {
        gameBoard[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return gameBoard.filterValues{predicate(it)}.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return gameBoard.filterValues{predicate(it)}.keys.firstOrNull()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return gameBoard.any{predicate(it.value)}
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return gameBoard.all{predicate(it.value)}
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)
