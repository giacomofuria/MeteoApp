
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.beans.property.*;
import javafx.scene.image.*;

public class PrevisioneGiornaliera {
    // 1
    private SimpleDoubleProperty tempMin;
    private SimpleDoubleProperty tempMax;
    // 2
    private SimpleStringProperty descrizione;
    private SimpleStringProperty idIcona;
    private ImageView icona;
    // 3
    private Date data;
    // 4
    private ArrayList<PrevisioneOraria> listaPrevisioniOrarie;
    
    public PrevisioneGiornaliera(Date d, ArrayList<PrevisioneOraria> lista){
        listaPrevisioniOrarie = lista;
        data = d;
        tempMin=new SimpleDoubleProperty();
        tempMax=new SimpleDoubleProperty();
        descrizione = new SimpleStringProperty();
        idIcona=new SimpleStringProperty();
        icona = new ImageView();
        icona.setFitHeight(40); icona.setFitWidth(40);
        
        aggiornaTempMinMax(); // 5
        aggiornaDescrizioneEIcona(); // 6
    }
    
    private void aggiornaTempMinMax(){// 5
        double min = listaPrevisioniOrarie.get(0).tempmin;
        double max = listaPrevisioniOrarie.get(0).tempmax;
        for(int i=1; i<listaPrevisioniOrarie.size(); i++){
            if(listaPrevisioniOrarie.get(i).tempmin < min)
                min = listaPrevisioniOrarie.get(i).tempmin;
            
            if(listaPrevisioniOrarie.get(i).tempmax > max)
                max = listaPrevisioniOrarie.get(i).tempmax;
        }
        // min e max contentogo la minima e la massima della gioranta
        tempMin.set(min);
        tempMax.set(max);
    }
    
    private void aggiornaDescrizioneEIcona(){ // 6

        String percorsoIcona;
        int dim = listaPrevisioniOrarie.size();
        descrizione.setValue(listaPrevisioniOrarie.get((int)(dim/2)).descrizione);
            //previsioniIntervalli[(int) prevIntUsed/2].getCondMeteo());
        idIcona.setValue(listaPrevisioniOrarie.get((int)(dim/2)).idIcona);
            //previsioniIntervalli[(int) prevIntUsed/2].getIdIcona());
        percorsoIcona = "file:./myfiles/icone/"+
            listaPrevisioniOrarie.get((int)(dim/2)).idIcona+".png";
        icona = new ImageView(percorsoIcona);
    }
     public double getTempMin(){return tempMin.doubleValue();}
    public double getTempMax(){return tempMax.doubleValue();}
    public String getCondMeteo(){return descrizione.getValue();}
    public String getData(){
        SimpleDateFormat formato = new SimpleDateFormat("EEE d MMM");
        return formato.format(data);
    }
    public String getIdIcona(){return idIcona.getValue();}
    public ImageView getImage() {
        return icona;
    }
    public ArrayList<PrevisioneOraria> getPrevisioniOrarie(){
        return listaPrevisioniOrarie;
    }
}
/*
(1) Temperatura minima e massima nella giornata (dati riassuntivi)
(2) Descrizione meteo e icona riassuntive della giornata
(3) Data della previsione giornaliera
(4) ArrayList di previsioni orarie che caratterizzano la previsione giornaliera
(5) Calcola ed aggiorna le temperatura minima e massima nella giornata, scorrendo 
    tutte le previsioni orarie e prelevando la temperatura minima e massima su 
    tutte le previsioni orarie.
(6) Calcola e aggiorna la descrizione meteo e l'icona che descrivono la previsione
    meteo per l'intera giornata. Per semplicita viene scelto come dato rappresentativo 
    quello della previsione oraria che riguarda le ore centrali della giornata 
    (quindi l'elemento centrale della lista di previsioni orarie).
*/