package com.example.demo.controller

import com.example.demo.app.Styles
import com.example.demo.model.*
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

    fun initFromPath() {
        //TODO: ImportCSV
        ues.addAll(
            UE("UE27862", 3),
            UE("UE89062", 2),
            UE("UE24352", 6),
            UE("UE13572", 8)

        )
        mentions.addAll(
            Mention("MIASH", ues),
            Mention("MIAGE")

        )
        formations.addAll(
            Formation(mentions[0], "MIAGE"),
            Formation(mentions[1], "MDS")

        )
        etudiants.addAll(
            Etudiant("A1Z972", "ValJean", "Jean", formations[0]),
            Etudiant("A7S987F", "Orliac", "Théophile", formations[0]),
            Etudiant("68D5SQD", "Oliver", "John", formations[0])
        )
        ueSuivis.addAll(
            UESuivi(ues[0],etudiants[0], "2020", "Oui", "Oui", "Non"),
            UESuivi(ues[1],etudiants[0], "2021", "Non", "Oui", "Oui"),
            UESuivi(ues[1],etudiants[0], "2021", "Oui", "Non", "Non")
        )




    }

}