
import java.sql.*;
import java.util.*;

public class ArchivioDatiMeteo {
    private Connection con; 
    public ArchivioDatiMeteo(String nomeUtente, String password){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/archiviometeoapp",nomeUtente,password);
        }catch(SQLException e){
            System.err.println("Errore comunicazione con archivio dati meteo");
        }
    }
    public boolean cittaPresente(String nomeCitta){ //1
        try{
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS ESITO FROM meteoattuale WHERE citta=?");
            ps.setString(1, nomeCitta);
            ResultSet rs = ps.executeQuery();
            if(rs.next() && rs.getInt("ESITO")>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            System.err.println("Errore esecuzione query");
        }
        return false;
    }
    
    public DatiMeteoAttuale prelevaMeteoAttuale(String nomeCitta){//2
        DatiMeteoAttuale dma = null;
        try{
            PreparedStatement ps = con.prepareStatement("SELECT citta, temperatura, umidita, descrizione, icona FROM meteoattuale WHERE citta=?");
            ps.setString(1,nomeCitta);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                dma = new DatiMeteoAttuale(rs.getString("citta"),rs.getDouble("temperatura"),rs.getInt("umidita"),rs.getString("descrizione"),rs.getString("icona"));
            }
        }catch(SQLException e){
            System.err.println("Errore esecuzione query");
        }
        return dma;
    }
    
    public ArrayList<PrevisioneOraria> prelevaPrevisioniOrarie(String nomeCitta){//3
        ArrayList<PrevisioneOraria> listaPrevisioniOrarie = new ArrayList<PrevisioneOraria>();
        try{
           PreparedStatement ps = con.prepareStatement("SELECT citta,inizio,fine,tempmin,tempmax,temp,umidita,pioggia,descrizione,icona FROM previsioni WHERE citta = ?"); 
           ps.setString(1, nomeCitta);
           ResultSet rs = ps.executeQuery();
           while(rs.next()){
               String citta = rs.getString("citta");
               String inizio = rs.getString("inizio");
               String fine = rs.getString("fine");
               double tempmin = rs.getDouble("tempmin");
               double tempmax = rs.getDouble("tempmax");
               double temp = rs.getDouble("temp");
               int umidita = rs.getInt("umidita");
               double pioggia = rs.getDouble("pioggia");
               String descrizione = rs.getString("descrizione");
               String icona = rs.getString("icona");
               listaPrevisioniOrarie.add(new PrevisioneOraria(citta,inizio,fine,tempmin,tempmax,temp,umidita,pioggia,descrizione,icona));
           }
        }catch(SQLException e){
            System.err.println("Errore esecuzione query");
        }
        return listaPrevisioniOrarie;
    }
}
/*
(1) Restituisce true se la città cercata è presente nel database, 
    false altrimenti.
(2) Dato il nome di una città, restituisce un oggetto di classe DatiMeteoAttuale
    contenente le condizioni meteo attuali sulla città cercata.
(3) Dato il nome di una città, restituisce un ArrayList di oggetti PrevisioneOraria
    contenente le PrevisioniGiornaliere per i 5 giorni successivi a quello in 
    cui viene effettuata la richuesta. Non restituisce un oggetto PrevisioneGiornaliera
    perché questa è una classe del back-end che si occupa soltanto di prelevare 
    i dati grezzi e di consegnarli ad una classe del middleware che farà le elaborazioni.
*/