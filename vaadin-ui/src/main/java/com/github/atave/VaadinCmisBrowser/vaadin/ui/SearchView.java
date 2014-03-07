package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;

import com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient;
import com.github.atave.VaadinCmisBrowser.cmis.api.FileView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FolderView;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SearchView extends VerticalLayout implements View  {
	
	private VerticalLayout editorLayout = new VerticalLayout();
	private FieldGroup editorFields = new FieldGroup();
	private VerticalLayout advancedLayout = new VerticalLayout();
	private HorizontalSplitPanel top;
	private Label name;
	private Label author;
	private Label type;
	private ComboBox nameCb;
	private ComboBox authorCb;
	private ComboBox typeCb;
	private Label word;
	private Label date;
	private TextField wordTf;
	private TextField dateTf;
	private String selectedValue;
	
	CmisClient client;
	FolderComponent folder;
	
	public SearchView() {
		setSizeFull();
		addStyleName("dashboard-view");
		client = ((AppUI)UI.getCurrent()).getClient();
//		System.out.println(folder.getDescription());
		client.navigateTo("/");
		
		
		folder = new FolderComponent(null,client);
		folder.populateTable(null);
		
		top = new HorizontalSplitPanel();
		top.setSizeFull();
		top.setSplitPosition(33);
		top.setLocked(true);
		top.addStyleName("toolbar");
		top.setFirstComponent(editorLayout);
    	top.setSecondComponent(folder);
		
		addComponent(top);

		editorLayout.setMargin(true);
		editorLayout.setSpacing(true);
		editorLayout.addStyleName("toolbar");
		
		name = new Label("Nome");
		editorLayout.addComponent(name);
		
		nameCb = new ComboBox();
		editorLayout.addComponent(nameCb);
		nameCb.setWidth("100%");
		String path = client.getCurrentFolder().getPath();
		client.navigateTo(path);
		fillName(nameCb,path);
		
		
		nameCb.setImmediate(true);
		
		author = new Label("Autore");
		editorLayout.addComponent(author);
		
		authorCb = new ComboBox();
		editorLayout.addComponent(authorCb);
		authorCb.setWidth("100%");
		
		nameCb.addListener(new Listener() {
			@Override
			public void componentEvent(Event event) {
				selectedValue = nameCb.getValue().toString();	
//				System.out.println(selectedValue);
				client.navigateTo(selectedValue);	// ATTENZIONE!! SE SI SELEZIONA UN DOCUMENTO NON VA!
//				System.out.println(client.getCurrentFolder().getPath());
				fillAuthor(authorCb, client.getCurrentFolder().getPath());
			}
	    });

		
		authorCb.addListener(new Listener(){

			public void  componentEvent(Event event) {
				
			}

	      
	    });
		
		authorCb.setImmediate(true);
		type = new Label("Tipo");
		editorLayout.addComponent(type);
			
		typeCb = new ComboBox();
		ArrayList<String> type = new ArrayList();
		type.add("alessia");
		type.add("federica");
		for (String ext : type) {
			typeCb.addItem(ext);
		}
		typeCb.setWidth("100%");
		editorLayout.addComponent(typeCb);
		
		ClickListener clickListener = new ClickListener(){
			Boolean flag = true;
			public void buttonClick(ClickEvent event) {
				if(flag){
					word.setVisible(true);
					wordTf.setVisible(true);
					date.setVisible(true);
					dateTf.setVisible(true);
					flag = false;
				}
				else{
					word.setVisible(false);
					wordTf.setVisible(false);
					date.setVisible(false);
					dateTf.setVisible(false);
					flag = true;
				}
					
			}
		};

		
		Button advancedButton = new Button("Opzioni Avanzate");

		editorLayout.addComponent(advancedButton);
		advancedButton.addClickListener(clickListener);;
		
		editorLayout.addComponent(advancedLayout);
		
		word = new Label("Cerca per parola");
		advancedLayout.addComponent(word);
		word.setVisible(false);
		
		wordTf = new TextField();
		advancedLayout.addComponent(wordTf);
		wordTf.setWidth("100%");
		wordTf.setVisible(false);
		
		date = new Label("Data ultima modifica");
		advancedLayout.addComponent(date);
		date.setVisible(false);
		
		dateTf = new TextField();
		advancedLayout.addComponent(dateTf);
		dateTf.setWidth("100%");
		dateTf.setVisible(false);
		
		advancedLayout.setSizeFull();
		
		Button searchButton = new Button("Search");
		editorLayout.addComponent(searchButton);
		editorLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_RIGHT);
		searchButton.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	System.out.println("SEARCHVIEW:    " + client.getCurrentFolder().getPath());
            	folder.populateTableFromComboBox(client.getCurrentFolder().getPath());

            }
              
        });

		
	}
	
	public void fillName(ComboBox c, String path){			
		Collection<FileView> files = client.getCurrentFolder().getChildren();
		for(FileView file : files){
			if(file.isDocument()){
					nameCb.addItem(file.getName());
			}
			else if(file.isFolder()){
				nameCb.addItem(file.getName());
				client.navigateTo(file.getName());
				fillName(nameCb, file.getName());
				client.navigateTo("..");
			}
		}
	}
	
	public void fillAuthor(ComboBox c, String path){	
		Collection<FileView> files = client.getCurrentFolder().getChildren();
		for(FileView file : files){
			if(file.isDocument()){
					authorCb.addItem(file.getCreatedBy());
			}
			else if(file.isFolder()){
				authorCb.addItem(file.getCreatedBy());
				client.navigateTo(file.getName());
				fillAuthor(authorCb, file.getName());
				client.navigateTo("..");
			}
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
