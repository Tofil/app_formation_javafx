package com.example.demo.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class Formation(mention: Mention, nomParcour: String) {
    val mentionProperty = SimpleObjectProperty<Mention>(mention)
    var mention by mentionProperty

    val nomParcourProperty = SimpleStringProperty(nomParcour)
    var nomParcour by nomParcourProperty

}