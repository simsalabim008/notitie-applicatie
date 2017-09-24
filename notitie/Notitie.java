package notitie;

import java.util.ArrayList;

/**
 * Notitie app waarin gemakkelijk notities bewaart en bewerkt kunnen worden.
 * Op dit moment in de vorm van tekst.
 * @author Simon
 *
 */
public class Notitie {
  private String titel = null;
  private String tekst = null;

  /**
   * Constructor
   * @param titel
   * @param String tekst
   */
  public Notitie(String titel, String tekst){
    this.titel = titel;
    this.tekst = tekst;    
  }
  
  /**
   * Geeft de titel van de notitie
   * @return String titel
   */
  public String getTitel() {
    return titel;
  }
  
  /**
   * Geeft de tekst van de notitie
   * @return String tekst
   */
  public String getTekst() {
    return tekst;
  }  
}
