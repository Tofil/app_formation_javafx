package com.example.demo.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class UESuivi(ue:UE?, etu:Etudiant?, annee:String, valide:String, semestrePair:String, enCour:String) {
    val ueProperty = SimpleObjectProperty<UE>(ue)
    var ue by ueProperty

    val etuProperty = SimpleObjectProperty<Etudiant>(etu)
    var etu by etuProperty


    val anneeProperty = SimpleStringProperty(annee)
    var annee by anneeProperty

    val semestrePairProperty = SimpleStringProperty(semestrePair)
    var semestrePair by semestrePairProperty


    val valideProperty = SimpleStringProperty(valide)
    var valide by valideProperty

    val enCourProperty = SimpleStringProperty(enCour)
    var enCour by enCourProperty

}
