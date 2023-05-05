package it.polito.tdp.tesi;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import it.polito.tdp.tesi.model.Analisi;
import it.polito.tdp.tesi.model.Domanda;
import it.polito.tdp.tesi.model.Model;
import it.polito.tdp.tesi.model.Produzione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class FXMLController {

	private Model model;
	
	private ArrayList<Domanda> domanda;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Domanda, Integer> colDemand;

    @FXML
    private TableColumn<Produzione, Integer> colEst;

    @FXML
    private TableColumn<Produzione, Integer> colMese;

    @FXML
    private TableColumn<Domanda, Integer> colMonth;

    @FXML
    private TableColumn<Produzione, Integer> colOrd;

    @FXML
    private TableColumn<Produzione, Integer> colStraord;

    @FXML
    private ComboBox<String> comboProduct;

    @FXML
    private ComboBox<Integer> comboYear;
    
    @FXML
    private Label txtError;

    @FXML
    private TableView<Produzione> tabSchedOttima;

    @FXML
    private TableView<Domanda> tableDemand;

    @FXML
    private TextField txtCapOrd;

    @FXML
    private TextField txtCapStraord;

    @FXML
    private TextField txtCtEst;

    @FXML
    private TextField txtCtOrd;

    @FXML
    private TextField txtCtStock;

    @FXML
    private TextField txtCtStraord;
    
    @FXML
    private Label txtErrorSched;
    
    @FXML
    private Label txtCtGest;
    
    @FXML
    private TextField txtPrezzo;

    @FXML
    private Label txtProfit;
    
    @FXML
    private TextField txtQtaProd;
    
    @FXML
    private CheckBox boxLinea1;

    @FXML
    private CheckBox boxLinea2;

    @FXML
    private CheckBox boxLinea3;
    
    @FXML
    private TextArea txtSpecifiche;
    
    @FXML
    private Label txtErrorSim;
    
    @FXML
    private Label txtTempoImpiegato;
    
    @FXML
    private Label txtCT;
    
    @FXML
    private Label txtTH;
    
    @FXML
    private Label txtWIP;
    
    /*@FXML
    private LineChart<String, Double> chartThWip;
    
    @FXML
    private LineChart<String, Double> chartCtWip;*/


    /*@FXML
    private NumberAxis th;
    
    @FXML
    private CategoryAxis wip;*/

    
    @FXML
    void handleAnno(ActionEvent event) {
    	this.comboProduct.getItems().clear();
    	int anno = this.comboYear.getValue();
    	this.comboProduct.getItems().addAll(this.model.getProdByYear(anno));
    	this.tableDemand.getItems().clear();
    }

    @FXML
    void handleElaboraPiano(ActionEvent event) {
    	try {
    		int capOrd = Integer.parseInt(this.txtCapOrd.getText());
    		int capStraord = Integer.parseInt(this.txtCapStraord.getText());
    		double ctOrd = Double.parseDouble(this.txtCtOrd.getText());
    		double ctStrord = Double.parseDouble(this.txtCtStraord.getText());
    		double ctEst = Double.parseDouble(this.txtCtEst.getText());
    		double ctStock = Double.parseDouble(this.txtCtStock.getText());
    		double prezzo = Double.parseDouble(this.txtPrezzo.getText());
    		ArrayList<Produzione> schedOttima = new ArrayList<Produzione>();
    		try {
	    		int anno = this.comboYear.getValue();
	    	    String idProd = this.comboProduct.getValue();
	    	    if(idProd.equals(null)) {
	    	    	this.txtErrorSched.setText("Selezionare anno e prodotto d'interesse nella sezione precedente!");
	    	    }else {
	    	    	this.txtErrorSched.setText(null);
		    	    this.domanda = this.model.getDomandaMensile(idProd, anno);
		    		schedOttima = this.model.lotSizing(this.domanda, capOrd, capStraord, ctOrd, ctStrord, ctEst, ctStock);
		    		this.tabSchedOttima.getItems().clear();
		    		this.tabSchedOttima.setItems(FXCollections.observableArrayList(schedOttima));
		    		this.txtCtGest.setText(Double.toString(this.model.getCostoSchedOttima()));
		    		this.txtProfit.setText(Double.toString(this.model.calcolaProfitto(prezzo)));
	    	    }
    		}catch(NullPointerException e) {
    			this.txtErrorSched.setText("Selezionare anno e prodotto d'interesse nella sezione precedente!");
    		}
    	}catch(NumberFormatException e) {
    		this.txtErrorSched.setText("Completare i campi con valori numerici!");
    		return;
    	}
    }

    @FXML
    void handleSearch(ActionEvent event) {
    	try {
	    	int anno = this.comboYear.getValue();
	    	String idProd = this.comboProduct.getValue();
	    	if(idProd.equals(null)) {
	    		this.txtError.setText("Inserire tutti i campi!");
				return;
	    	}else {
		    	this.domanda = this.model.getDomandaMensile(idProd, anno);
		    	this.tableDemand.setItems(FXCollections.observableArrayList(this.domanda));
		    	this.txtError.setText(null);
	    	}
    	}catch(NullPointerException e) {
    		this.txtError.setText("Inserire tutti i campi!");
			return;
    	}
    }
    
    @FXML
    void handleCheck1(ActionEvent event) {
    	if(!this.boxLinea1.isSelected()) {
    		this.txtSpecifiche.clear();
    		this.model.resetLinea();
    	}else {
	    	this.txtSpecifiche.clear();
		    this.boxLinea2.setSelected(false);
		    this.boxLinea3.setSelected(false);
		    ArrayList<String> specifiche = new ArrayList<String>();
		    specifiche = this.model.scegliLinea(1);
		    for(String s : specifiche) {
		    	this.txtSpecifiche.appendText(s);
		    }
    	}
    }
    
    @FXML
    void handleCheck2(ActionEvent event) {
    	if(!this.boxLinea2.isSelected()) {
    		this.txtSpecifiche.clear();
    		this.model.resetLinea();
    	}else {
	    	this.txtSpecifiche.clear();
	    	this.boxLinea1.setSelected(false);
	    	this.boxLinea3.setSelected(false);
	    	ArrayList<String> specifiche = new ArrayList<String>();
	    	specifiche = this.model.scegliLinea(2);
	    	for(String s : specifiche) {
	    		this.txtSpecifiche.appendText(s);
	    	}
    	}
    }
    
    @FXML
    void handleCheck3(ActionEvent event) {
    	if(!this.boxLinea3.isSelected()) {
    		this.txtSpecifiche.clear();
    		this.model.resetLinea();
    	}else {
	    	this.txtSpecifiche.clear();
	    	this.boxLinea2.setSelected(false);
	    	this.boxLinea1.setSelected(false);
	    	ArrayList<String> specifiche = new ArrayList<String>();
	    	specifiche = this.model.scegliLinea(3);
	    	for(String s : specifiche) {
	    		this.txtSpecifiche.appendText(s);
	    	}
    	}
    }

    
    @FXML
    void handleSimula(ActionEvent event) {
    	int qtaDaProdurre;
    	try {
    		qtaDaProdurre = Integer.parseInt(this.txtQtaProd.getText());
    		if(qtaDaProdurre>20000) {
    			this.txtErrorSim.setText("Inserire un numero inferiore a 20000!");
    			System.out.println("Inserire un numero inferiore a 20000!");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtErrorSim.setText("Introdurre un numero intero di pezzi da produrre!");
    		System.out.println("Introdurre un numero intero di pezzi da produrre!");
    		return;
    	}
    	try {
    		
    		this.model.simula(qtaDaProdurre);
    		
    		this.txtErrorSim.setText(null);
    		
    		this.txtTempoImpiegato.setText(String.valueOf(this.model.getTempoImpiegato())+" h, "
    				+ "equivalenti a "+String.valueOf(this.model.getTempoImpiegatoG())+" giorni");
    		this.txtCT.setText(String.valueOf(this.model.getCT())+" h");
    		this.txtTH.setText(String.valueOf(this.model.getTH())+" unità/h");
    		this.txtWIP.setText(String.valueOf(this.model.getWIP())+" unità");
    		/*this.txtCT.setText(null);
    		this.chartThWip.getData().clear();
    		this.chartCtWip.getData().clear();
    		
    		this.model.simula();
    		
    		this.txtCT.setText(String.valueOf(this.model.getCT()));
    		this.txtTH.setText(String.valueOf(this.model.getTH()));
    		this.txtWIP.setText(String.valueOf(this.model.getWIP()));
    		
    		XYChart.Series<String, Double> bestCase1 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> bestCase2 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> worstCase1 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> worstCase2 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> practWorstCase1 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> practWorstCase2 = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> TH = new XYChart.Series<String, Double>();
    		XYChart.Series<String, Double> CT = new XYChart.Series<String, Double>();
    		ArrayList<Analisi> best1 = new ArrayList<Analisi>();
    		ArrayList<Analisi> worst1 = new ArrayList<Analisi>();
    		ArrayList<Analisi> practWorst1 = new ArrayList<Analisi>();
    		/*best1 = this.model.getBestCase();
    		worst1 = this.model.getWorstCase();
    		practWorst1 = this.model.getPracticalWorstCase();
    		for(Analisi a : best1) {
    			bestCase1.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getTH()));
    			bestCase2.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getCT()));
    		}
    		bestCase1.setName("best case");
    		bestCase2.setName("best case");
    		for(Analisi a : worst1) {
    			worstCase1.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getTH()));
    			worstCase2.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getCT()));
    		}
    		worstCase1.setName("worst case");
    		worstCase2.setName("worst case");
    		for(Analisi a : practWorst1) {
    			practWorstCase1.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getTH()));
    			practWorstCase2.getData().add(new XYChart.Data<String, Double>(String.valueOf(a.getW()),a.getCT()));
    		}
    		practWorstCase1.setName("practical worst case");
    		practWorstCase2.setName("practical worst case");
    		this.chartThWip.getData().add(bestCase1);
    		this.chartCtWip.getData().add(bestCase2);
    		this.chartThWip.getData().add(worstCase1);
    		this.chartCtWip.getData().add(worstCase2);
    		this.chartThWip.getData().add(practWorstCase1);
    		this.chartCtWip.getData().add(practWorstCase2);
    		TH.getData().add(new XYChart.Data<String, Double>(String.valueOf(this.model.getWipInt()), this.model.getTH()));
    		CT.getData().add(new XYChart.Data<String, Double>(String.valueOf(this.model.getWipInt()), this.model.getCT()));
    		TH.setName("TH linea");
    		CT.setName("CT linea");
    		this.chartThWip.getData().add(TH);
    		this.chartCtWip.getData().add(CT);*/
    	}catch(NullPointerException e) {
    		this.txtErrorSim.setText("Selezionare la linea da testare!");
    		System.out.println("Selezionare la linea da testare!");
    		//e.printStackTrace();
    		return;
    	}
    }

    @FXML
    void initialize() {
        assert colDemand != null : "fx:id=\"colDemand\" was not injected: check your FXML file 'Scene.fxml'.";
        assert colEst != null : "fx:id=\"colEst\" was not injected: check your FXML file 'Scene.fxml'.";
        assert colMese != null : "fx:id=\"colMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert colMonth != null : "fx:id=\"colMonth\" was not injected: check your FXML file 'Scene.fxml'.";
        assert colOrd != null : "fx:id=\"colOrd\" was not injected: check your FXML file 'Scene.fxml'.";
        assert colStraord != null : "fx:id=\"colStraord\" was not injected: check your FXML file 'Scene.fxml'.";
        assert comboProduct != null : "fx:id=\"comboProduct\" was not injected: check your FXML file 'Scene.fxml'.";
        assert comboYear != null : "fx:id=\"comboYear\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabSchedOttima != null : "fx:id=\"tabSchedOttima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tableDemand != null : "fx:id=\"tableDemand\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCapOrd != null : "fx:id=\"txtCapOrd\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCapStraord != null : "fx:id=\"txtCapStraord\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCtEst != null : "fx:id=\"txtCtEst\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCtGest != null : "fx:id=\"txtCtGest\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCtOrd != null : "fx:id=\"txtCtOrd\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCtStock != null : "fx:id=\"txtCtStock\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCtStraord != null : "fx:id=\"txtCtStraord\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtError != null : "fx:id=\"txtError\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtErrorSched != null : "fx:id=\"txtErrorSched\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPrezzo != null : "fx:id=\"txtPrezzo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtProfit != null : "fx:id=\"txtProfit\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQtaProd != null : "fx:id=\"txtQtaProd\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLinea1 != null : "fx:id=\"boxLinea1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLinea2 != null : "fx:id=\"boxLinea2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLinea3 != null : "fx:id=\"boxLinea3\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSpecifiche != null : "fx:id=\"txtSpecifiche\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtErrorSim != null : "fx:id=\"txtErrorSim\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtTempoImpiegato != null : "fx:id=\"txtTempoImpiegato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCT != null : "fx:id=\"txtCT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtTH != null : "fx:id=\"txtTH\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtWIP != null : "fx:id=\"txtWIP\" was not injected: check your FXML file 'Scene.fxml'.";
        //assert chartThWip != null : "fx:id=\"chartThWip\" was not injected: check your FXML file 'Scene.fxml'.";
        //assert chartCtWip != null : "fx:id=\"chartCtWip\" was not injected: check your FXML file 'Scene.fxml'.";
        //assert th != null : "fx:id=\"th\" was not injected: check your FXML file 'Scene.fxml'.";
        //assert wip != null : "fx:id=\"wip\" was not injected: check your FXML file 'Scene.fxml'.";

        this.colDemand.setCellValueFactory(new PropertyValueFactory<Domanda,Integer>("qta"));
        this.colMonth.setCellValueFactory(new PropertyValueFactory<Domanda,Integer>("mese"));
        
        this.colMese.setCellValueFactory(new PropertyValueFactory<Produzione,Integer>("mese"));
        this.colOrd.setCellValueFactory(new PropertyValueFactory<Produzione,Integer>("qtaProdOrd"));
        this.colStraord.setCellValueFactory(new PropertyValueFactory<Produzione,Integer>("qtaProdStraord"));
        this.colEst.setCellValueFactory(new PropertyValueFactory<Produzione,Integer>("qtaProdEst"));
    }

	public void setModel(Model model) {
		this.model = model;
		this.comboYear.getItems().addAll(this.model.getAnni());
		this.domanda = new ArrayList<Domanda>();
	}

}
