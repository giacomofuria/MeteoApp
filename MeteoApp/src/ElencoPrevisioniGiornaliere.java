
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class ElencoPrevisioniGiornaliere extends TableView<PrevisioneGiornaliera> {
    private ObservableList<PrevisioneGiornaliera> olPrevisioni;
    private MeteoApp controller;//1
    public ElencoPrevisioniGiornaliere(MeteoApp controller, String unitaTemperatura){
        olPrevisioni = FXCollections.observableArrayList();
        this.controller = controller;
        setLayoutX(2); setLayoutY(20); setPrefWidth(420); setPrefHeight(253);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn colData = new TableColumn("Giorno");
        colData.setCellValueFactory(new PropertyValueFactory("data"));
        colData.setResizable(false); colData.setSortable(false);
        colData.setMaxWidth(88); colData.setMinWidth(88);
        TableColumn colIcona = new TableColumn<PrevisioneGiornaliera, ImageView>("Icona");
        colIcona.setCellValueFactory(
                new PropertyValueFactory<PrevisioneGiornaliera, ImageView>("image"));
        colIcona.setResizable(false); colIcona.setSortable(false);
        colIcona.setMaxWidth(60); colIcona.setMinWidth(60); 
        TableColumn colCondMeteo = new TableColumn("Descrizione");
        colCondMeteo.setCellValueFactory(
                new PropertyValueFactory("condMeteo"));
        colCondMeteo.setResizable(false); colCondMeteo.setSortable(false);
        colCondMeteo.setMaxWidth(120); colCondMeteo.setMinWidth(120);
        TableColumn colMin = new TableColumn("Min ("+unitaTemperatura+")");
        colMin.setCellValueFactory(new PropertyValueFactory("tempMin"));
        colMin.setResizable(false); colMin.setMaxWidth(75); colMin.setMinWidth(75);
        TableColumn colMax = new TableColumn("Max ("+unitaTemperatura+")");
        colMax.setCellValueFactory(new PropertyValueFactory("tempMax"));
        colMax.setResizable(false); colMax.setMaxWidth(75); colMax.setMinWidth(75);
        setItems(olPrevisioni); 
        setOnMouseClicked((MouseEvent me)->{
            PrevisioneGiornaliera prevSelezionata = getSelectionModel().getSelectedItem();
            int rigaSelezionata = getSelectionModel().getSelectedIndex();
            
            if(prevSelezionata != null){ //2
                controller.richiestaGrafico(rigaSelezionata, prevSelezionata);//3
            }
            controller.setLog("SELEZIONE PREVISIONE"); //4
        });
        
        getColumns().setAll(colData, colIcona, 
                colCondMeteo, colMin, colMax);
    }
    
    public void aggiornaElencoPrevisioniGiornaliere(ArrayList<PrevisioneGiornaliera> listaPrevisioniGiornaliere, int numeroUltimaPrevisioneSelezionata){ //5
        olPrevisioni.clear();
        olPrevisioni.addAll(listaPrevisioniGiornaliere);
        getSelectionModel().select(numeroUltimaPrevisioneSelezionata);
    }
    
}
/*
(1) Riferimento alla classe MeteoApp che permette di invocare i suoi metodi:
    - richiestaGrafico() per creare un grafico con la previsione selezionata
    - setLog() per inviare un log di "selezione previsione"
(2) Controllo non strettamente necessario
(3) Chiamo il metodo richiestaGrafico() della classe MeteoApp
(4) Chiamo il metodo setLog() della classe MeteoApp che a sua volta userà la 
    classe LogEventiGUI per inviare log al server di log.
(5) Aggiorna la tabella contenente le previsioni con la lista delle previsioni 
    giornaliere e selezionando l'elemento salvato in cache.
*/