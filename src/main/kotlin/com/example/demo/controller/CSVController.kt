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
import kotlin.collections.listOf as listOf

class CSVController : Controller() {
    private val mainCtrl: MainController by inject()
    private val fileList = listOf("\\formations.csv", "\\ues.csv", "\\etudiants.csv", "\\ueSuivis.csv")
    private var flagFhicherManquant: Boolean = false

    fun csvReader() = runBlocking {
        for (filename in fileList){
            val filePath = mainCtrl.folderPath.value + filename
            if(File(filePath).exists()){
                val job: Job = launch { com.github.doyaaaaaken.kotlincsv.dsl.csvReader().open(filePath) {
                    println("Lecture de $filename")
                    readAllAsSequence().forEach { row: List<String> -> //Read as Sequence
                        println(row)
                        itemFactory(filename, row, false)
                    }
                }
                }
                if (filename == fileList[1]){
                    job.join()
                    launch { com.github.doyaaaaaken.kotlincsv.dsl.csvReader().open(filePath) {
                        println("Lecture de $filename")
                        readAllAsSequence().forEach { row: List<String> ->
                            println(row) //[a, b, c]
                            itemFactory(filename, row, true)
                        }
                    }
                    }
                }
            }else{
                alert(
                    Alert.AlertType.ERROR,
                    "Erreur : Fichier manquant",
                    "Le fichier $filePath est introuvable"
                )
                flagFhicherManquant = true
            }
        }
    }

    fun csvWriter() = runBlocking {
        for (filename in fileList){
            val filePath = mainCtrl.folderPath.value + "\\test" + filename
            launch {
                println("Ecriture de $filename")
                val rows = ArrayList<ArrayList<String>>()
                when (filename) {
                    fileList[0] -> {

                        for (formation in mainCtrl.formations){
                            rows.add(arrayListOf(formation.mention,formation.parcours))
                        }
                    }
                    fileList[1] -> {
                        for (ue in mainCtrl.ues){
                            val row = arrayListOf(ue.code,ue.nbCredits.toString(), getConcatFormation(ue.formation))
                            for (uePrereq in ue.listeUePrereq){
                                if (uePrereq != null){
                                    row.add(uePrereq.code)
                                }else{
                                    row.add("")
                                }
                            }
                            rows.add(row)
                        }

                        val maxRowLenght = (rows.maxByOrNull { it.size })?.size
                        for (row in rows){
                            if (row.size < maxRowLenght!!){
                                for (i in row.size until maxRowLenght){
                                    row.add("")
                                }
                            }

                        }
                    }
                    fileList[2] -> {
                        for (etudiant in mainCtrl.etudiants){
                            rows.add(arrayListOf(etudiant.numero, etudiant.nom, etudiant.prenom, getConcatFormation(etudiant.formation)))
                        }
                    }
                    fileList[3] -> {
                        for (ueSuivi in mainCtrl.ueSuivis){
                            rows.add(arrayListOf(ueSuivi.ue.code, ueSuivi.etu.numero, ueSuivi.annee, ueSuivi.valide, ueSuivi.semestrePair, ueSuivi.enCour))
                        }
                    }
                }

                com.github.doyaaaaaken.kotlincsv.dsl.csvWriter().writeAll(rows, filePath)
            }
        }
    }

    private fun itemFactory(name : String, list : List<String>, postCharger : Boolean){
        if (postCharger){
            if(list.size > 2){
                for (eu in mainCtrl.ues){
                    if (eu.code == list[0]){

                        val forma = getFormation(list[2])
                        var debut = 2
                        val listeUePrereq : ObservableList<UE> = FXCollections.observableArrayList()

                        if (forma != null){
                            eu.formation = forma
                            debut = 3
                        }

                        for (i in debut until list.size ){
                            val uePrereq = getUE(list[i])
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
                    mainCtrl.etudiants.add(Etudiant(list[0], list[1], list[2], getFormation(list[3])))
                }
                fileList[3] -> {
                    mainCtrl.ueSuivis.add(UESuivi(getUE(list[0]),getEtudiant(list[1]), list[2], list[3], list[4], list[5]))
                }
            }
        }
    }

    private fun getConcatFormation(formation: Formation?): String {
        return if (formation != null) {
            formation.mention + formation.parcours
        } else {
            ""
        }
    }

    private fun getFormation(concat : String): Formation? {
        for (formation in mainCtrl.formations){
            if (concat == (formation.mention+formation.parcours)){
                return formation
            }
        }
        return null
    }

    private fun getUE(concat : String): UE? {
        for (ue in mainCtrl.ues){
            if (concat == ue.code){
                return ue
            }
        }
        return null
    }

    private fun getEtudiant(concat : String): Etudiant? {
        for (etudiant in mainCtrl.etudiants){
            if (concat == etudiant.numero){
                return etudiant
            }
        }
        return null
    }
}