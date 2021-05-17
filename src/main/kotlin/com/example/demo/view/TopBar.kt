package com.example.demo.view

import com.example.demo.app.MyApp
import com.example.demo.view.visualisation_conseil.ListeVisuView
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.stage.Stage
import tornadofx.*
import java.awt.Desktop
import java.net.URI

class TopBar : View() {
    override val root =
        menubar {
            menu {
                graphic = button("accueil")
                (graphic as Button).setOnAction {
                    primaryStage.scene.root = find<StartPopup>().root


                }
            }
            menu {
                graphic = button("Aide")
                (graphic as Button).setOnAction {
                    Desktop.getDesktop().browse( URI("http://www.example.com"));

                }
            }
        }
}