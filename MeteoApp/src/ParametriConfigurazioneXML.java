
import com.thoughtworks.xstream.*;
import java.io.*;
import java.nio.file.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class ParametriConfigurazioneXML{
    
    public String nomeUtenteApplicazione;
    public String nomeUtenteArchivio;
    public String passwordArchivio;
    public String indirizzoIPServerLog;
    public int portaServerLog;
    public String unitaTemperatura;
    public String unitaUmidita;
    public int numeroMaxCittaPreferite;
    
    public void deserializzaXML(){
        ParametriConfigurazioneXML c;
        String x = null;
        XStream xs = new XStream();
        try{
            x = new String(Files.readAllBytes(Paths.get("./myfiles/config/fileConfig.xml")));
            if(valida() && x != null){
                c = (ParametriConfigurazioneXML)xs.fromXML(x);
                nomeUtenteApplicazione = c.nomeUtenteApplicazione;
                nomeUtenteArchivio = c.nomeUtenteArchivio;
                passwordArchivio = c.passwordArchivio;
                indirizzoIPServerLog = c.indirizzoIPServerLog;
                portaServerLog = c.portaServerLog;
                unitaTemperatura = c.unitaTemperatura;
                unitaUmidita = c.unitaUmidita;
                numeroMaxCittaPreferite = c.numeroMaxCittaPreferite;
            }
        }catch(IOException ex){
            System.out.println("Errore deserializzazione file di configurazione");
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }   
    }
    
    private static boolean valida(){
        boolean ret = true;
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File("./myfiles/config/fileConfig.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./myfiles/config/schemaConfig.xsd")));
            s.newValidator().validate(new DOMSource(d));
        }catch(Exception ex) {
            ret = false;
            if(ex instanceof SAXException){
                System.out.println("Errore di validazione del file di configurazione");
            }else{
                System.out.println(ex.getMessage());
            }
        }
        return ret;
    }   
    
}
