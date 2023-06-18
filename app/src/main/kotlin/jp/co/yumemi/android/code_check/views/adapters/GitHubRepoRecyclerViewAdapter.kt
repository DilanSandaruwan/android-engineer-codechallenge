package jp.co.yumemi.android.code_check.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.GitHubAccount

/**
 * RecyclerView adapter for displaying GitHub repositories.
 *
 * @param itemClickListener The listener for item click events.
 */
class GitHubRepoRecyclerViewAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<GitHubAccount, GitHubRepoRecyclerViewAdapter.ViewHolder>(diff_util) {

    /**
     * ViewHolder class for holding the views of each item in the RecyclerView.
     *
     * @param view The view representing an item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /**
     * Interface for defining item click events.
     */
    interface OnItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param item The clicked GitHubAccount item.
         */
        fun itemClick(item: GitHubAccount)
    }

    /**
     * Creates a ViewHolder by inflating the item layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the view.
     * @return The created ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(_view)
    }

    /**
     * Binds the data to the views of the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val _item = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            _item.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(_item)
        }
    }

    companion object {
        /**
         * DiffUtil.ItemCallback for calculating the differences between old and new items in the list.
         */
        val diff_util = object : DiffUtil.ItemCallback<GitHubAccount>() {
            /**
             * Checks if the items have the same identity.
             *
             * @param oldItem The old item.
             * @param newItem The new item.
             * @return True if the items have the same identity, false otherwise.
             */
            override fun areItemsTheSame(oldItem: GitHubAccount, newItem: GitHubAccount): Boolean {
                return oldItem.name == newItem.name
            }

            /**
             * Checks if the contents of the items are the same.
             *
             * @param oldItem The old item.
             * @param newItem The new item.
             * @return True if the contents of the items are the same, false otherwise.
             */
            override fun areContentsTheSame(
                oldItem: GitHubAccount,
                newItem: GitHubAccount
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}