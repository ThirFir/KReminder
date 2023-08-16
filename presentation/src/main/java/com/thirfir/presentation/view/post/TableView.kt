package com.thirfir.presentation.view.post

import android.content.Context
import android.util.Log
import android.view.View
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
        id = View.generateViewId()
        val constraintSet = ConstraintSet()

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

        for (rowIndex in table.indices) {
            for (columnIndex in table[rowIndex].indices) {
                val tableElement = table[rowIndex][columnIndex] ?: continue
                if(tableElement.rowSpan == 1 && tableElement.colSpan == 1)
                    continue

                if(tableElement.rowSpan > 1) {
                    for(c in table[rowIndex].indices) {
                        var nulled = false
                        if(c == columnIndex)
                            continue
                        var height = 0
                        for(r in 0 until tableElement.rowSpan) {
                            if(tableItems[rowIndex + r][c] == null)
                                nulled = true
                            else {
                                height += tableItems[rowIndex + r][c]!!.mHeight
                            }
                        }
                        if(nulled)
                            continue
                        else {
                            tableItems[rowIndex][columnIndex]!!.mHeight = height
                            break
                        }
                    }
                }

                if(tableElement.colSpan > 1) {
                    for(r in table.indices) {
                        var nulled = false
                        if(r == rowIndex)
                            continue
                        var width = 0
                        for(c in 0 until tableElement.colSpan) {
                            if(tableItems[r][columnIndex + c] == null)
                                nulled = true
                            else
                                width += tableItems[r][columnIndex + c]!!.mWidth
                        }
                        if(nulled)
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
}