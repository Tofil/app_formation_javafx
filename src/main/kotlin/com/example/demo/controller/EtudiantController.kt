package com.example.demo.controller

import com.example.demo.app.Styles
import com.example.demo.model.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.time.Year

class EtudiantController : Controller() {
    val mainCtrl: MainController by inject()
    val selectedEtuProperty = SimpleObjectProperty<Etudiant>()
    var selectedEtu by selectedEtuProperty

    fun inscrireUE(selectedUE: UE?, selectedEtus: ObservableList<Etudiant>) {
        for(etu in selectedEtus){
          //  mainCtrl.ueSuivis.add(UESuivi(UE,etu, Year.now().value.toString(),))
        }
    }
}