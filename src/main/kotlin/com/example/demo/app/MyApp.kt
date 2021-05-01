package com.example.demo.app

import com.example.demo.model.Formation
import com.example.demo.model.Mention
import com.example.demo.model.UE
import com.example.demo.view.MainView
import com.example.demo.view.StartPopup
import tornadofx.App
import tornadofx.importStylesheet

class MyApp: App(){
    override var primaryView = StartPopup::class

    init {
    }
}