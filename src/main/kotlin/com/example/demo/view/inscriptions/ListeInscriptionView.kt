package com.example.demo.view.visualisation_conseil

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.Etudiant
import com.example.demo.model.UE
import com.example.demo.view.TopBar
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import tornadofx.*

class ListeInscriptionView : View("Inscription") {
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()

    val filteredUEs = SortedFilteredList(ctrl.ues)
    val filteredEtus = SortedFilteredList(ctrl.etudiants);

    val selectedUEProperty = SimpleObjectProperty<UE>();
    var selectedUE by selectedUEProperty


    var filterMention = SimpleStringProperty("")
    var filterParcours = SimpleStringProperty("")

    private lateinit var tableEtu: TableView<Etudiant>


    override val root = borderpane {
        top = find(TopBar::class).root
        left {
            tabpane {
                tab("Liste des UE") {
                    vbox {
                        form {
                            fieldset {
                                field("Filtrer par mention") {
                                    textfield(filterMention).setOnAction { filteredUEs.refilter() }
                                }
                                field("Filtrer par parcours") {
                                    textfield(filterParcours).setOnAction { filteredUEs.refilter() }
                                }
                            }
                        }
                        tableview(filteredUEs) {
                            column("Code", UE::code)
                            column("Crédits", UE::nbCredits)
                            column<UE, String>("Mention") { if (it.value.formation != null) it.value.formation.mentionProperty else SimpleStringProperty() }
                            column<UE, String>("Parcours") { if (it.value.formation != null) it.value.formation.parcoursProperty else SimpleStringProperty() }

                            onUserSelect(clickCount = 1) { ue ->
                                selectedUE = ue
                                filteredEtus.refilter()
                                println("refilter selected:"+selectedUE.code)
                            }
                        }
                    }
                }
                tabClosingPolicy= TabPane.TabClosingPolicy.UNAVAILABLE
                minWidth = 500.0
            }
        }
        center {
            vbox {
                tabpane {
                    tab("Étudiants ayant prérequis") {
                        tableEtu =tableview(filteredEtus) {
                            readonlyColumn("Num Etudiant", Etudiant::numero)
                            readonlyColumn("Nom", Etudiant::nom)
                            readonlyColumn("Prenom", Etudiant::prenom)

                            selectionModel.selectionMode = SelectionMode.MULTIPLE
                        }
                    }
                    tabClosingPolicy= TabPane.TabClosingPolicy.UNAVAILABLE
                }
                vbox{
                    button("Ajouter la sélection"){
                        setOnAction{
                            val selectedEtus: ObservableList<Etudiant>? = tableEtu.getSelectionModel().selectedItems
                            if (selectedEtus != null) {
                                if (selectedEtus.size > 0) {
                                    etuCtrl.inscrireUE(selectedUE,selectedEtus)
                                }
                            }
                        }
                    }
                    button("Sauvegarder"){

                    }
                    spacing = 10.0
                    alignment = Pos.CENTER
                }
            }
        }
    }

    init {
        filteredUEs.predicate = { ue ->
            (ue.formation == null ||
                    (ue.formation != null && (ue.formation.mention.contains(filterMention.value)) &&
                            (ue.formation.parcours.contains(filterParcours.value))))
        }
        filteredEtus.predicate = { etu ->
            selectedUE !=null &&
                    ctrl.ueSuivis.filter { it.etu == etu }.map { it.ue }.any(selectedUE.listeUePrereq::contains)
        }
    }

    override fun onDock() {
        primaryStage.width = 1024.0
        primaryStage.height = 768.0
        primaryStage.centerOnScreen();
    }
}
