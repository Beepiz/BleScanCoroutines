package com.beepiz.blescancoroutines.sample

import android.content.Context
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.button
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.verticalLayout
import splitties.views.gravityCenterHorizontal

class MainUi(override val ctx: Context) : Ui {

    val btn = button()

    override val root = verticalLayout {
        add(btn, lParams(gravity = gravityCenterHorizontal))
    }
}
