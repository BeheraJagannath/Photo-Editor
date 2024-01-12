package com.example.neweditor.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.neweditor.customView.PuzzleUtils.getPuzzleLayout
import com.example.neweditor.customView.slant.NumberSlantLayout
import com.example.neweditor.customView.straight.NumberStraightLayout
import com.example.neweditor.data.PuzzleData
import com.xiaopo.flying.puzzle.PuzzleLayout
import com.xiaopo.flying.puzzle.PuzzleView
import com.xiaopo.flying.puzzle.SquarePuzzleView
import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout

fun getPuzzleData(layout: PuzzleLayout, arrBitmapSize: Int): PuzzleData {

    var theme: Int = 0

    if (layout is NumberSlantLayout) {
        theme = layout.theme
    } else if (layout is NumberStraightLayout) {
        theme = layout.theme
    }

    val type = if (layout is SlantPuzzleLayout) {
        0
    } else {
        1
    }


    val pieceSize = arrBitmapSize

    return PuzzleData(type = type, pieceSize = pieceSize, themeId = theme)


}


fun setUpPuzzleLayout(
    data: PuzzleData,
    bitmapArr: MutableList<Bitmap>,
    puzzleView: SquarePuzzleView
) {

    val TAG = "fun setUpPuzzleLayout()"

    val (type, pieceSize, themeId) = data
    val puzzleLayout = getPuzzleLayout(type, pieceSize, themeId)

    puzzleView.setPuzzleLayout(puzzleLayout)


    if (puzzleLayout.areaCount > bitmapArr.size) {
        for (i in 0 until puzzleLayout.areaCount) {
            puzzleView.addPiece(bitmapArr[i % bitmapArr.size])
        }
    } else {
        puzzleView.addPieces(bitmapArr)

    }


}


fun PuzzleView.getBitmap(): Bitmap {
    isNeedDrawLine=false
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap

}


