
import java.io.*;
import java.util.*;

public class CacheCittaPreferite implements Serializable{
    public String ultimaCittaCercata;
    public ArrayList<String> cittaPreferite;
    public int numeroUltimaPrevisioneSelezionata;
    public int tipoGrafico;
    
    public CacheCittaPreferite(){//1
        cittaPreferite = new ArrayList<String>();
    }
    
    public void salvaCache(String ultimaCittaCercata, ArrayList<String> cittaPreferite, int numeroUltimaPrevisioneSelezionata, int tipoGrafico){//2
        this.ultimaCittaCercata = ultimaCittaCercata;
        this.cittaPreferite = cittaPreferite;
        this.numeroUltimaPrevisioneSelezionata = numeroUltimaPrevisioneSelezionata;
        this.tipoGrafico = tipoGrafico;
        
        try(
            FileOutputStream fos = new FileOutputStream("./myfiles/cache/cache.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(this);
        }catch(Exception e){
            System.err.println("Impossibile memorizzare il file in cache");
        }
    }
    
    public boolean caricaCache(){//3
        CacheCittaPreferite cp = null;
        try(
            FileInputStream fis = new FileInputStream("./myfiles/cache/cache.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
        ){
            cp = (CacheCittaPreferite)ois.readObject();
        }catch(FileNotFoundException fe){
            System.out.println("file cache non presente");
            return false;
        }catch(IOException | ClassNotFoundException e){
            System.err.println("Errore nella lettura del file cache");
            return false;
        }
        
        ultimaCittaCercata = cp.ultimaCittaCercata;
        cittaPreferite = cp.cittaPreferite;
        numeroUltimaPrevisioneSelezionata = cp.numeroUltimaPrevisioneSelezionata;
        tipoGrafico = cp.tipoGrafico;
        return true;
       
    }
    
}
/*
(1) Crea un nuovo ArrayList<String> per la lista di città preferite e lascia tutti
    gli altri attributi ai loro valori di default
(2) Salva gli argomenti nel file binario cache.bin. Se il file non esiste lo crea.
(3) Copia il contenuto del file binario cache.bin negli attributi dell'oggetto 
    e restituisce true. Se il file non è presente o si verifica un problema di 
    lettura stampa un messaggio di avviso e restituisce false.
*/