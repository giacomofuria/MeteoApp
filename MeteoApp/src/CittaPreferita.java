
import javafx.beans.property.*;

public class CittaPreferita{
        public SimpleStringProperty nomeCitta;
        public CittaPreferita(String c){
            nomeCitta = new SimpleStringProperty(c);
        }
        public String getNomeCitta(){return nomeCitta.getValue();}
}