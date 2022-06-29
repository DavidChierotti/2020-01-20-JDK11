package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;
import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi");
    	if(boxRuolo.getValue()==null) {
    		txtResult.appendText("\nSelezionare ruolo");
    	}
    	else if(this.model.getGrafo().vertexSet().size()==0) {
    		txtResult.appendText("\nCreare grafico");
    	}
    	else {
    		for(Adiacenza a:this.model.stampa()) {
    			txtResult.appendText("\nSorgente: "+this.model.getArtisti().get(a.getA1()).getNome()+" Destinazione: "+this.model.getArtisti().get(a.getA2()).getNome()+" peso:"+a.getPeso());
    		}
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    	int x;
    	try {
        	x=Integer.parseInt(txtArtista.getText());
        	
        }
        catch(NumberFormatException e) {
        	txtResult.appendText("Inserire valore numerico");
        	return;
        }
    	if(this.model.isPresente(x)==false)
    		txtResult.appendText("\n Artista non presente");
    	else {
    		List<Artist> ris=this.model.cercaCammino(x);
    		for(Artist a:ris) {
    			txtResult.appendText("\n"+a.getId()+"  "+a.getNome());
    		}
    		txtResult.appendText("\n"+this.model.calcolaPeso(ris));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	if(boxRuolo.getValue()==null) {
    		txtResult.appendText("\nSelezionare ruolo");
    	}
    	else {
    		this.model.creaGrafo(boxRuolo.getValue());
    	}
    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	ArtsmiaDAO dao=new ArtsmiaDAO();
    	boxRuolo.getItems().addAll(dao.ruoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
