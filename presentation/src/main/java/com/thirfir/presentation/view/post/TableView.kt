package com.thirfir.presentation.custom

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.thirfir.domain.model.element.TableElement

class TableView(
    context: Context,
    private val table: List<List<TableElement?>>
) : ConstraintLayout(context) {

    private val paint = Paint()
    private val tableMaxWidth = 300
    private val itemMaxWidths = MutableList(table[0].size) { 0 }
    private val itemMaxHeights = MutableList(table.size) { 0 }
    private val ids = MutableList(table.size) { MutableList(table[0].size) { 0 } }
    private val spaces =
        MutableList(table.size) {
            MutableList<TableSpace?>(table[0].size) {
                TableSpace(
                    context,
                    mutableListOf()
                )
            }
        }

    private fun calcSize() {
        table.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, td ->
                var totalText = ""
                td?.textElement?.forEach {
                    totalText += it.text
                }
                itemMaxWidths[columnIndex] = Math.max(
                    itemMaxWidths[columnIndex],
                    paint.measureText(totalText).toInt(),
                )
                itemMaxWidths[columnIndex] = Math.min(
                    itemMaxWidths[columnIndex],
                    tableMaxWidth
                )
                val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top
                val desiredHeight =
                    (spaces[rowIndex][columnIndex]!!.paddingTop + textHeight + spaces[rowIndex][columnIndex]!!.paddingBottom).toInt()
                itemMaxHeights[rowIndex] = Math.max(itemMaxHeights[rowIndex], desiredHeight)

            }
        }
        table.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, td ->
                initSpace(rowIndex, columnIndex)
            }
        }
    }

    private fun initSpace(rowIndex: Int, columnIndex: Int) {
        if (table[rowIndex][columnIndex] != null) {
            spaces[rowIndex][columnIndex] = TableSpace(context, table[rowIndex][columnIndex]!!.textElement).also {
                it.id = View.generateViewId()
                ids[rowIndex][columnIndex] = it.id

                it.mHeight = itemMaxHeights[rowIndex]
                it.mWidth = itemMaxWidths[columnIndex]


                for (i in 1 until (table[rowIndex][columnIndex]?.rowSpan ?: 1)) {
                    it.mHeight += itemMaxHeights[rowIndex + i]
                }
                for (i in 1 until (table[rowIndex][columnIndex]?.colSpan ?: 1)) {
                    it.mWidth += itemMaxWidths[columnIndex + i]
                }
            }
        } else
            spaces[rowIndex][columnIndex] = null
    }

    init {
        paint.textSize = 100f
        calcSize()
        this.id = View.generateViewId()
        val constraintSet = ConstraintSet()

        spaces.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, space ->
                if (space != null) {

                    addView(space)
                    constraintSet.clone(this)
                    if (rowIndex == 0 && columnIndex == 0) {
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.TOP,
                            this.id,
                            ConstraintSet.TOP,
                            0
                        )
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.START,
                            this.id,
                            ConstraintSet.START,
                            0
                        )
                    } else if (rowIndex == 0) {
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.TOP,
                            this.id,
                            ConstraintSet.TOP,
                            0
                        )
                        var columnIter = columnIndex - 1
                        while (spaces[rowIndex][columnIter] == null)
                            --columnIter
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.START,
                            ids[rowIndex][columnIter],
                            ConstraintSet.END,
                            0
                        )
                    } else if (columnIndex == 0) {

                        var rowIter = rowIndex - 1
                        while (spaces[rowIter][columnIndex] == null)
                            --rowIter
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.TOP,
                            ids[rowIter][columnIndex],
                            ConstraintSet.BOTTOM,
                            0
                        )
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.START,
                            this.id,
                            ConstraintSet.START,
                            0
                        )
                    } else {

                        // 수직 방향 Constraint
                        var columnIter = columnIndex
                        while (spaces[rowIndex - 1][columnIter] == null && columnIter > 0)
                            --columnIter
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.TOP,
                            ids[rowIndex - 1][columnIter],
                            ConstraintSet.BOTTOM,
                            0
                        )

                        // 수평 방향 Constraint
                        var rowIter = rowIndex
                        while (spaces[rowIter][columnIndex - 1] == null && rowIter > 0)
                            --rowIter
                        constraintSet.connect(
                            space.id,
                            ConstraintSet.START,
                            ids[rowIter][columnIndex - 1],
                            ConstraintSet.END,
                            0
                        )
                    }
                    constraintSet.applyTo(this)
                }
            }
        }
    }
}