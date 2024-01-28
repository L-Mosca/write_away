package br.com.writeaway.base

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class CustomItemAnimator : DefaultItemAnimator() {

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val scaleX = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 1f, 0f)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        set.duration = removeDuration

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        set.start()
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        val changeAnimations = super.animateChange(oldHolder, newHolder, preInfo, postInfo)

        val scaleX = ObjectAnimator.ofFloat(newHolder.itemView, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(newHolder.itemView, View.SCALE_Y, 0f, 1f)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        set.duration = changeDuration

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                dispatchChangeStarting(newHolder, true)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchChangeFinished(newHolder, true)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        set.start()

        dispatchChangeFinished(oldHolder, false)
        dispatchChangeFinished(newHolder, true)

        return changeAnimations
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val scaleX = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 0f, 1f)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        set.duration = addDuration

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        set.start()
        return true
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder) = true

    /*
    recordPreLayoutInformation() is called in many cases, not only in item change case. As I want to
    intervene only in the item change case, I check for FLAG_CHANGED. But that is not enough either.
    Change animation can also be called when notifyDataSetChanged() is called for instance. I don’t
    want to check heart state and switch like state in those cases.

    The argument payloads come to play at this point. Payloads help us communicate to the recyclerview
    what exactly has changed. notifyItemChanged method has an optional payload argument. This has the
    type Object, so you could communicate the change as you wish, like an integer or string key, or
    a Bundle.. Inside the recordPreLayoutInformation method, we’ll check for this payload and register
    the information when we need it.
    */
    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        holder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {

        if (changeFlags == FLAG_CHANGED) {
            val scaleX = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 0f, 1f)

            val set = AnimatorSet()
            set.playTogether(scaleX, scaleY)
            set.duration = addDuration

            set.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    dispatchAddStarting(holder)
                }

                override fun onAnimationEnd(animation: Animator) {
                    dispatchAddFinished(holder)
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            set.start()
        }

        return super.recordPreLayoutInformation(state, holder, changeFlags, payloads)
    }
}
