package com.example.demo.view

import tornadofx.*

class MainView : View("Suivi Formations") {
    //val mainCtrl
    override val root = borderpane {
        top = find(TopBar::class).root
        center = hbox {


        }
    }
    init {

    }
    override fun onDock() {
        primaryStage.width = 1024.0
        primaryStage.height = 768.0
    }
}
