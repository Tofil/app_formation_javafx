package com.example.demo.view

import com.example.demo.controller.MainController
import com.example.demo.view.visualisation_conseil.ListeInscriptionView
import com.example.demo.view.visualisation_conseil.ListeSaisieResView
import com.example.demo.view.visualisation_conseil.ListeVisuView
import javafx.geometry.Pos
import javafx.scene.image.Image
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import tornadofx.*

class StartPopup : View("App Formation") {
    val ctrl: MainController by inject()

    override val root = borderpane {
        top = hbox {
            imageview{
                image = Image("/images/logo.png")
                alignment = Pos.CENTER
                paddingTop = 10
            }
        }
        center {
           form{
               fieldset {
                   field  ("Chemin Actuel"){
                       label(ctrl.folderPath)
                   }
                   button("Changer") {
                       setOnAction {
                           ctrl.setFolderPath()
                       }
                   }
                   field ("Choisir un rôle") {
                       combobox(ctrl.selectedRole, ctrl.roles) {
                       }
                   }
                   button("OK") {
                       setOnAction {
                           ctrl.initFromPath()
                           if (ctrl.selectedRole.value == "Directeurs d’Etude")
                               this@StartPopup.replaceWith(ListeVisuView::class)
                           if (ctrl.selectedRole.value == "Secrétariat Pédagogique")
                               this@StartPopup.replaceWith(ListeInscriptionView::class)
                           if (ctrl.selectedRole.value == "Bureau Des Examens")
                               this@StartPopup.replaceWith(ListeSaisieResView::class)
                       }
                   }
               }
           }
        }
    }
    override fun onDock() {
        primaryStage.width = 400.0
        primaryStage.height = 280.0

        primaryStage.centerOnScreen()
    }
    override fun onBeforeShow() {
        super.onBeforeShow()
        val jMetro = JMetro(Style.LIGHT)
        if (currentStage != null) jMetro.scene = currentStage?.scene
    }
}
