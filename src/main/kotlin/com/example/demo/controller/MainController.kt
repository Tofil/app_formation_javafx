package com.example.demo.controller

import com.example.demo.app.Styles
import com.example.demo.model.*
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.importStylesheet

class MainController : Controller() {
    val ues = FXCollections.observableArrayList<UE>()
    val mentions = FXCollections.observableArrayList<Mention>()
    val formations = FXCollections.observableArrayList<Formation>()
    val etudiants = FXCollections.observableArrayList<Etudiant>()
    val ueSuivis = FXCollections.observableArrayList<UESuivi>()

    var selectedRole = SimpleStringProperty("Directeurs d’Etude")
    val roles = FXCollections.observableArrayList( "Directeurs d’Etude","Secrétariat Pédagogique", "Bureau Des Examens")

    fun initFromPath() {
        //TODO: ImportCSV


        formations.addAll(
            Formation("MIASH", "MIAGE"),
            Formation("MIAGE", "MDS")
        )

        ues.addAll(
            UE("UE27862", 3,formations[0]),
            UE("UE89062", 2,formations[0]),
            UE("UE24352", 6,formations[1]),
            UE("UE13572", 8)
        )

        etudiants.addAll(
            Etudiant("A1Z972", "ValJean", "Jean", formations[0]),
            Etudiant("A7S987F", "Orliac", "Théophile", formations[0]),
            Etudiant("68D5SQD", "Oliver", "John", formations[0])
        )

        ueSuivis.addAll(
            UESuivi(ues[0],etudiants[0], "2020", "Oui", "Oui", "Non"),
            UESuivi(ues[1],etudiants[0], "2021", "Non", "Oui", "Oui"),
            UESuivi(ues[1],etudiants[1], "2021", "Oui", "Non", "Non")
        )




    }

}