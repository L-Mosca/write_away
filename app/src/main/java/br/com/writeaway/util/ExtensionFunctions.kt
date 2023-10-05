package br.com.writeaway.util

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import br.com.writeaway.R

fun Float.getTextSizeLabel(context: Context): String {
    return when (this) {
        AppConstants.TEXT_SIZE_SMALL -> ContextCompat.getString(context, R.string.small)
        AppConstants.TEXT_SIZE_MEDIUM -> ContextCompat.getString(context, R.string.medium)
        AppConstants.TEXT_SIZE_LARGE -> ContextCompat.getString(context, R.string.large)
        AppConstants.TEXT_SIZE_EXTRA_LARGE -> ContextCompat.getString(context, R.string.extraLarge)
        else -> ContextCompat.getString(context, R.string.medium)
    }
}

fun String.getTextSizeValue(context: Context): Float {
    return when (this) {
        ContextCompat.getString(context, R.string.small) -> AppConstants.TEXT_SIZE_SMALL
        ContextCompat.getString(context, R.string.medium) -> AppConstants.TEXT_SIZE_MEDIUM
        ContextCompat.getString(context, R.string.large) -> AppConstants.TEXT_SIZE_LARGE
        ContextCompat.getString(context, R.string.extraLarge) -> AppConstants.TEXT_SIZE_EXTRA_LARGE
        else -> AppConstants.TEXT_SIZE_MEDIUM
    }
}

fun Int.getOrderTypeLabel(context: Context): String {
    return when (this) {
        AppConstants.ORDER_BY_CREATE_DATE -> ContextCompat.getString(context, R.string.createDate)
        AppConstants.ORDER_BY_UPDATE_DATE -> ContextCompat.getString(
            context,
            R.string.modificationDate
        )

        else -> ContextCompat.getString(context, R.string.modificationDate)
    }
}

fun String.getOrderTypeValue(context: Context): Int {
    return when (this) {
        ContextCompat.getString(context, R.string.createDate) -> AppConstants.ORDER_BY_CREATE_DATE
        ContextCompat.getString(
            context,
            R.string.modificationDate
        ) -> AppConstants.ORDER_BY_UPDATE_DATE

        else -> AppConstants.ORDER_BY_UPDATE_DATE
    }
}

fun String.getLisViewValue(context: Context): LayoutManager {
    return when (this) {
        ContextCompat.getString(context, R.string.viewInGrid) -> GridLayoutManager(context, 2)
        ContextCompat.getString(context, R.string.viewInList) -> LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}