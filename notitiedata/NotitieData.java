package notitiedata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import notitie.Notitie;

/**
 * De klasse NotitieData is verantwoordelijk voor het lezen en schrijven 
 * van notities naar het bestand notitie.txt.
 * @author Simon
 *
 */
public class NotitieData {
  private File file = new File("notitie.txt");
  private File files = new File("notities.txt");
  private ObjectOutputStream schrijver = null;

  
  /**
   * Schrijft alle notities weg naar bestand notities.txt
   * @param notities
   * @throws NotitieException
   */
  public void schrijfAlleNotities(ArrayList<Notitie> notities) throws NotitieException {
    try {
      schrijver = new ObjectOutputStream(new FileOutputStream(files));
      schrijver.writeObject(notities);
    }
    catch (IOException e) {
      throw new NotitieException ("Fout bij schrijven");
    }
    finally {
      if (schrijver != null) {
        try {schrijver.close();}
        catch (IOException ioe){}
      }
    }
  }
  
  /**
   * Schrijft notitie weg naar bestand notitie.txt.
   * @param notitie
   * @throws NotitieException
   */
  public void schrijfNotitie (Notitie notitie) throws NotitieException {
    try {
      schrijver = new ObjectOutputStream(new FileOutputStream(file));
      schrijver.writeObject(notitie);
    }
    catch (IOException e) {
      throw new NotitieException ("Fout bij schrijven");
    }
    finally {
      if (schrijver != null) {
        try {schrijver.close();}
        catch (IOException ioe){}
      }
    }
  }
}
