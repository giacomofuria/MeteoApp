
import java.util.*;
import javafx.scene.chart.*;

public class AndamentoOrarioDelParametro extends LineChart<String,Number>{

    public AndamentoOrarioDelParametro(Axis<String> asseX, Axis<Number> asseY) {
        super(asseX, asseY); //1
        setLayoutX(0); setLayoutY(10);
        setMinSize(740, 290);
        setMaxSize(740, 290);
        setAnimated(false);

    }

    public void disegna(PrevisioneGiornaliera previsioneGiornaliera, int tipoGrafico){
        if(getData().size()>0)
            getData().remove(0, getData().size());//2
        XYChart.Series series = new XYChart.Series();
        series.setName(previsioneGiornaliera.getData()); //3
        getData().add(series);
        ArrayList<PrevisioneOraria> previsioniOrarie = previsioneGiornaliera.getPrevisioniOrarie();
        for(int i=0; i < previsioniOrarie.size(); i++){//4
            Number x;
            switch(tipoGrafico){
                case 1: x = previsioniOrarie.get(i).pioggia; break;
                case 2: x = previsioniOrarie.get(i).umidita; break;
                default: x =previsioniOrarie.get(i).temp;break;
            }
            
            series.getData().add(
                new XYChart.Data<String, Number>(previsioniOrarie.get(i).getOraInizio(),x));           
        }
    }
    
}
/*
(1) Richiamo il costruttore della classe LineChart ( file:///C:/prg/javafx2docs/api/javafx/scene/chart/LineChart.html )
(2) Se nel grafico sono già presenti dei dati li rimuovo
(3) Imposta la data della previsione giornaliera come nome della serie 
    visualizzata dal grafico 
(4) Per ogni PrevisioneOraria che compone la PrevisioneGiornaliera estrae il 
    parametro da visualizzare (temperatura, umidita o pioggia) e l'ora di inizio
    dell'intervallo e lo aggiunge li aggiunge ai dati che il grafico dovrà visualizzare.
*/