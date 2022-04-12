package com.migulyaev.tindercards.presentation.view.utils

import androidx.constraintlayout.motion.widget.MotionLayout

class TransitionListener(
    private val onTransitionChange: ((motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) -> Unit)? = null,
    private val onTransitionCompleted: ((motionLayout: MotionLayout?, currentId: Int) -> Unit)? = null,
    private val onTransitionStarted: ((motionLayout: MotionLayout?, startId: Int, endId: Int) -> Unit)? = null,
    private val onTransitionTrigger: ((motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) -> Unit)? = null,
): MotionLayout.TransitionListener {

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) = onTransitionChange?.invoke(motionLayout, startId, endId, progress) ?: Unit

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) = onTransitionTrigger?.invoke(motionLayout, triggerId, positive, progress) ?: Unit

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) = onTransitionCompleted?.invoke(motionLayout, currentId) ?: Unit

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) = onTransitionStarted?.invoke(motionLayout, startId, endId) ?: Unit

}