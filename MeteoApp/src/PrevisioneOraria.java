
import java.text.*;
import java.util.*;

public class PrevisioneOraria {
    public String citta;
    public Date data;
    public Date oraInizio;
    public Date oraFine;
    public double tempmin;
    public double tempmax;
    public double temp;
    public int umidita;
    public double pioggia;
    public String descrizione;
    public String idIcona;
    
    public PrevisioneOraria(String citta, String inizio, String fine, double tempmin, double tempmax, double temp, int umidita, double pioggia, String descrizione, String idIcona){
        this.citta = citta;

        String[] app = inizio.split("T"); // 1
        String inizio_app = app[1];
        app = fine.split("T");
        String fine_app = app[1];
        try{// 2
            SimpleDateFormat formato_data = new SimpleDateFormat("yyyy-MM-dd");
            data = formato_data.parse(inizio);
            SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
            oraInizio = formato.parse(inizio_app);
            oraFine = formato.parse(fine_app);
        }catch(ParseException ex){
            System.out.println("Formato data non valido");
        }
        
        this.tempmin = tempmin;
        this.tempmax = tempmax;
        this.temp = temp;
        this.umidita = umidita;
        this.pioggia = pioggia;
        this.descrizione = descrizione;
        this.idIcona = idIcona;
    }
    public String getOraInizio(){
        
        SimpleDateFormat formato = new SimpleDateFormat("HH");
        return formato.format(oraInizio);
    }
    public String getOraFine(){
        SimpleDateFormat formato = new SimpleDateFormat("HH");
        return formato.format(oraFine);
    }
}
/*
(1) Separa la stringa nelle due porzioni prima e dopo il carattere 'T' e prende la 
    seconda parte perché contiene l'orario di inizio o di fine di una previsione
    oraria.
(2) Converto la data in un oggetto di classe Date e l'ora di inizio e di fine in 
    due oggetti di classe Date per comodità di utilizo.
*/