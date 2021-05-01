package com.example.demo.view

import com.example.demo.controller.MainController
import com.example.demo.view.visualisation_conseil.ListeInscriptionView
import com.example.demo.view.visualisation_conseil.ListeSaisieResView
import com.example.demo.view.visualisation_conseil.ListeVisuView
import tornadofx.*

class StartPopup : View("App Formation") {
    val ctrl: MainController by inject()


    override val root = borderpane {
        prefHeight = 150.0;
        prefWidth = 300.0;
        center {
           form{
               fieldset {
                   field  ("Chemin Actuel"){
                       label("C:\\Users\\test\\app_formation")
                   }
                   button("Changer") {
                       setOnAction {
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
}
