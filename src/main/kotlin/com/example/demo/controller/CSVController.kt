package com.example.demo.controller

import com.example.demo.model.Etudiant
import com.example.demo.model.Formation
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Alert
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tornadofx.Controller
import tornadofx.alert
import java.io.File
import java.lang.Exception
import kotlin.collections.listOf as listOf

/**
 * Controlleur d'intéraction lecture/ecriture avec les fichiers csv
 *
 */
class CSVController : Controller() {
    private val mainCtrl: MainController by inject()
    private val fileList = listOf("\\formations.csv", "\\ues.csv", "\\etudiants.csv", "\\ueSuivis.csv")
    private var flagFhicherManquant: Boolean = false

    /**
     * Fonction de lecture csv
     */
    fun csvReader() = runBlocking {
        for (filename in fileList){
            try{
                val filePath = mainCtrl.folderPath.value + filename
                if (File(filePath).exists()) {
                    val job: Job = launch {
                        try{
                            com.github.doyaaaaaken.kotlincsv.dsl.csvReader().open(filePath) {
                                //println("Lecture de $filename")
                                readAllAsSequence().forEach { row: List<String> -> //Read as Sequence
                                    //println(row)
                                    itemFactory(filename, row, false)
                                }
                            }
                        }catch (e : com.github.doyaaaaaken.kotlincsv.util.CSVFieldNumDifferentException){
                            alert(
                                Alert.AlertType.ERROR,
                                "Erreur : CSV Malformé",
                                "Le fichier csv $filename semble mal formée, chaque ligne doit avoir le même nombre de séparateur"
                            )
                        }
                    }
                    if (filename == fileList[1]) {
                        job.join()
                        launch {
                            try{
                                com.github.doyaaaaaken.kotlincsv.dsl.csvReader().open(filePath) {
                                    //println("Lecture de $filename")
                                    readAllAsSequence().forEach { row: List<String> ->
                                        //println(row) //[a, b, c]
                                        itemFactory(filename, row, true)
                                    }
                                }
                            }catch (e : com.github.doyaaaaaken.kotlincsv.util.CSVFieldNumDifferentException){
                                alert(
                                    Alert.AlertType.ERROR,
                                    "Erreur : CSV Malformé",
                                    "Le fichier csv $filename semble mal formée, chaque ligne doit avoir le même nombre de séparateur"
                                )
                            }
                        }
                    }
                } else {
                    alert(
                        Alert.AlertType.ERROR,
                        "Erreur : Fichier manquant",
                        "Le fichier $filePath est introuvable"
                    )
                    flagFhicherManquant = true
                }
            }catch (e: Exception){
                alert(
                    Alert.AlertType.ERROR,
                    "Erreur : Exception",
                    e.message
                )
            }
        }
    }

    /**
     * Fonction de d'ecriture csv
     */
    fun csvWriter() = runBlocking {
        for (filename in fileList){
            val filePath = mainCtrl.folderPath.value + filename
            launch {
                //println("Ecriture de $filename")
                try{
                    val rows = ArrayList<ArrayList<String>>()
                    when (filename) {
                        fileList[0] -> {

                            for (formation in mainCtrl.formations) {
                                rows.add(arrayListOf(formation.mention, formation.parcours))
                            }
                        }
                        fileList[1] -> {
                            for (ue in mainCtrl.ues) {
                                val row =
                                    arrayListOf(ue.code, ue.nbCredits.toString(), mainCtrl.getConcatFormation(ue.formation))
                                for (uePrereq in ue.listeUePrereq) {
                                    if (uePrereq != null) {
                                        row.add(uePrereq.code)
                                    } else {
                                        row.add("")
                                    }
                                }
                                rows.add(row)
                            }

                            val maxRowLenght = (rows.maxByOrNull { it.size })?.size
                            for (row in rows) {
                                if (row.size < maxRowLenght!!) {
                                    for (i in row.size until maxRowLenght) {
                                        row.add("")
                                    }
                                }

                            }
                        }
                        fileList[2] -> {
                            for (etudiant in mainCtrl.etudiants) {
                                rows.add(
                                    arrayListOf(
                                        etudiant.numero,
                                        etudiant.nom,
                                        etudiant.prenom,
                                        mainCtrl.getConcatFormation(etudiant.formation)
                                    )
                                )
                            }
                        }
                        fileList[3] -> {
                            for (ueSuivi in mainCtrl.ueSuivis) {
                                rows.add(
                                    arrayListOf(
                                        ueSuivi.ue.code,
                                        ueSuivi.etu.numero,
                                        ueSuivi.annee,
                                        ueSuivi.valide.toString(),
                                        ueSuivi.semestrePair.toString(),
                                        ueSuivi.enCour.toString()
                                    )
                                )
                            }
                        }
                    }

                    com.github.doyaaaaaken.kotlincsv.dsl.csvWriter().writeAll(rows, filePath)
                }catch (e:Exception){
                    alert(
                        Alert.AlertType.ERROR,
                        "Erreur : Exception",
                        e.message
                    )
                }
            }
        }
    }

    /**
     * Fabrique d'objets Formation, Etudiant, UE, UESuivi
     *
     * @param name nom du fichier qui est en train d'être lu
     * @param list la liste de string de la ligne retournée par le csvReader
     * @param postCharger mode post postCharger :
     *      false (Prechargement) : on fabrique les objets ue uniquement avec leur code et nombre de crédit
     *      true : on ajoute aux objets ue la formation rattachée et leurs ue prérequis
     */
    private fun itemFactory(name : String, list : List<String>, postCharger : Boolean){
        if (postCharger){
            if(list.size > 2){
                for (eu in mainCtrl.ues){
                    if (eu.code == list[0]){

                        val forma = mainCtrl.getFormation(list[2])
                        var debut = 2
                        val listeUePrereq : ObservableList<UE> = FXCollections.observableArrayList()

                        if (forma != null){
                            eu.formation = forma
                            debut = 3
                        }

                        for (i in debut until list.size ){
                            val uePrereq = mainCtrl.getUE(list[i])
                            if (uePrereq != null )
                                listeUePrereq.add(uePrereq)
                        }

                        eu.listeUePrereq = listeUePrereq
                    }
                }
            }
        }
        else if (list.isNotEmpty()){
            when (name) {
                fileList[0] -> {
                    mainCtrl.formations.add(Formation(list[0], list[1]))
                }
                fileList[1] -> {
                    mainCtrl.ues.add(UE(list[0], list[1].toInt())) //Prechargement
                }
                fileList[2] -> {
                    mainCtrl.etudiants.add(Etudiant(list[0], list[1], list[2], mainCtrl.getFormation(list[3])))
                }
                fileList[3] -> {
                    mainCtrl.ueSuivis.add(UESuivi(mainCtrl.getUE(list[0]),
                        mainCtrl.getEtudiant(list[1]),
                        list[2],
                        list[3] == "true",
                        list[4] == "true",
                        list[5] == "true",
                    ))
                }
            }
        }
    }
}