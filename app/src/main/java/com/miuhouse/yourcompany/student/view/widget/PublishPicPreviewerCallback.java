package com.miuhouse.yourcompany.student.view.widget;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by kings on 3/10/2017. drag,swiped
 */
public class PublishPicPreviewerCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mItemTouchHelperAdapter;
    public static final float ALPHA_FULL = 1.0f;

    public PublishPicPreviewerCallback(
        ItemTouchHelperAdapter mItemTouchHelperAdapter) {
        this.mItemTouchHelperAdapter = mItemTouchHelperAdapter;
    }

    @Override public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP
                | ItemTouchHelper.DOWN
                | ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mItemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),
            target.getAdapterPosition());
        return false;
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //if (viewHolder instanceof )
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
    }

    /**
     * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}.
     * 可以将onMove和onSwiped回调方法传递出去的接口
     */
    public interface ItemTouchHelperAdapter {

        /**
         * Called when an item has been dragged far enough to trigger a move. This is called every
         * time an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
         * <br/> Implementations should call {@link RecyclerView.Adapter#notifyItemMoved(int, int)}
         * after adjusting the underlying data to reflect this move.
         *
         * @param fromPosition The start position of the moved item.
         * @param toPosition Then resolved position of the moved item.
         * @return True if the item was moved to the new adapter position.
         * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        boolean onItemMove(int fromPosition, int toPosition);

        /**
         * Called when an item has been dismissed by a swipe.<br/> <br/> Implementations should call
         * {@link RecyclerView.Adapter#notifyItemRemoved(int)} after adjusting the underlying data
         * to reflect this removal.
         *
         * @param position The position of the item dismissed.
         * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onItemDismiss(int position);
    }

    /**
     * Interface to notify an item ViewHolder of relevant callbacks from {@link
     * ItemTouchHelper.Callback}.
     */
    public interface ItemTouchHelperViewHolder {

        /**
         * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
         * Implementations should update the item view to indicate it's active state.
         */
        void onItemSelected();

        /**
         * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active
         * item state should be cleared.
         */
        void onItemClear();
    }
}
