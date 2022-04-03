package com.example.passwordmanager.ui.home

import android.annotation.SuppressLint
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlin.math.max
import kotlin.math.min

@SuppressLint("StaticFieldLeak")
class HomeItemTouchHelperCallback private constructor() : ItemTouchHelper.Callback() {
    companion object {
        fun newInstance(): HomeItemTouchHelperCallback = HomeItemTouchHelperCallback().apply {
            mItemTouchHelper = ItemTouchHelper(this)
        }
    }

    /** newInstance()の中でのみ初期化している */
    private lateinit var mItemTouchHelper: ItemTouchHelper
    val itemTouchHelper: ItemTouchHelper get() = mItemTouchHelper

    /** 現在スワイプされているholder */
    private var swipedHolder: HomeViewHolder? = null // nullでどこもスワイプされてないことを示す

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val holder = viewHolder.cast()
        when(direction) {
            ItemTouchHelper.START -> onLeftSwiped(holder)  // 左方向にSwipeした場合
            ItemTouchHelper.END   -> onRightSwiped(holder) // 右方向にSwipeした場合
        }

        // RecoverAnimation を削除する(notifyItemRemovedの代わり)
        ::mItemTouchHelper.isInitialized || throw AssertionError("ItemTouchHelper must be attached.")
        mItemTouchHelper.onChildViewDetachedFromWindow(holder.itemView)
    }

    // Itemの描画を行う
    @SuppressLint("NotifyDataSetChanged")
    override fun onChildDraw(c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder, dX: Float, dY: Float, a: Int, isCA: Boolean) {
        val holder = vh.cast()
        if(dX <= 0f) { // 左に動かした場合
            if (holder == swipedHolder) return // スワイプしたholderがスワイプボタン表示済みだったら何もしない
            onLeftChildDraw(c,rv, holder, dX, dY, a, isCA)

        } else { // 右に動かした場合
            if (holder != swipedHolder) return // スワイプボタンが閉じている場合は何もしない
            onRightChildDraw(c,rv, holder, dX, dY, a, isCA)
        }
    }

    /** START, END方向のSwipeを有効にする */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
        makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)

    /** メニューが半分開いたらSwipeにする */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val holder = viewHolder.cast()
        return holder.background.width.toFloat() / holder.itemView.width / 2
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

    private fun onLeftSwiped(holder: HomeViewHolder) {
        /** ２つ目のスワイプの場合に、前回のスワイプを元に戻す */
        val unlockListener = object : View.OnLayoutChangeListener {
            override fun onLayoutChange(view: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                view?.removeOnLayoutChangeListener(this) // 複数のリスナーが登録されないようにする
                clearHolder()
            }
        }

        holder.foreground.addOnLayoutChangeListener(unlockListener)
        lockForeground(holder) // 固定する
    }

    private fun onRightSwiped(holder: HomeViewHolder) {
        // スワイプしたholderとスワイプ済みholderが一致している場合、固定解除する
        if (holder == swipedHolder) unlockForeground()
    }

    private fun onLeftChildDraw(c: Canvas, recyclerView: RecyclerView, holder: HomeViewHolder, dX: Float, dY: Float, a: Int, isCA: Boolean) {
        val maxWidth = holder.background.width.toFloat()
        val x = max(dX, -maxWidth) // backgroundの幅までしか開かない

        // backgroundを固定して、foregroundだけを動かす
        getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, a, isCA)
        getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, dY, a, isCA)

        // 固定を入れ替えるために、固定済みのItemを解除する
        clearHolder(holder)
    }

    private fun onRightChildDraw(c: Canvas, recyclerView: RecyclerView, holder: HomeViewHolder, dX: Float, dY: Float, a: Int, isCA: Boolean) {
        val maxWidth = holder.background.width.toFloat()
        val x = min(maxWidth, dX) - maxWidth // backgroundの幅までしか閉じない

        // backgroundを固定して、foregroundだけを動かす
        getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, a, isCA)
        getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, dY, a, isCA)
    }

    private fun clearHolder(holder: HomeViewHolder? = null) {
        swipedHolder?.also {
            if (it != holder) {
                getDefaultUIUtil().clearView(it.background)
                getDefaultUIUtil().clearView(it.foreground)
                unlockForeground()
            }
        }
    }

            fun unlockForeground() { swipedHolder = null }
    private fun lockForeground(holder: HomeViewHolder) { swipedHolder = holder }
    private fun RecyclerView.ViewHolder.cast() = this as HomeViewHolder
}