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

    /**
     * Fonction d'initialisation de l'application, appelle la lecture de tous les csv et set le path entrer en préférence d'application
     */
    fun initFromPath() {
        csvHelper.csvReader()
        pref.put("App_Formation_folderPath", folderPath.value)
    }

    /**
     * Fonction de sélection du dossier contenant tous les csv utiliser par l'application
     */
    fun setFolderPath(){
        folderPath.value = chooseDirectory("Select Target Directory")?.absolutePath
    }

    /**
     * Fonction de concaténation de la mention et du parcours d'une formation
     * @param formation la formation à concaténer
     * @return la chaine de caractère de la formation concaténé, si la formation passer en paramètre et null renvoie une chaine vide
     */
    fun getConcatFormation(formation: Formation?): String {
        return if (formation != null) {
            formation.mention + formation.parcours
        } else {
            ""
        }
    }

    /**
     * Fonction de recherche d'une formation par le string concaténé de la mention et du parcours de la formation
     * @param concat string concaténé à rechercher
     * @return la formation correspondante, si aucune formation ne correspond renvoie null
     */
    fun getFormation(concat : String): Formation? {
        for (formation in formations){
            if (concat == (formation.mention+formation.parcours)){
                return formation
            }
        }
        return null
    }

    /**
     * Fonction de recherche d'une UE par le code d'UE
     * @param code code de l'UE à rechercher
     * @return l'UE correspondante, si aucune UE ne correspond renvoie null
     */
    fun getUE(code : String): UE? {
        for (ue in ues){
            if (code == ue.code){
                return ue
            }
        }
        return null
    }

    /**
     * Fonction de recherche d'un Etudiant par le numero d'Etudiant
     * @param numero code de l'UE à rechercher
     * @return l'Etudiant correspondante, si aucune Etudiant ne correspond renvoie null
     */
    fun getEtudiant(numero : String): Etudiant? {
        for (etudiant in etudiants){
            if (numero == etudiant.numero){
                return etudiant
            }
        }
        return null
    }

}