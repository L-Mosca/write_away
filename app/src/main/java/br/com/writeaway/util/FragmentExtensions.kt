package br.com.writeaway.util

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import br.com.writeaway.R
import com.airbnb.lottie.LottieAnimationView

enum class TransitionAnimation {
    TRANSLATE_FROM_RIGHT, TRANSLATE_FROM_DOWN, TRANSLATE_FROM_LEFT, TRANSLATE_FROM_UP, NO_ANIMATION, FADE
}

fun Fragment.navigate(
    directions: NavDirections,
    animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
    popUpTo: Int? = null,
    clearBackStack: Boolean? = false,
    sharedElements: Pair<View, String>? = null
) {
    val navController = NavHostFragment.findNavController(this)
    val destinationId = if (clearBackStack == true) navController.graph.id else popUpTo
    val transitionAnimation = if (sharedElements == null) animation else null
    val options = buildOptions(transitionAnimation, clearBackStack, destinationId)
    val extras = sharedElements?.let {
        FragmentNavigatorExtras(it)
    }

    navController.navigate(directions.actionId, directions.arguments, options, extras)
}

private fun buildOptions(
    transitionAnimation: TransitionAnimation?, clearBackStack: Boolean?, @IdRes destinationId: Int?
): NavOptions {
    return navOptions {
        anim {
            when (transitionAnimation) {
                TransitionAnimation.TRANSLATE_FROM_RIGHT -> {
                    enter = R.anim.translate_left_enter
                    exit = R.anim.translate_left_exit
                    popEnter = R.anim.translate_right_enter
                    popExit = R.anim.translate_right_exit
                }

                TransitionAnimation.TRANSLATE_FROM_DOWN -> {
                    enter = R.anim.translate_slide_bottom_up
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_slide_bottom_down
                }

                TransitionAnimation.TRANSLATE_FROM_LEFT -> {
                    enter = R.anim.translate_right_enter
                    exit = R.anim.translate_right_exit
                    popEnter = R.anim.translate_left_enter
                    popExit = R.anim.translate_left_exit
                }

                TransitionAnimation.TRANSLATE_FROM_UP -> {
                    enter = R.anim.translate_slide_bottom_down
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_slide_bottom_up
                }

                TransitionAnimation.NO_ANIMATION -> {
                    enter = R.anim.translate_no_change
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_no_change
                }

                TransitionAnimation.FADE -> {
                    enter = R.anim.translate_fade_in
                    exit = R.anim.translate_fade_out
                    popEnter = R.anim.translate_fade_in
                    popExit = R.anim.translate_fade_out
                }

                else -> {
                }
            }
        }

        // To clean the stack below the current fragment,
        // you must set the 'destinationId' = navGraphId and 'inclusive' = true
        destinationId?.let {
            popUpTo(destinationId) {
                inclusive = clearBackStack == true
            }
        }
    }
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.showKeyboard(view: View) {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun LottieAnimationView.setAnimationStatus(startAnimation: Boolean, whenFalse: () -> Unit = {}) {
    if (startAnimation) {
        this.playAnimation()
    } else {
        whenFalse.invoke()
        this.pauseAnimation()
    }
}

fun Fragment.setStatusBarColor(color: Int) {
    val windows = requireActivity().window
    windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    windows.statusBarColor = color
}