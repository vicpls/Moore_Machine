package com.hh.moore_machine

object Util {

    val tr = arrayOf(
        Transition(1, 1, 2),
        Transition(1, 2, 3),
        Transition(2, 1, 5),
        Transition(2, 2, 6),
        Transition(5, 1, 1),
        Transition(5, 5, 7)
    ).toList()

}