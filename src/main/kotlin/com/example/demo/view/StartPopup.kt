package com.example.demo.view

import com.example.demo.controller.MainController
import com.example.demo.view.visualisation_conseil.ListVisuView
import tornadofx.*

class StartPopup : View("App Formation") {
    val ctrl: MainController by inject()
    override val root = borderpane {
        prefHeight = 150.0;
        prefWidth = 300.0;
        center {
           vbox {
               label("Chemin Actuel")
               label("C:\\Users\\test\\app_formation")
               button("Changer") {
                   setOnAction {
                   }
               }
               label("Choisir un rôle")
                //label()
               button("OK") {
                   setOnAction {
                       ctrl.initFromPath()
                       this@StartPopup.replaceWith(ListVisuView::class)
                   }
               }
           }
        }
    }
}
