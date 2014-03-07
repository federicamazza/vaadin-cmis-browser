package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient;
import com.github.atave.VaadinCmisBrowser.cmis.api.DocumentView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FileView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FolderView;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.ui.Image;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class DeleteDocumentComponent extends CustomComponent {
	Button yes, no;
	Table table;
	CmisClient client;
	Boolean isFolder;
	Integer itemId;
	String path;
	Window window;
	Panel buttonPanel;

	public DeleteDocumentComponent(final String path, final Integer itemId , final Table table, final CmisClient client, final Boolean isFolder) {
		this.table = table;
		this.client = client;
		this.isFolder = isFolder;
		this.itemId = itemId;
		this.path = path;

		// A layout structure used for composition
		Panel panel = new Panel();
		//which rows it refers to 
		panel.setData(itemId);
		VerticalLayout layout = new VerticalLayout();
		panel.setContent(layout);

		//button for download document
		Image documentDownload = new Image(null, new ThemeResource(
				"img/document-download.png"));
		documentDownload.setHeight("34px");
		documentDownload.setWidth("34px");
		buttonPanel = new Panel();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		yes = new Button("yes");
		no = new Button("no");
		yes.addClickListener(buttonListener);
		no.addClickListener(buttonListener);
		horizontalLayout.addComponent(yes);
		horizontalLayout.setComponentAlignment(yes, Alignment.MIDDLE_LEFT);
		horizontalLayout.addComponent(no);
		horizontalLayout.setComponentAlignment(no, Alignment.MIDDLE_RIGHT);
		buttonPanel.setContent(horizontalLayout);

		ClickListener documentDownloadListener = new ClickListener() {
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				//create and add window
				window = new Window();
				window.center();
				final VerticalLayout verticalLayout = new VerticalLayout();      
				UI.getCurrent().addWindow(window);
				Label question = new Label("Delete: Are you sure?");

				verticalLayout.addComponent(question);
//				verticalLayout.setComponentAlignment(question, Alignment.MIDDLE_CENTER);
				verticalLayout.addComponent(buttonPanel);
//				verticalLayout.setComponentAlignment(buttonPanel, Alignment.BOTTOM_CENTER);

				window.setHeight("150px");
				window.setWidth("500px");
				window.setContent(verticalLayout);


			}
		};
		documentDownload.addClickListener(documentDownloadListener);

		panel.addClickListener(panelListener);
		layout.addComponent(documentDownload);
		layout.setComponentAlignment(documentDownload, Alignment.MIDDLE_CENTER);
		setCompositionRoot(panel);

	}

	ClickListener panelListener = new ClickListener() {
		//select table row
		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
			Integer id = (Integer)((Panel) event.getComponent()).getData();
			table.select(id);
		}
	};

	Button.ClickListener buttonListener = new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
			if(event.getButton().equals(yes)){
				// Delete all versions of a document			
				if (isFolder){
					FolderView folder = client.getFolder(path);
					client.deleteFolder(folder);			
				} else {
					DocumentView document = client.getDocument(path);
					client.deleteDocument(document.asDocument());
				}
				table.removeItem(itemId);
			}	
		
				window.close();
		}
	};


}