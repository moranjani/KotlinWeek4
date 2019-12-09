package board

import board.Direction.*
open class BoardImpl(override val width: Int) : SquareBoard {
    protected val board : MutableList<Cell> = mutableListOf()
    init {
        for (x in 1..width) {
            for (y in 1..width) {
                board.add(Cell(x,y))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
         if (i !in 1..width || j !in 1..width) return null
        return getCell(i, j)
    }

    override fun getCell(i: Int, j: Int): Cell {
        return board[(i-1)*width + (j-1)]
    }

    override fun getAllCells(): Collection<Cell> {
        return board
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val row = mutableListOf<Cell>()
        for (x in jRange) {
            getCellOrNull(i,x)?.let {
                row.add(it)
            }
        }
        return row
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val col = mutableListOf<Cell>()
        for (x in iRange) {
            getCellOrNull(x,j)?.let {
                col.add(it)
            }
        }
        return col
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
         return when (direction) {
             UP -> getCellOrNull(i-1, j)
             DOWN -> getCellOrNull(i+1, j)
             LEFT -> getCellOrNull(i, j-1)
             RIGHT -> getCellOrNull(i, j+1)
         }
    }
}

class GameBoardImpl<T>(width: Int) : GameBoard<T>, BoardImpl(width) {
    private val mapOfValues : MutableMap<Cell, T?> = mutableMapOf()
    init {
        for (cell in board) {
            mapOfValues[cell] = null
        }
    }
    override fun get(cell: Cell): T? {
        return mapOfValues.getOrDefault(getCellOrNull(cell.i, cell.j), null)
    }

    override fun set(cell: Cell, value: T?) {
        mapOfValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return mapOfValues.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return mapOfValues.filterValues(predicate).keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return mapOfValues.filterValues(predicate).isNotEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return mapOfValues.filterValues(predicate).size == width*width
    }

}

fun createSquareBoard(width: Int): SquareBoard = BoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

