
import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.annotations.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

public class LogEventiGUI {
    private String applicazione="MeteoApp";
    private String indirizzoIPClient;
    private String data;
    private String ora;
    private String evento; // 1

    public void setLog(String evento, String indirizzoIPServerLog, int portaServerLog){
        this.evento = evento;
        
        try {
            indirizzoIPClient = InetAddress.getLocalHost().toString();//2
        } catch (UnknownHostException ex) {
            System.out.println("Errore nella determinazione dell'indirizzo IP");
        }
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatoOra = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();//3
        data = formatoData.format(d);//4
        ora = formatoOra.format(d);//5
        String serialized = serializza();//6
        try(
                Socket s = new Socket(indirizzoIPServerLog, portaServerLog);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ){
            dos.writeUTF(serialized);//7
        }catch(IOException e){
            System.out.println("Errore invio log al server");
        }
    }
    
    private String serializza(){
        XStream xs = new XStream();
        return xs.toXML(this);
    }
}
/*
(1) Gli eventi trasmessi sono:
    - AVVIO
    - PRESSIONE PULSANTE CERCA
    - PRESSIONE PULSANTE AGGIUNGI A PREFERITE
    - PRESSIONE PULSANTE GRAFICO TEMPERATURA
    - PRESSIONE PULSANTE GRAFICO PIOGGIA
    - PRESSIONE PULSANTE GRAFICO UMIDITA
    - PRESSIONE PULSANTE RIMUOVI
    - SELEZIONE PREVISIONE
    - SELEZIONE CITTA PREFERITA
    - TERMINE
(2) Prelevo l'indirizzo IP locale ( file:///C:/prg/java8docs/api/java/net/InetAddress.html )
(3) Creo un nuovo oggetto date contenente il timestamp dell'istante in cui viene invocato
(4-5) Estraggo dall'oggetto Date la data (in formato dd-MM-yyyy) e l'ora (in formato HH:mm:ss)
      - classe Date file:///C:/prg/java8docs/api/java/util/Date.html
      - classe SimpleDateFormat file:///C:/prg/java8docs/api/java/text/SimpleDateFormat.html
(6) Serializza l'oggetto implicito
(7) Invia il log sotto forma testuale
*/
