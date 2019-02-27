
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;

public class ElencoCittaPreferite extends TableView<CittaPreferita>{
    private ObservableList<CittaPreferita> olCittaPreferite;
    private MeteoApp controller; //1
    public CittaPreferita cittaSelezionata;//2
    public ElencoCittaPreferite(MeteoApp controller){
        olCittaPreferite = FXCollections.observableArrayList();
        setLayoutX(2); setLayoutY(20);
        setPrefWidth(292);
        setPrefHeight(110);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.controller = controller;
        TableColumn colNomeCitta = new TableColumn("Città");
        colNomeCitta.setCellValueFactory(
                new PropertyValueFactory("nomeCitta"));
        colNomeCitta.setResizable(false);
        colNomeCitta.setSortable(false);
        colNomeCitta.setMaxWidth(290); colNomeCitta.setMinWidth(290);
        
        setOnMouseClicked((MouseEvent me)->{ //3
            cittaSelezionata = getSelectionModel().getSelectedItem();
            if(cittaSelezionata != null){
                controller.eseguiRicerca(cittaSelezionata.getNomeCitta());
                controller.setLog("SELEZIONE CITTA PREFERITA");
            }   
        });
        
        setItems(olCittaPreferite);
        getColumns().addAll(colNomeCitta);
        
    }
    public void aggiornaElencoCittaPreferite(ArrayList<String> lista){//4
        olCittaPreferite.clear();
        ArrayList<CittaPreferita> listaPreferite = new ArrayList<>();
        for(int i=0;i<lista.size(); i++){
            listaPreferite.add(new CittaPreferita(lista.get(i)));
        }
        olCittaPreferite.addAll(listaPreferite);
    }
    
}
/*
(1) Riferimento alla classe MeteoApp che permette di invocare il suo metodo eseguiRicerca().
(2) Memorizzo la cittaSelezionata in un campo public perché, la classe MeteoApp che gestisce il tasto rimuovi 
    sappia in ogni momento quel é la città selezionata all'interno della classe ElencoCittaPreferite.
(3) Intercetta l'evento di selezione di una riga all'interno della tabella.
    Quando una riga viene selezionata, memorizza l'elemento selezionato ed invoca
    la funzione eseguiRicerca() passando come nome città quello dell'elemento selezionato.
(4) Aggiorna la tabella delle citta preferite:
    - svuota l'observable list con le città preferite
    - converte l'arrayList di stringhe in un ArrayList<CittaPreferita>
    - setta l'observable list con i nuovi dati
    Questo metodo viene invocato dalla classe principale MeteoApp quando viene
    specificata una nuova citta preferita.
*/