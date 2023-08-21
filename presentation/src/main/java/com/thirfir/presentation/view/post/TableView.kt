package com.thirfir.presentation.view.post

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginBottom
import com.thirfir.presentation.model.TableElement

class TableView(
    context: Context,
    private val table: Array<Array<TableElement?>>,
) : ConstraintLayout(context) {

    private val ids = Array(table.size) { Array<Int?>(table[0].size) { null } }
    private val tableItems = Array(table.size) { Array<TableItemView?>(table[0].size) { null } }

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        id = View.generateViewId()
        val constraintSet = ConstraintSet()

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val maxHeights = Array(table.size) { 0 }
                for(rowIndex in table.indices)
                    maxHeights[rowIndex] = findMaxHeight(rowIndex)

                val maxWidths = Array(table[0].size) { 0 }
                for(columnIndex in table[0].indices)
                    maxWidths[columnIndex] = findMaxWidth(columnIndex)

                for (rowIndex in table.indices) {
                    for (columnIndex in table[0].indices) {
                        if(tableItems[rowIndex][columnIndex] == null)
                            continue
                        tableItems[rowIndex][columnIndex]!!.mWidth = maxWidths[columnIndex]
                        tableItems[rowIndex][columnIndex]!!.mHeight = maxHeights[rowIndex]
                        tableItems[rowIndex][columnIndex]!!.setFitSize()
                        tableItems[rowIndex][columnIndex]!!.requestLayout()
                    }
                }
                setSpannedSize()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        for (rowIndex in table.indices) {
            for (columnIndex in table[rowIndex].indices) {
                if (table[rowIndex][columnIndex] == null)
                    continue

                val tableItem = TableItemView(context, table[rowIndex][columnIndex]!!)
                tableItems[rowIndex][columnIndex] = tableItem
                tableItem.id = View.generateViewId()
                for (r in 0 until table[rowIndex][columnIndex]!!.rowSpan) {
                    for (c in 0 until table[rowIndex][columnIndex]!!.colSpan) {
                        ids[rowIndex + r][columnIndex + c] = tableItem.id
                    }
                }
                addView(tableItem)
                constraintSet.clone(this)

                if (rowIndex == 0 && columnIndex == 0) {
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.TOP,
                        this.id,
                        ConstraintSet.TOP,
                    )
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.START,
                        this.id,
                        ConstraintSet.START,
                    )
                } else if (rowIndex == 0) {
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.TOP,
                        this.id,
                        ConstraintSet.TOP,
                    )
                    for (cIter in 1..columnIndex) {
                        if (ids[rowIndex][columnIndex - cIter] != null) {
                            constraintSet.connect(
                                tableItem.id,
                                ConstraintSet.START,
                                ids[rowIndex][columnIndex - cIter]!!,
                                ConstraintSet.END,
                            )
                            break
                        }
                    }
                } else if (columnIndex == 0) {
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.START,
                        this.id,
                        ConstraintSet.START,
                    )
                    for (rIter in 1..rowIndex) {
                        if (ids[rowIndex - rIter][columnIndex] != null) {
                            constraintSet.connect(
                                tableItem.id,
                                ConstraintSet.TOP,
                                ids[rowIndex - rIter][columnIndex]!!,
                                ConstraintSet.BOTTOM,
                            )
                            break
                        }
                    }
                } else {
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.TOP,
                        ids[rowIndex - 1][columnIndex]!!,
                        ConstraintSet.BOTTOM,
                    )
                    constraintSet.connect(
                        tableItem.id,
                        ConstraintSet.START,
                        ids[rowIndex][columnIndex - 1]!!,
                        ConstraintSet.END,
                    )
                }
                constraintSet.applyTo(this)
            }
        }
    }

    private fun setSpannedSize() {
        for (rowIndex in table.indices) {
            for (columnIndex in table[rowIndex].indices) {
                val tableElement = table[rowIndex][columnIndex] ?: continue
                if (tableElement.rowSpan == 1 && tableElement.colSpan == 1)
                    continue

                if (tableElement.rowSpan > 1) {
                    for (c in table[rowIndex].indices) {
                        var nulled = false
                        if (c == columnIndex)
                            continue
                        var height = 0
                        for (r in 0 until tableElement.rowSpan) {
                            if (tableItems[rowIndex + r][c] == null || table[rowIndex + r][c]!!.rowSpan > 1) {
                                nulled = true
                                break
                            }
                            else {
                                height += tableItems[rowIndex + r][c]!!.mHeight
                            }
                        }
                        if (nulled)
                            continue
                        else {
                            tableItems[rowIndex][columnIndex]!!.mHeight = height
                            break
                        }
                    }
                }

                if (tableElement.colSpan > 1) {
                    for (r in table.indices) {
                        var nulled = false
                        if (r == rowIndex)
                            continue
                        var width = 0
                        for (c in 0 until tableElement.colSpan) {
                            if (tableItems[r][columnIndex + c] == null || table[r][columnIndex + c]!!.colSpan > 1) {
                                nulled = true
                                break
                            }
                            else
                                width += tableItems[r][columnIndex + c]!!.mWidth
                        }
                        if (nulled)
                            continue
                        else {
                            tableItems[rowIndex][columnIndex]!!.mWidth = width
                            break
                        }
                    }
                }
                tableItems[rowIndex][columnIndex]?.setFitSize()
            }
        }
    }

    private fun findMaxHeight(rowIndex: Int): Int {
        var maxHeight = 0
        for (columnIndex in table[rowIndex].indices) {
            if (tableItems[rowIndex][columnIndex] == null || table[rowIndex][columnIndex]!!.rowSpan > 1)
                continue
            maxHeight = maxHeight.coerceAtLeast(tableItems[rowIndex][columnIndex]!!.height)
        }
        return maxHeight
    }

    private fun findMaxWidth(columnIndex: Int): Int {
        var maxWidth = 0
        for (rowIndex in table.indices) {
            if (tableItems[rowIndex][columnIndex] == null || table[rowIndex][columnIndex]!!.colSpan > 1)
                continue
            maxWidth = maxWidth.coerceAtLeast(tableItems[rowIndex][columnIndex]!!.width)
        }
        return maxWidth
    }
}