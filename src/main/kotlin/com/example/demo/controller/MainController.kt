package com.example.demo.controller

import com.example.demo.model.Etudiant
import com.example.demo.model.Formation
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.Controller
import tornadofx.chooseDirectory
import java.util.prefs.Preferences


class MainController : Controller() {
    val ues = FXCollections.observableArrayList<UE>()
    val formations = FXCollections.observableArrayList<Formation>()
    val etudiants = FXCollections.observableArrayList<Etudiant>()
    val ueSuivis = FXCollections.observableArrayList<UESuivi>()
    val pref = Preferences.userRoot().node(javaClass.name) //Preferance utilisateur (chemin dossier)
    var selectedRole = SimpleStringProperty("Directeurs d’Etude")
    val roles = FXCollections.observableArrayList( "Directeurs d’Etude","Secrétariat Pédagogique", "Bureau Des Examens")
    var folderPath = SimpleStringProperty(pref.get("App_Formation_folderPath", "Default_Path"))

    val csvHelper = CSVController()

    fun initFromPath() {
        //TODO: ImportCSV
        csvHelper.csvReader()
        pref.put("App_Formation_folderPath", folderPath.value)
        /*
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
        ues.addAll(
            UE("UE243252", 6,formations[1], FXCollections.observableArrayList(ues[0])),
            UE("UE244352", 6,formations[0], FXCollections.observableArrayList(ues[1]))
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
        )*/
    }

    fun setFolderPath(){
        folderPath.value = chooseDirectory("Select Target Directory")?.absolutePath
    }


}