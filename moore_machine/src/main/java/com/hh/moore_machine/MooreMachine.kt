package com.hh.moore_machine

/**
 *  The finite-state Moore machine realization.
 *
 *  The basis is the table of transition that should be fill before any action.
 *  Use methods [addAllTransition] and [addTransition] for that.
 *  Also should be point initial state with [init].
 *  The state change of machine occurs by method [act].
 *  @param T is a state of machine type
 *  @param R is an input action type
 */
class MooreMachine <T,R> {

    /**
     * Current state of machine.
     */
    var currentState: T? = null
    private set

    private var logBuffer: MutableList<String>? = null

    /**
     * Size of buffer for logging. If 0 - no any logging.
     */
    var logBufferSize = 0
        set(value) {
            if (value < 1) {
                logBuffer = null
                field = 0
            } else {
                if (logBuffer == null) logBuffer = MutableList(0) { "" }
                else {
                    if (value in 1 until logBuffer!!.size) logBuffer =
                        logBuffer!!.drop(logBuffer!!.size - value).toMutableList()
                }
            }
            field = value
        }


    private var transitions: ArrayList<Transition<T,R>> = ArrayList(0)

    /**
     * The record of state-transition table
     * @param curState current state
     * @param signal inputs
     * @param newState next State
     */
    data class Transition <T, R> (
        internal val curState: T,
        internal val signal: R,
        internal val newState: T
    ) {
        override fun toString(): String
            = "$curState($signal)=$newState\n"
    }

    /**
     * Create a transition and add to table.
     */
    fun addTransition(curState: T, signal: R, newState: T){
        val newTransition = Transition(curState, signal, newState)
        transitions += newTransition
        log(Action.ADD_TRANSI, newTransition)
    }

    /**
     * Add to table list of [Transition].
     */
    fun addAllTransition(transition: List<Transition<T, R>>){
        transitions += transition
        log(Action.ADD_TRANSI, "$transition\n")
    }

    /**
     * Specify initial state of machine.
     */
    fun init(start: T){
        currentState = start
        log(Action.INIT, "${start.toString()}\n")
    }

    /**
     * Do the transition of machine.
     *
     * if the inputs cannot be find in the transition table then no changing state will be.
     * if a few record match a pair of current state and signal then acts first record in table.
     * The Transient table can be verified by method [verify].
     *
     * @param signal input influence
     * @return new state of machine
     */
    fun act(signal: R): T{
        if (currentState == null) throw IllegalStateException("Class is not initialised.")

        synchronized(currentState!!) {
            transitions.firstOrNull { it.curState == currentState && it.signal == signal }
                ?.let {
                    currentState = it.newState
                    log(Action.ACT, it)
                }
        }

        return currentState!!
    }

    /**
     * Return the whole machine log.
     */
    fun getLog(): List<String>? {
        return logBuffer
    }

    /**
     * Return the n-last string of log.
     * @param last quantity of last strings in a log
     */
    fun getLog(last: Int): List<String>? {
        return logBuffer?.takeLast(last)
    }

    /**
     * Clear log of machine.
     */
    fun clearLog(){logBuffer?.clear()}

    /**
     * Verify transient table.
     * It is not mandatory method for action machine.
     * Checked the next inconsistent records:
     * * unreachable state
     * * pairs (state, signal) leads to different states
     * * copy of records
     * @return false if one of the above present in table
     */
    fun verify(initialState: T): Boolean = !( isUnreachable(initialState) or isAmbiguity())


    // для соcтояния может быть несколько записей с одним сигналом ведущим в разные состояния.
    // True if transitions contains ambiguity records or copy.
    private fun isAmbiguity(): Boolean {
        val keys: HashSet<Pair<T,R>> = HashSet(0)

        transitions.forEach {
            val pair = Pair(it.curState, it.signal)
            if ( ! keys.add(pair) ) {
                return@isAmbiguity true     // Copy or Ambiguity
            }
        }
        return false
    }


    // состояние не достижимо: зн. слева не имеет записей где оно справа.
    // True if transitions contains unreachable state.
    private fun isUnreachable(initialState: T): Boolean {
        val s: HashSet<T> = HashSet()
        val d: HashSet<T> = HashSet()
        transitions.forEach {
            s.add(it.curState)
            d.add(it.newState)
        }

        val dif = s-d

        return !(dif.isEmpty() or (dif.size==1 && dif.contains(initialState)))
    }



    private fun log(action: Action, transition: Transition<T,R>){
        if (logBufferSize <0) return
        log(action, "$transition")
    }

    private fun log(action: Action, log: String){
        if (logBufferSize <=0) return

        if (logBuffer!!.size >= logBufferSize) {
            logBuffer!!.removeAt(0)
        }
        logBuffer!! +=  "${action.name}: $log"
    }

    private enum class Action {
        INIT,
        ACT,
        ADD_TRANSI
    }

}