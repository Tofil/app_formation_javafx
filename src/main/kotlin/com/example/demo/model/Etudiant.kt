package com.example.demo.model

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.SortedFilteredList
import tornadofx.getValue
import tornadofx.setValue

class Etudiant (numero:String, nom: String, prenom: String, formation: Formation?){
    val numeroProperty = SimpleStringProperty(numero)
    var numero by numeroProperty

    val nomProperty = SimpleStringProperty(nom)
    var nom by nomProperty

    val prenomProperty = SimpleStringProperty(prenom)
    var prenom by prenomProperty

    val formationProperty = SimpleObjectProperty<Formation>(formation)
    var formation by formationProperty

    override fun equals(other: Any?)
        =other is Etudiant
            && numero == other.numero




}