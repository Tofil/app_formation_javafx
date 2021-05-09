package com.example.demo.controller

import com.example.demo.app.Styles
import com.example.demo.model.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*

class EtudiantController : Controller() {
    val mainCtrl: MainController by inject()
    var selectedEtu = Etudiant("", "", "", Formation("", ""))
}