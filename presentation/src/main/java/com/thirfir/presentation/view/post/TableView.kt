package com.thirfir.presentation.view.post

import android.content.Context
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.thirfir.presentation.model.TableElement

class TableView(
    context: Context,
    private val table: Array<Array<TableElement?>>,
) : ConstraintLayout(context) {

    private val ids = Array(table.size) { Array<Int?>(table[0].size) { null } }

    init {
        id = View.generateViewId()
        val constraintSet = ConstraintSet()

        for (rowIndex in table.indices) {
            for (columnIndex in table[rowIndex].indices) {
                if (table[rowIndex][columnIndex] == null)
                    continue

                val tableItem = TableItemView(context, table[rowIndex][columnIndex]!!)
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

}