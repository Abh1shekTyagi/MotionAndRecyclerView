package com.example.complexmotion

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SwipeAndDragItemHelper(private val mAdapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

    private fun getScaleAnimator(
        view: View,
        scaleValueX: Float,
        scaleValueY: Float
    ): ValueAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValueX)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValueY)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ACTION_STATE_DRAG -> {
                getScaleAnimator(viewHolder?.itemView ?: return, 1.02f, 1.02f).start()
                val targetElevation = 20f
                viewHolder.itemView.apply {
                    animate()
                        .setDuration(200)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .translationZ(targetElevation)
                        .start()
                }
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        when (actionState) {
            ACTION_STATE_SWIPE -> {
                val width =
                    viewHolder.itemView.width / 1.08f //1.08 to make it fade faster when swiping
                viewHolder.itemView.alpha =
                    1 - abs(dX / width)//abs to fade in both directions left and right
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        getScaleAnimator(viewHolder.itemView, 1f, 1f).start()
        val targetElevation = 0f
        viewHolder.itemView.animate()
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .translationZ(targetElevation)
            .start()
    }
}


interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}