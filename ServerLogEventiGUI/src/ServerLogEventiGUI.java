import java.io.*;
import java.net.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class ServerLogEventiGUI {
    
    public static void main(String[] args) {
        System.out.println("ServerLogEventiGUI avviato");
        riceviXML();
    }
    
    public static void riceviXML(){// 1
        try(
            ServerSocket serv = new ServerSocket(80);
        ){
            while(true){
                try(
                    Socket s = serv.accept();
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                ){
                    String log = dis.readUTF();
                    boolean valida = valida(log);
                    if(valida){
                        System.out.println("Log ricevuto e validato correttamente");
                    }
                }catch(Exception e){
                    System.out.println("Errore nella ricezione del log");
                }
            }
        }catch(IOException e){
            System.out.println("Errore apertura ServerSocket");
        }
    }
    
    private static boolean valida(String log){
        boolean ret = true;
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            //  2
            InputStream is = new ByteArrayInputStream(log.getBytes("UTF-8"));
            Document d = db.parse(is);
            Schema s = sf.newSchema(new StreamSource(new File("./myfiles/log/schemaLog.xsd")));
            s.newValidator().validate(new DOMSource(d));
            
            BufferedWriter bfTotale = new BufferedWriter(new FileWriter("./myfiles/log/log.xml", true));
            bfTotale.write(log);
            bfTotale.newLine();
            bfTotale.flush();

        }catch(Exception ex) {
            ret = false;
            if(ex instanceof SAXException){
                System.out.println("Errore di validazione del file di log");
            }else{
                System.out.println("Problema nella lettura del file");
            }
        }
        return ret;
    }    
}
/*
(1) Riceve una stringa e la valida mediante il metodo valida()
(2) Crea un InputStream a partire da una stringa e lo passe al metodo parse. In questo
    modo non è necessario creare un file xml ogni volta che si riceve un nuovo log.
    file:///C:/prg/java8docs/api/javax/xml/parsers/DocumentBuilder.html
    Nel caso in cui si verifichi un errore di validazione o di lettura del file 
    di log complessivo stampa a video un messaggio di errore.
*/