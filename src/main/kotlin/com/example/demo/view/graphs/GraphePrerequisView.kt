package com.example.demo.view.graphs

import com.example.demo.controller.EtudiantController
import com.example.demo.controller.MainController
import com.example.demo.model.UE
import com.example.demo.model.UESuivi
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph
import org.graphstream.stream.ProxyPipe
import org.graphstream.stream.SinkAdapter
import org.graphstream.ui.fx_viewer.FxDefaultView
import org.graphstream.ui.fx_viewer.FxViewer
import org.graphstream.ui.layout.Layouts
import org.graphstream.ui.view.Viewer
import tornadofx.Fragment
import tornadofx.hbox
import tornadofx.label
import tornadofx.vbox
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream


class GraphePrerequisView : Fragment("Graphe des prérequis") {
    private lateinit var suiviesParEtu: FilteredList<UESuivi>
    val ctrl: MainController by inject()
    val etuCtrl: EtudiantController by inject()
    var graph: Graph = SingleGraph("graph")

    override val root = vbox {
        hbox {
            label("Légende ")
            label("Suivie : bleu ")
            label("Valide : vert ")
            label("Possible : blanc ")
            label("Impossible : rouge ")
        }
        System.setProperty("org.graphstream.ui", "javafx")
    }

    init {
        print("Graphe")
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer")
        graph = SingleGraph("graph")
        loadCSS()
        loadGraph()
        val viewer: Viewer = FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD)
        val algo = Layouts.newLayoutAlgorithm()
        algo.stabilizationLimit = 0.000000000000000000000000000000000000000000000000000000000000000000000001
        algo.quality = 4.0
        algo.force = 2.0
        viewer.enableAutoLayout(algo)

        val view = viewer.addDefaultView(true) as FxDefaultView
        this.add(view)
    }

    private fun loadGraph() {
        suiviesParEtu = ctrl.ueSuivis.filtered { ue -> ue.etu.equals(etuCtrl.selectedEtu) }

        for (ue in ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation }) {
            val node = graph.addNode(ue.code)
            node.setAttribute("ui.label", ue.code)
            node.setAttribute("ui.class", "impossible")
            //node.setAttribute("layout.weight", 100000.0)
            node.setAttribute("layout.weight", 5)
//            node.setAttribute("layout.weight", 0.2)
            drawPrerequis(null, ue.listeUePrereq)
        }
        for (ue in suiviesParEtu.filter { ue -> ue.ue.formation == etuCtrl.selectedEtu.formation}) {
            val node = graph.getNode(ue.ue.code)
            node.setAttribute("ui.class", "suivie")
            if (ue.valide) {
                node.setAttribute("ui.class", "valide")
            }
        }
        setPossible()
    }

    private fun setPossible() {
        for (ue in ctrl.ues.filter { ue -> ue.formation == etuCtrl.selectedEtu.formation}) {
            println(ue.listeUePrereq.size)
            println(ue.code)
            if (!ue.listeUePrereq.any()) {
                val node = graph.getEdge(ue.code)
                if (node != null) {
                    node.setAttribute("ui.class", "possible") // dont work ...
                }
            }
        }
    }

    private fun removeEdges(edges: Stream<Edge>) {
        for (edge in edges)
            if (edge != null)
                graph.removeEdge(edge.id)

    }

    private fun removeNodes(nodes: Stream<Node>) {
        for (node in nodes)
            if (node != null)
                graph.removeNode(node.id)
    }


    private fun drawPrerequis(ueRoot: UE?, listeUePrereq: ObservableList<UE>) {
        for (ue in listeUePrereq) {
            if (ueRoot != null && null == graph.getEdge(ueRoot.code + "/" + ue.code)) {
                val edge = graph.addEdge(ueRoot.code + "/" + ue.code, ueRoot.code, ue.code, true)
                edge.setAttribute("layout.weight", 10)
            }
            if (ue.listeUePrereq.size > 0) {
                drawPrerequis(ue, ue.listeUePrereq)
            }else {
                graph.getEdge(ue.code)?.setAttribute("ui.class", "possible")
            }
        }
    }

    private fun loadCSS() {
        val cssURI = javaClass.classLoader.getResource("stylesheet.css")?.toURI()
        val lines = Files.readAllLines(Paths.get(cssURI), StandardCharsets.UTF_8)
        graph.setAttribute("ui.stylesheet", lines.joinToString("\n"))
    }


}
