package minesweeper

class Cell(var hasMine: Boolean = false, var isMark: Boolean = false, var isExplored: Boolean = false) {
    var adjacentCells: MutableList<Cell> = mutableListOf()
    var isSolved: Boolean = true
        get() {
            if (hasMine && !isMark) {
                return false
            }

            return true
        }

    var hasMineAround: Boolean = false
        get() {
            return numberOfMinesInVicinity > 0
        }

    private var numberOfMinesInVicinity = 0

    enum class MARK_STATUS {
        MARKED,
        UNMARKED,
        CAN_NOT_MARKED
    }

    fun calculateNumberOfMinesInVicinity() {
        numberOfMinesInVicinity = 0
        for (adjacentCell in adjacentCells) {
            if (adjacentCell.hasMine) {
                numberOfMinesInVicinity++
            }
        }
    }

    fun mark(): MARK_STATUS {
        if (isExplored) {
            if (!hasMine && hasMineAround) {
                return MARK_STATUS.CAN_NOT_MARKED
            }

            return MARK_STATUS.MARKED
        }

        if (!isMark) {
            isMark = true
            return MARK_STATUS.MARKED
        }

        isMark = false
        return MARK_STATUS.UNMARKED
    }

    fun explore(): Boolean {
        if (isExplored) {
            return true
        }

        isExplored = true
        if (hasMine) {
            return false
        }

        automaticExplore()

        return true
    }

    private fun automaticExplore() {
        if (hasMine) {
            return
        }

        isExplored = true

        if (hasMineAround) {
            return
        }

        for (adjacentCell in adjacentCells) {
            if (!adjacentCell.isExplored) {
                adjacentCell.automaticExplore()
            }
        }
    }

    override fun toString(): String {
        if (isMark) {
            return "*"
        }

        if (hasMine) {
            return "."
        }

        if (!isExplored) {
            return "."
        }

        return if (hasMineAround) {
            numberOfMinesInVicinity.toString()
        } else {
            "/"
        }
    }

    fun tryToRemoveMark() {
        if (!isMark) {
            return
        }

        for (adjacentCell in adjacentCells) {
            if (adjacentCell.isExplored && !adjacentCell.hasMineAround) {
                isMark = false
                return
            }
        }
    }
}
