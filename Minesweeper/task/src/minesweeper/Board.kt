package minesweeper

class Board(private val numberOfRows: Int, private val numberOfColumns: Int, var numberOfMines: Int = 0) {
    private val rows = mutableListOf<MutableList<Cell>>()

    var isSolved: Boolean = false
        get() {
            var numberOfExploredCell = 0
            var remainingCellHasMine = numberOfMines

            for (row in rows) {
                for (cell in row) {
                    if (cell.isExplored) {
                        numberOfExploredCell++
                    }

                    if (cell.hasMine && cell.isSolved) {
                        remainingCellHasMine--
                    }
                }
            }

            if (remainingCellHasMine == 0) {
                return true
            }

            var totalCell = numberOfRows * numberOfColumns
            return (numberOfExploredCell + numberOfMines) == totalCell
        }

    init {
        generateBoard()
        calculateAdjacentCellsForEachCells()
    }

    private fun generateBoard() {
        repeat(numberOfRows) {
            val row = mutableListOf<Cell>()
            repeat(numberOfColumns) {
                row.add(Cell())
            }
            rows += row
        }
    }

    private fun calculateAdjacentCellsForEachCells() {
        for (rowIndex in 0 until numberOfRows) {
            for (columnIndex in 0 until numberOfColumns) {
                rows[rowIndex][columnIndex].adjacentCells = getAdjacentCells(rowIndex, columnIndex)
            }
        }
    }

    private fun getAdjacentCells(rowIndex: Int, columnIndex: Int): MutableList<Cell> {
        val adjacentCells = mutableListOf<Cell>()

        for (adjacentRowIndexOffset in -1..1) {
            for (adjacentColumnIndexOffset in -1..1) {
                val adjacentRowIndex = rowIndex + adjacentRowIndexOffset
                val adjacentColumnIndex = columnIndex + adjacentColumnIndexOffset

                if (adjacentRowIndex == rowIndex && adjacentColumnIndex == columnIndex) {
                    continue
                }

                if (adjacentRowIndex in 0 until numberOfRows
                    && adjacentColumnIndex in 0 until numberOfColumns) {
                    adjacentCells.add(rows[adjacentRowIndex][adjacentColumnIndex])
                }
            }
        }

        return adjacentCells
    }

    fun populateMines(numberOfMines: Int) {
        this.numberOfMines = numberOfMines
        var mineCounter = 0
        val totalCell = numberOfRows * numberOfColumns

        while (mineCounter < numberOfMines && mineCounter < totalCell) {
            val randomRow = (0 until numberOfRows).random()
            val randomColumn = (0 until numberOfColumns).random()
            val cell = rows[randomRow][randomColumn]
            if (cell.hasMine) {
                continue
            }

            cell.hasMine = true
            mineCounter++
        }
    }

    fun calculateNumberOfMinesInVicinityForEachCell() {
        for (row in rows) {
            for (cell in row) {
                cell.calculateNumberOfMinesInVicinity()
            }
        }
    }

    fun markMine(rowIndex: Int, columnIndex: Int): Cell.MARK_STATUS {
        val cell = rows[rowIndex][columnIndex]
        return cell.mark()
    }

    fun print() {
        print(" |")
        for (rowIndex in 0 until numberOfRows) {
            print(rowIndex + 1)
        }
        print('|')
        println()

        print("-|")
        for (rowIndex in 0 until numberOfRows) {
            print("-")
        }
        print('|')
        println()

        for (rowIndex in 0 until numberOfRows) {
            print("${rowIndex + 1}|")
            for (cell in rows[rowIndex]) {
                print(cell)
            }
            print('|')
            println()
        }

        print("-|")
        for (rowIndex in 0 until numberOfRows) {
            print("-")
        }
        print('|')
        println()
    }

    fun exploreCell(rowIndex: Int, columnIndex: Int): Boolean {
        val cell = rows[rowIndex][columnIndex]
        val exploreStatus = cell.explore()
        tryToRemoveMark()

        return exploreStatus
    }

    private fun tryToRemoveMark() {
        for (row in rows) {
            for (cell in row) {
                if (cell.isMark) {
                    cell.tryToRemoveMark()
                }
            }
        }
    }
}
