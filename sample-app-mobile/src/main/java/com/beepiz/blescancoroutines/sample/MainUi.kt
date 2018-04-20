package com.beepiz.blescancoroutines.sample

import android.content.Context
import android.view.Gravity
import splitties.viewdsl.appcompat.button
import splitties.viewdsl.core.Ui
import splitties.viewdsl.core.add
import splitties.viewdsl.core.lParams
import splitties.viewdsl.core.v
import splitties.viewdsl.core.verticalLayout

class MainUi(override val ctx: Context) : Ui {

    val btn = v(::button)

    override val root = v(::verticalLayout) {
        add(btn, lParams(gravity = Gravity.CENTER_HORIZONTAL))
    }
}
