
import java.text.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class MeteoApp extends Application {

    private ParametriConfigurazioneXML conf;
    private CacheCittaPreferite cache;
    private LogEventiGUI log;
    
    private boolean cachePresente; // 1
    private ArchivioDatiMeteo archivio;
    // 2
    private Pane sezioneMeteoAttuale;
    private Label etichettaMeteoAttuale;
    private Label nomeCitta;
    private Label temperatura;
    private Label umidita;
    private Label descrizione;
    private ImageView icona;
    // 3
    private Pane sezioneRicerca;
    private Label nomeUtente;
    private Label etichettaRicerca;
    private TextField campoRicerca;
    private Button pulsanteRicerca;
    private Button pulsanteAggiungiPreferita;
    private Label messaggio; // 4
    // 5
    private Pane sezionePrevisioni;
    private Label etichettaMeteoPrevisto;
    private ElencoPrevisioniGiornaliere tabellaPrevisioni;
    // 6
    private Pane sezioneGrafico;
    private Label etichettaGrafico;
    private AndamentoOrarioDelParametro grafico;
    private Button pulsanteGraficoTemperatura;
    private Button pulsanteGraficoPioggia;
    private Button pulsanteGraficoUmidita;
    private CategoryAxis asseX;
    private NumberAxis asseY;
    // 7
    private Pane sezioneCittaPreferite;
    private Label etichettaCittaPref;
    private ElencoCittaPreferite tabellaCittaPreferite;
    private Button rimuoviCittaPreferita;
    private int numeroCittaPreferiteAggiunte;
    public void start(Stage stage) {
        System.out.println("avvio applicazione");
        Group root = new Group();
        Scene scene = new Scene(root, 770, 700);
        scene.getStylesheets().add("file:./stile/stile.css");
        
        conf = new ParametriConfigurazioneXML();
        conf.deserializzaXML();
        
        log = new LogEventiGUI();
        setLog("AVVIO");
        cache = new CacheCittaPreferite();
        cachePresente = cache.caricaCache();
        archivio = new ArchivioDatiMeteo(conf.nomeUtenteArchivio, conf.passwordArchivio);
        
        creaSezioneMeteoAttuale();
        creaSezioneRicerca();
        creaSezionePrevisioni();
        creaSezioneGrafico();
        creaSezioneCittaPreferite();
        
        root.getChildren().addAll(sezioneMeteoAttuale, sezioneRicerca, sezionePrevisioni, sezioneGrafico, sezioneCittaPreferite);
        
        stage.getIcons().add(new Image("file:myfiles/icone/10d.png"));
        stage.setTitle("MeteoApp");
        stage.setScene(scene);
        stage.show();
        
        stage.setOnCloseRequest((WindowEvent e)->{
              System.out.println("Chiusura applicazione");
              cache.salvaCache(campoRicerca.getText(), cache.cittaPreferite, cache.numeroUltimaPrevisioneSelezionata, cache.tipoGrafico);
              setLog("TERMINE");
        }); 
    }

    private void creaSezioneMeteoAttuale(){
        sezioneMeteoAttuale = new Pane();
        sezioneMeteoAttuale.setStyle("-fx-background-color: #F9F9F9;");
        sezioneMeteoAttuale.setPrefSize(300,200);
        sezioneMeteoAttuale.setLayoutX(15); sezioneMeteoAttuale.setLayoutY(15);
        
        etichettaMeteoAttuale = new Label("Meteo attuale");
        etichettaMeteoAttuale.setLayoutX(8); 
        etichettaMeteoAttuale.setStyle("-fx-font-size: 13px; -fx-text-fill: #425F9C;");
        
        nomeCitta = new Label("Città");
        nomeCitta.setLayoutX(8); nomeCitta.setLayoutY(20);
        nomeCitta.setStyle("-fx-font-size: 25px; -fx-text-fill: #425F9C;");
        
        temperatura = new Label(conf.unitaTemperatura);
        temperatura.setLayoutX(4); temperatura.setLayoutY(45);
        temperatura.setStyle("-fx-font-size: 50px; -fx-text-fill: #425F9C;");
        
        icona = new ImageView();
        icona.setFitHeight(50); icona.setFitWidth(50);
        icona.setLayoutX(200); icona.setLayoutY(55);
        
        umidita = new Label("Umidità: ");
        umidita.setLayoutX(10); umidita.setLayoutY(120);
        umidita.setStyle("-fx-font-size: 18px; -fx-text-fill: #425F9C;");
        
        descrizione = new Label("Descrizione: ");
        descrizione.setLayoutX(10); descrizione.setLayoutY(150);
        descrizione.setStyle("-fx-font-size: 18px; -fx-text-fill: #425F9C;");
        sezioneMeteoAttuale.getChildren().addAll(etichettaMeteoAttuale,nomeCitta,temperatura,umidita,descrizione,icona);
    }
    private void creaSezioneRicerca(){
        sezioneRicerca = new Pane();
        sezioneRicerca.setStyle("-fx-background-color: #F9F9F9;");
        sezioneRicerca.setPrefSize(425,90);
        sezioneRicerca.setLayoutX(330); sezioneRicerca.setLayoutY(15);
        
        nomeUtente = new Label("Utente: "+conf.nomeUtenteApplicazione);
        nomeUtente.setLayoutX(30); nomeUtente.setLayoutY(10);
        nomeUtente.setStyle("-fx-font-size: 15px; "
                + "-fx-text-fill: #425F9C;");
        
        etichettaRicerca = new Label("Cerca città");
        etichettaRicerca.setLayoutX(30); etichettaRicerca.setLayoutY(35);
        etichettaRicerca.setStyle("-fx-font-size: 15px; "
                + "-fx-text-fill: #425F9C;");
        
        campoRicerca = new TextField((cachePresente)?cache.ultimaCittaCercata:"");
        campoRicerca.setLayoutX(130); campoRicerca.setLayoutY(35);
        
        pulsanteRicerca = new Button("Cerca");
        pulsanteRicerca.setLayoutX(300); pulsanteRicerca.setLayoutY(35);
        pulsanteRicerca.setMaxSize(47, 25);
        pulsanteRicerca.setMinSize(47, 25);
        pulsanteRicerca.setOnAction((ActionEvent ev)->{
            eseguiRicerca(campoRicerca.getText());
            setLog("PRESSIONE PULSANTE CERCA");
        });
        
        pulsanteAggiungiPreferita = new Button("+");
        pulsanteAggiungiPreferita.setLayoutX(350); pulsanteAggiungiPreferita.setLayoutY(35);
        Tooltip t = new Tooltip("Salva come preferita");
        pulsanteAggiungiPreferita.setTooltip(t);
        pulsanteAggiungiPreferita.setStyle("-fx-font-weight: bold;");
        pulsanteAggiungiPreferita.setMaxSize(25, 25);
        pulsanteAggiungiPreferita.setMinSize(25,25);
        pulsanteAggiungiPreferita.setOnAction((ActionEvent ev)->{
            if(!campoRicerca.getText().equals("")){
                aggiungiCittaPreferita(campoRicerca.getText());
                setLog("PRESSIONE PULSANTE AGGIUNGI A PREFERITE");
            }
            
        });
        
        messaggio = new Label();
        messaggio.setLayoutX(30); messaggio.setLayoutY(70);
        messaggio.setStyle("-fx-font-size: 15px; -fx-text-fill: #DA422F;");
        
        sezioneRicerca.getChildren().addAll(nomeUtente, etichettaRicerca, campoRicerca,pulsanteRicerca,pulsanteAggiungiPreferita, messaggio);
    }
    public void eseguiRicerca(String citta){
        campoRicerca.setText(citta);
        messaggio.setText(""); // 8
            if(!archivio.cittaPresente(citta)){
                messaggio.setText("Errore: città non trovata");
            }else{
                DatiMeteoAttuale dma = archivio.prelevaMeteoAttuale(citta);// 9
                aggiornaMeteoAttuale(dma); // 10
                ArrayList<PrevisioneOraria> listaPrevisioniOrarie = 
                        archivio.prelevaPrevisioniOrarie(citta);
                ArrayList<PrevisioneGiornaliera> listaPrevisioniGiornaliere = 
                        creaPrevisioniGiornaliere(listaPrevisioniOrarie); // 11
                tabellaPrevisioni.aggiornaElencoPrevisioniGiornaliere(listaPrevisioniGiornaliere, cache.numeroUltimaPrevisioneSelezionata); //12
            }
    }
    private void creaSezionePrevisioni(){
        sezionePrevisioni = new Pane();
        sezionePrevisioni.setStyle("-fx-background-color: #F9F9F9;");
        sezionePrevisioni.setPrefSize(425,275);
        sezionePrevisioni.setLayoutX(330); sezionePrevisioni.setLayoutY(115);
        
        etichettaMeteoPrevisto = new Label("Meteo Previsto");
        etichettaMeteoPrevisto.setLayoutX(4);
        etichettaMeteoPrevisto.setStyle(""
                + "-fx-font-size: 13px; "
                + "-fx-text-fill: #425F9C;");
        tabellaPrevisioni = new ElencoPrevisioniGiornaliere(this, conf.unitaTemperatura);
        sezionePrevisioni.getChildren().addAll(etichettaMeteoPrevisto, tabellaPrevisioni);
    }
    private void creaSezioneGrafico(){
        sezioneGrafico = new Pane();
        sezioneGrafico.setStyle("-fx-background-color: #F9F9F9;");
        sezioneGrafico.setPrefSize(740,295);
        sezioneGrafico.setLayoutX(15); sezioneGrafico.setLayoutY(395);
        
        etichettaGrafico = new Label("Grafico");
        etichettaGrafico.setLayoutX(4);
        etichettaGrafico.setStyle(""
                + "-fx-font-size: 13px; "
                + "-fx-text-fill: #425F9C;");
        if(!cachePresente){
            cache.tipoGrafico = 0;
        }
        
        asseX = new CategoryAxis();
        asseY = new NumberAxis();

        asseX.setLabel("Ora"); 
        String etichettaAsseY = "";
        switch(cache.tipoGrafico){
            case 1: etichettaAsseY = "Pioggia (mm)"; break;
            case 2: etichettaAsseY = "Umidità ("+conf.unitaUmidita+")";break;
            default: etichettaAsseY = "Temperatura ("+conf.unitaTemperatura+")"; break;
        }
        
        asseY.setLabel(etichettaAsseY);
        asseY.setUpperBound(40);
        grafico = new AndamentoOrarioDelParametro(asseX, asseY);
        
        creaEAttivaPulsantiGrafico();

        sezioneGrafico.getChildren().addAll(etichettaGrafico, grafico,pulsanteGraficoTemperatura, pulsanteGraficoPioggia,pulsanteGraficoUmidita);
    }
    private void creaSezioneCittaPreferite(){
        sezioneCittaPreferite = new Pane();
        sezioneCittaPreferite.setStyle("-fx-background-color: #F9F9F9;");
        sezioneCittaPreferite.setPrefSize(300,160);
        sezioneCittaPreferite.setLayoutX(15); sezioneCittaPreferite.setLayoutY(225);
        
        etichettaCittaPref = new Label("Città preferite");
        etichettaCittaPref.setLayoutX(4);
        etichettaCittaPref.setStyle(""
                + "-fx-font-size: 13px; "
                + "-fx-text-fill: #425F9C;");
        
        tabellaCittaPreferite = new ElencoCittaPreferite(this);
        rimuoviCittaPreferita = new Button("Rimuovi");
        rimuoviCittaPreferita.setLayoutX(215);
        rimuoviCittaPreferita.setLayoutY(132);
        rimuoviCittaPreferita.setMaxSize(80, 25);
        rimuoviCittaPreferita.setMinSize(80, 25);
        rimuoviCittaPreferita.setOnAction((ActionEvent ev)->{
            if(tabellaCittaPreferite.cittaSelezionata != null){
                String cittaDaEliminare = tabellaCittaPreferite.cittaSelezionata.getNomeCitta();
                for(int i=0; i<cache.cittaPreferite.size(); i++){ // 16
                    if(cache.cittaPreferite.get(i).equals(cittaDaEliminare)){
                        cache.cittaPreferite.remove(i);
                        numeroCittaPreferiteAggiunte--;
                        break;
                    }
                }
                tabellaCittaPreferite.aggiornaElencoCittaPreferite(cache.cittaPreferite);// 13
                setLog("PRESSIONE PULSANTE RIMUOVI");
            }
            
        });
        tabellaCittaPreferite.aggiornaElencoCittaPreferite(cache.cittaPreferite);
        sezioneCittaPreferite.getChildren().addAll(etichettaCittaPref, tabellaCittaPreferite, rimuoviCittaPreferita); 
    }
    private void aggiornaMeteoAttuale(DatiMeteoAttuale dma){
        nomeCitta.setText(dma.citta);
        temperatura.setText(dma.temperatura+" "+conf.unitaTemperatura);
        String percorsoIcona = "file:./myfiles/icone/"+dma.icona+".png";
        Image im = new Image(percorsoIcona);
        icona.setImage(im);
        umidita.setText("Umidità: "+dma.umidita+" "+conf.unitaUmidita);
        descrizione.setText("Descrizione: "+dma.descrizione);
    }
    private ArrayList<PrevisioneGiornaliera> creaPrevisioniGiornaliere(ArrayList<PrevisioneOraria> listaPrevisioniOrarie){//11
        ArrayList<PrevisioneGiornaliera> listaPrevisioniGiornaliere = new ArrayList<PrevisioneGiornaliera>();
        
        String dateFrom = null;
        Date dataIniziale = null;
        Date dataCorrente = null;
        ArrayList<PrevisioneOraria> listaPrevisioniOrarieGiornata = new ArrayList<>();
        for(int i=0; i<listaPrevisioniOrarie.size(); i++){
            dataCorrente = listaPrevisioniOrarie.get(i).data;
            if(!dataCorrente.equals(dataIniziale) && listaPrevisioniOrarieGiornata.size()==0){
                dataIniziale = dataCorrente;
            }  
            if(!dataCorrente.equals(dataIniziale) && listaPrevisioniOrarieGiornata.size()>0){
                listaPrevisioniGiornaliere.add(new PrevisioneGiornaliera(dataIniziale, listaPrevisioniOrarieGiornata));
                listaPrevisioniOrarieGiornata = new ArrayList<PrevisioneOraria>();
                dataIniziale = dataCorrente;
            }  
            listaPrevisioniOrarieGiornata.add(listaPrevisioniOrarie.get(i));
        }
        listaPrevisioniGiornaliere.add(new PrevisioneGiornaliera(dataIniziale, listaPrevisioniOrarieGiornata)); // per l'ultima giornata
        return listaPrevisioniGiornaliere;
    }
     
    private void creaEAttivaPulsantiGrafico(){
        pulsanteGraficoTemperatura = new Button("Temperatura");
        pulsanteGraficoTemperatura.setDisable(true);
        pulsanteGraficoTemperatura.setLayoutX(470);
        pulsanteGraficoTemperatura.setLayoutY(257);
        pulsanteGraficoTemperatura.setMaxSize(86,25);
        pulsanteGraficoTemperatura.setMinSize(86,25);

        pulsanteGraficoPioggia = new Button("Piogga");
        pulsanteGraficoPioggia.setDisable(true);
        pulsanteGraficoPioggia.setLayoutX(557);
        pulsanteGraficoPioggia.setLayoutY(257);
        pulsanteGraficoPioggia.setMaxSize(86,25);
        pulsanteGraficoPioggia.setMinSize(86,25);
        
        pulsanteGraficoUmidita = new Button("Umidità");
        pulsanteGraficoUmidita.setDisable(true);
        pulsanteGraficoUmidita.setLayoutX(644);
        pulsanteGraficoUmidita.setLayoutY(257);
        pulsanteGraficoUmidita.setMaxSize(86,25);
        pulsanteGraficoUmidita.setMinSize(86,25);
        
        switch(cache.tipoGrafico){
            case 1: 
                pulsanteGraficoTemperatura.setOpacity(0.5);
                pulsanteGraficoPioggia.setOpacity(1);
                pulsanteGraficoUmidita.setOpacity(0.5);
                break;
            case 2:
                pulsanteGraficoTemperatura.setOpacity(0.5);
                pulsanteGraficoPioggia.setOpacity(0.5);
                pulsanteGraficoUmidita.setOpacity(1);
                break;
            default: 
                pulsanteGraficoTemperatura.setOpacity(1);
                pulsanteGraficoPioggia.setOpacity(0.5);
                pulsanteGraficoUmidita.setOpacity(0.5);
                break;
        }
    }
    public void richiestaGrafico(int numeroPrevisione, PrevisioneGiornaliera previsione){
        cache.numeroUltimaPrevisioneSelezionata = numeroPrevisione;
        grafico.disegna(previsione, cache.tipoGrafico);
        // 14
        pulsanteGraficoTemperatura.setDisable(false);
        pulsanteGraficoPioggia.setDisable(false);
        pulsanteGraficoUmidita.setDisable(false);
        
        pulsanteGraficoTemperatura.setOnAction((ActionEvent ev)->{
            pulsanteGraficoTemperatura.setOpacity(1);
            asseY.setLabel("Temperatura ("+conf.unitaTemperatura+")");
            cache.tipoGrafico = 0;
            grafico.disegna(previsione,cache.tipoGrafico);
            pulsanteGraficoPioggia.setOpacity(0.5);
            pulsanteGraficoUmidita.setOpacity(0.5);
            setLog("PRESSIONE PULSANTE GRAFICO TEMPERATURA");
        });
        
        pulsanteGraficoPioggia.setOnAction((ActionEvent ev)->{
            pulsanteGraficoPioggia.setOpacity(1);
            asseY.setLabel("Pioggia (mm)");
            cache.tipoGrafico = 1;
            grafico.disegna(previsione,cache.tipoGrafico);
            pulsanteGraficoTemperatura.setOpacity(0.5);
            pulsanteGraficoUmidita.setOpacity(0.5);
            setLog("PRESSIONE PULSANTE GRAFICO PIOGGIA");
        });
        
        pulsanteGraficoUmidita.setOnAction((ActionEvent ev)->{
            pulsanteGraficoUmidita.setOpacity(1);
            asseY.setLabel("Umidità ("+conf.unitaUmidita+")");
            cache.tipoGrafico = 2;
            grafico.disegna(previsione,cache.tipoGrafico);
            pulsanteGraficoTemperatura.setOpacity(0.5);
            pulsanteGraficoPioggia.setOpacity(0.5);
            setLog("PRESSIONE PULSANTE GRAFICO UMIDITA");
        });
    }
    private void aggiungiCittaPreferita(String citta){// 15
        if(numeroCittaPreferiteAggiunte >= conf.numeroMaxCittaPreferite){
            messaggio.setText("Numero max di città preferite raggiunto");
            return;
        }
        boolean giaPresente = false;
        if(cache.cittaPreferite != null){
            for(int i=0; i<cache.cittaPreferite.size(); i++){
                if(cache.cittaPreferite.get(i).equals(citta)){
                    giaPresente = true;
                    break;
                }
            }
            if(!giaPresente){
                cache.cittaPreferite.add(citta);
                numeroCittaPreferiteAggiunte++;
            }
        }
        tabellaCittaPreferite.aggiornaElencoCittaPreferite(cache.cittaPreferite);
    }
    public void setLog(String evento){
        log.setLog(evento, conf.indirizzoIPServerLog, conf.portaServerLog);
    }
}
/*
(1) Flag che vale true se la cache è presente, false altrimenti
(2) Attributi della sezione meteo attuale
(3) Attributi della sezione di ricerca
(4) Label in cui vengono visualizzati eventuali messaggi per l'utente
(5) Attributi della sezione meteo previsto
(6) Attributi della sezione grafico
(7) Attributi della sezione citta preferite
(8) Ripulisce il campo "messaggio" in quanto potrebbero esserci rimasti dei 
    messaggi di errore precedenti.
(9-10) Preleva dall'archivio i dati meteo attuali ed aggiorna le informazioni 
       nell'interfaccia grafica
(11) Metodo che crea una lista di previsioni giornaliere a partire da una lista di previsioni orarie.
     Il metodo scorre tutta la lista delle previsioni orarie ed ogni volta che 
     incontra un cambio di data, crea una nuova PrevisioneGiornaliera.
(12) Aggiorna la tabella conle previsioni giornaliere
(13) Ogni volta che viene premuto il tasto rimuovi la tabella con le città 
     preferite viene aggiornata
(14) Dopo la richiesta di un grafico abilito i tasti ad essere premuti
(15) Metodo che permette di aggiungere una città alla lista delle preferite.
     Se l'utente ha raggiunto il numero massimo di città preferite allora mostra
     un messaggio di errore nel campo "messaggio". Se la città è già tra le preferite 
     non ne salva una copia e non incrementa il numero di città salvate dall'utente.
(16) Cerca la città all'interno dell'ArrayList di città preferite, quando la trova la rimuove ed esce.
*/