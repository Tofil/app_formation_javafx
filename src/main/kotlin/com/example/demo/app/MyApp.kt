package com.example.demo.app

import com.example.demo.view.StartPopup
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import tornadofx.App

class MyApp: App(){
    override var primaryView = StartPopup::class

    override fun start(stage: Stage) {
        super.start(stage)
        var jMetro = JMetro(Style.LIGHT)
        jMetro.scene = stage.scene
    }
}