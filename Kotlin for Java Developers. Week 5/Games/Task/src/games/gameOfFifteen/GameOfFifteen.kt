package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.game2048.Game2048Initializer
import games.game2048.addNewValue
import games.game2048.moveValues

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        var index = 0
        for(i in 1..board.width){
            for(j in 1..board.width){
                board[board.getCell(i,j)] = initializer.initialPermutation.getOrNull(index++)
            }
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon(): Boolean{
        var count = 1
        var error = false
        for(i in 1..board.width){
            for(j in 1..board.width){
                if(board[board.getCell(i,j)] != count++)
                    error = !(i==board.width && i==j)
            }
        }
        return !error
    }

    fun switchValues(oldCell: Cell, newCell:Cell){
        board[oldCell] = board[newCell]
        board[newCell] = null
    }

    override fun processMove(direction: Direction) {
        val nullCell = board.filter{it==null}.toList()[0]
        when(direction){
            Direction.RIGHT -> if(nullCell.j - 1 > 0){switchValues(nullCell, board.getCell(nullCell.i, nullCell.j-1))}
            Direction.LEFT -> if(nullCell.j + 1 <= board.width){switchValues(nullCell, board.getCell(nullCell.i, nullCell.j+1))}
            Direction.DOWN -> if(nullCell.i - 1 > 0){switchValues(nullCell, board.getCell(nullCell.i-1, nullCell.j))}
            else -> if(nullCell.i + 1 <= board.width){switchValues(nullCell, board.getCell(nullCell.i+ 1, nullCell.j))}
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}