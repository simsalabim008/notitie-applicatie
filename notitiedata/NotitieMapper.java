package notitiedata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import notitie.Notitie;

/**
 * De klasse NotitieMapper is verantwoordelijk voor het bijhouden
 * van de notities in een achterliggende database.
 * @author Simon
 *
 */
public class NotitieMapper {
  private Connection con = null;
  private PreparedStatement prepLeesAlles = null;
  private PreparedStatement prepVoegtoe = null;
  private PreparedStatement prepVerwijder = null;
  private String url = DBConst.URL;
  
  /**
   * Maakt verbinding met de database genoemd in url en 
   * initialiseert PreparedStatement-objecten.
   * @param url de url
   * @throws CDException als er iets fout gaat.
   */
  public NotitieMapper() throws NotitieException {
    maakVerbindingDB(url);
    initialiseerPrepStatements();
  }
  
  public ArrayList<Notitie> leesAlleNotities() throws NotitieException {
    ArrayList<Notitie> notitieLijst = new ArrayList<Notitie>();
    try {
      ResultSet res = prepLeesAlles.executeQuery();
      while (res.next()) {
        String titel = res.getString(1);
        String tekst = res.getString(2);
        Notitie notitie = new Notitie(titel, tekst);
        notitieLijst.add(notitie);
      }
    }
    catch (SQLException e) {
      throw new NotitieException("Fout bij het lezen");
    }
    return notitieLijst;
  }
  
  /**
   * Schrijft een notitie naar de database.
   * @param notitie
   * @throws NotitieException
   */
  public void schrijfNotitie(Notitie notitie) throws NotitieException {
      String titel = notitie.getTitel();
      String tekst = notitie.getTekst();
      try {
      prepVoegtoe.setString(1, titel);
      prepVoegtoe.setString(2, tekst);
      prepVoegtoe.executeUpdate();
    }
    catch (SQLException e) {
      throw new NotitieException("Fout bij het wegschrijven van de notitie");
    }
  }

  public void verwijderNotitie(Notitie notitie) throws NotitieException {
    try{
      if(notitie != null){
      String titel = notitie.getTitel();
      prepVerwijder.setString(1, titel);
      prepVerwijder.executeUpdate();
      }
    }
    catch (SQLException e) {
      throw new NotitieException("Fout bij het verwijderen van de notitie");
    }
  }
  /**
   * Sluit de verbinding met de database.
   */
  public void sluitAf() {
    if (con != null) {
      try {
        con.close();
      }
      catch (SQLException ignore) {
        }
    }
  }
  
  private void maakVerbindingDB(String url) throws NotitieException {
    try {
      Class.forName(DBConst.DRIVERNAAM);
      con = DriverManager.getConnection(DBConst.URL, DBConst.GEBRUIKERSNAAM, DBConst.WACHTWOORD);
    }
    catch (ClassNotFoundException e) {
      throw new NotitieException("Driver niet geladen");
    }
    catch (SQLException e) {
      throw new NotitieException("Fout bij maken van de verbinding");
    }
  }
  
  private void initialiseerPrepStatements() throws NotitieException {
    try {
      String sql = "SELECT * FROM Notitie";
      prepLeesAlles = con.prepareStatement(sql);
      sql = "INSERT INTO Notitie (titel, tekst) values (?,?)";
      prepVoegtoe = con.prepareStatement(sql);
      sql = "DELETE FROM Notitie where titel = (?)";
      prepVerwijder = con.prepareStatement(sql);
    }
    catch (SQLException e) {
      sluitAf();
      throw new NotitieException("Fout bij het formuleren van een SQL-opdracht.");
    }
  }
}
