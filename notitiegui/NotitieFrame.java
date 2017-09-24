package notitiegui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import notitie.Notitie;
import notitiedata.NotitieData;
import notitiedata.NotitieException;
import notitiedata.NotitieMapper;

public class NotitieFrame extends JFrame {
  private Notitie notitie = null;
  private ArrayList<Notitie> note = null;
  
  private JLabel titelLabel = null;
  private JTextField titelVeld = null;
  private JTextArea tekstVeld = null;
  private JComboBox keuzeBox = null;
  private JButton opslaan = null;
  private JButton verwijderen = null;

  private NotitieMapper nm = null;
  private NotitieData nd = null;
  
  /**
   * Constructor
   * @throws NotitieException
   */
  public NotitieFrame () throws NotitieException {
    super();
    try{
    initialize();  
    mijnInit();
    }
    catch(NotitieException e){}
  }
  
  
  /**
   * Zorgt voor initialisatie van de combobox
   */
  public void mijnInit() throws NotitieException{
    // Initialiseert notities vanuit de sql-database server
    nm = new NotitieMapper();
    nd = new NotitieData();
    note = nm.leesAlleNotities();
    initialiseerKeuzeBox();
  }
  
  /**
   * Zorgt voor initialisatie van het venster en paneel.
   * @throws NotitieException
   */
  public void initialize() {
    setTitle("Notitie");
    setSize(800, 800);
    Container pane = getContentPane();
    pane.setLayout(new BorderLayout());

    // Creeert het menu
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    JMenu bestand = new JMenu("Bestand");
    menuBar.add(bestand);
    JMenu aanpassen = new JMenu("Aanpassen");
    menuBar.add(aanpassen);
    JMenuItem nieuw = new JMenuItem("Nieuw");
    nieuw.addActionListener(new nieuwAction());
    bestand.add(nieuw);
    JMenuItem opslaanAls = new JMenuItem("Opslaan als...");
    opslaanAls.addActionListener(new opslaanAlsAction());
    bestand.add(opslaanAls);
    JMenuItem letterType = new JMenuItem("Lettertype");
    letterType.addActionListener(new letterTypeAction());
    aanpassen.add(letterType);
   
    // Creeert het bovenste gedeelte van het venster, Noord    
    JPanel paneel = new JPanel();
    paneel.setLayout(new BorderLayout());
    pane.add(paneel, BorderLayout.CENTER);
    JPanel paneel1 = new JPanel(new FlowLayout()); 
    paneel.add(paneel1, BorderLayout.NORTH);
    keuzeBox = new JComboBox<Notitie>();
    keuzeBox.addActionListener(new keuzeBoxAction());
    paneel1.add(keuzeBox);
    titelLabel = new JLabel("Titel: ");
    paneel1.add(titelLabel);
    titelVeld = new JTextField("                                                        ");
    paneel1.add(titelVeld);
   
    // Creeert het centrum van het venster
    tekstVeld = new JTextArea();
    paneel.add(tekstVeld, BorderLayout.CENTER);
    tekstVeld.setBounds(10, 10, 400, 400);
   
    // Creeert het zuiden van het venster    
    JPanel knop = new JPanel();
    knop.setLayout(new FlowLayout());
    opslaan = new JButton("opslaan");
    opslaan.addActionListener(new opslaanAction());
    knop.add(opslaan);
    verwijderen = new JButton("verwijderen");
    verwijderen.addActionListener(new verwijderAction());
    knop.add(verwijderen);
    pane.add(knop, BorderLayout.SOUTH);   
  }
  
  
  
  /**
   * Als op de knop 'nieuw' in het bestandsmenu wordt gedrukt dan wordt er een 
   * nieuwe notitie gemaakt, waarbij het titelveld en het tekstveld leeg zijn.
   */
  public class nieuwAction implements ActionListener{    
    public void actionPerformed(ActionEvent e) {
      Notitie nieuw = new Notitie("                               ", "      ");
      note.add(nieuw);
      titelVeld.setText(nieuw.getTitel());
      tekstVeld.setText(nieuw.getTekst());
      keuzeBox.addItem(nieuw.getTitel());
      initialiseerKeuzeBox();
//      keuzeBox.setSelectedItem(nieuw.getTitel());
    }
  }
  
  
  
  /**
   * Als op de knop 'opslaan' wordt gedrukt dan wordt de notitie opgeslagen in de sql-database.
   */
  public class opslaanAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Notitie nieuw = new Notitie(titelVeld.getText(), tekstVeld.getText());
      try{
          nm.schrijfNotitie(nieuw);
          keuzeBox.addItem(nieuw.getTitel());
        }
      catch(NotitieException ne){}
    }
  }
  
  /**
   * Als op de knop 'verwijderen' wordt geklikt dan wordt de notitie uit de sql-database verwijdert.
   */
  public class verwijderAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Notitie notitie = new Notitie(titelVeld.getText(), tekstVeld.getText());
      try{
        nm.verwijderNotitie(notitie);
        keuzeBox.setSelectedItem(0);
      }
      catch(NotitieException ne){}
    }
  }

  /**
   * Als de titel in de keuzeBox verandert, dan verschijnt de bijbehorende tekst in het tekstveld.
   */
  public class keuzeBoxAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String titel = keuzeBox.getSelectedItem().toString();
      for(int i = 0; i < note.size(); i++){
        if(titel.equals(note.get(i).getTitel())){
        titelVeld.setText(note.get(i).getTitel());
        tekstVeld.setText(note.get(i).getTekst());
        }
      }
  }
  }
  
  /**
   * Als op de opslaan als... knop uit het bestandsmenu wordt geklikt
   * dan wordt de optie gegeven om de notitie in een bestand op te slaan.
   */
  public class opslaanAlsAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Notitie nieuw = new Notitie(titelVeld.getText(), tekstVeld.getText());
      try{
          nd.schrijfNotitie(nieuw);
          keuzeBox.addItem(nieuw.getTitel());
        }
      catch(NotitieException ne){}
    }
  }
  
  /**
   * Als op de knop 'lettertype' in het bestandsmenu wordt geklikt dan kunnen 
   * er aanpassingen gedaan worden aan het lettertype.
   */
  public class letterTypeAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      
    }
  }

  private void initialiseerKeuzeBox(){
    if(note != null){
      for(int i = 0; i < note.size(); i++){
        keuzeBox.addItem(note.get(i).getTitel());
      }
      keuzeBox.setSelectedItem(0);
    }
    // vult het tekstVeld met de tekst van de notitie met de gegeven titel
    for(Notitie n: note){
      if(keuzeBox.getSelectedItem().toString().equals(n.getTitel())){
        tekstVeld.setText(n.getTekst());
      }
    }
    titelVeld.setText(keuzeBox.getSelectedItem().toString());
  }
  
  public static void main(String[] args) throws NotitieException {
    NotitieFrame notitieframe = new NotitieFrame();
    notitieframe.setVisible(true);
    notitieframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
}
