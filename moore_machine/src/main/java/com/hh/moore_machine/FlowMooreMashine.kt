package com.hh.moore_machine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FlowMooreMashine <T,R> constructor(
    initState: T,
    disp: CoroutineDispatcher = Dispatchers.Default) {

    private val _state = MutableStateFlow(initState)
    val state: StateFlow<T> = _state.asStateFlow()

    private var transitions: MutableList<Transition<T, R>> = ArrayList(0)

    //private val cScope = CoroutineScope(SupervisorJob() + disp)
    private val mutex = Mutex()

    /**
     * Create a transition and add to table.
     */
    @Synchronized
    fun addTransition(curState: T, signal: R, newState: T){
        val newTransition = Transition(curState, signal, newState)
        transitions += newTransition
        //log(MooreMachine.Action.ADD_TRANS, newTransition)
    }

    /**
     * Add to table list of [Transition].
     */
    @Synchronized
    fun addAllTransition(transition: List<Transition<T, R>>) {
        transitions += transition
        //log(MooreMachine.Action.ADD_TRANS, "$transition\n")
    }

    suspend fun act (signal: R): FlowMooreMashine<T,R> {
        //cScope.launch {
            mutex.withLock {
                transitions
                    .firstOrNull { it.curState == _state.value && it.signal == signal }
                    ?.let {
                        //println(it.toString())
                        _state.value = it.newState
                        //_state.emit( it.newState )
                        //log(MooreMachine.Action.ACT, it)
                    }
            }
        //}
        return this
    }
}