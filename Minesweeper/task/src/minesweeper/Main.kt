package minesweeper



fun main() {
    val numberOfRows = 9
    val numberOfColumns = 9

    println("How many times do you want on the field?")
    val numberOfMines = readln().toInt()

    val board = Board(numberOfRows, numberOfColumns)
    board.populateMines(numberOfMines)
    board.calculateNumberOfMinesInVicinityForEachCell()
    board.print()

    while (true) {
        println("Set/unset mines marks or claim a cell as free: ")
        var (x, y, action) = readln().trim().split("\\s+".toRegex())

        if (action == "mine") {
            val markStatus = board.markMine(y.toInt() - 1, x.toInt() - 1)
            if (markStatus == Cell.MARK_STATUS.CAN_NOT_MARKED) {
                println("There is a number here!")
                continue
            }
        } else {
            val exploreStatus = board.exploreCell(y.toInt() - 1, x.toInt() - 1)
            if (!exploreStatus) {
                board.print()
                println("You stepped on a mine and failed!")
                break
            }
        }

        board.print()

        if (board.isSolved) {
            println("Congratulations! You found all the mines!")
            break
        }
    }
}