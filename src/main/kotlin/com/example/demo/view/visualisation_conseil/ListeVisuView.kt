package com.example.demo.view.visualisation_conseil

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.Etudiant
import tornadofx.*

class ListeVisuView : View("Visualisation et conseil") {
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()
    override val root = borderpane {
        center{
            tableview(ctrl.etudiants){
                readonlyColumn("Num Etudiant", Etudiant::numero)
                readonlyColumn("Nom", Etudiant::nom)
                readonlyColumn("Prenom", Etudiant::prenom)


                onUserSelect(clickCount = 1) { it ->
                    etuCtrl.selectedEtu = it
                    DetailsVisuView().openWindow()

                }
            }
        }
    }
    override fun onDock() {
        primaryStage.width = 1024.0
        primaryStage.height = 768.0
        primaryStage.centerOnScreen();

    }
}
