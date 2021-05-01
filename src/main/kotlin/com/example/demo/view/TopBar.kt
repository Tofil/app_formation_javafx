package com.example.demo.view

import javafx.scene.Parent
import tornadofx.*

class TopBar : View() {
    override val root =
        menubar {
            menu("Fichier") {
                menu("Connect") {
                    item("Facebook")
                    item("Twitter")
                }
                item("Save")
                item("Quit")
            }
            menu("Edit") {
                item("Copy")
                item("Paste")
            }
        }
}