package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient;
import com.github.atave.VaadinCmisBrowser.cmis.api.DocumentView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FileView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FolderView;
import com.google.gwt.core.client.Callback;
import com.vaadin.ui.Image;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class FolderComponent extends CustomComponent {
	public final Table table;
	public CmisClient client;
	//	public Callback clickHandler;

	public FolderComponent(String path, CmisClient client) {
		this.client = client;

		//Layout
		Panel panel = new Panel();
		VerticalLayout layout = new VerticalLayout();
		panel.setContent(layout);

		table = new Table();
		table.setPageLength(table.size()); 
		table.setSizeFull();
		table.addStyleName("borderless");
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setImmediate(true);
		//add property
		table.addContainerProperty("image", Image.class, null);
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("description", String.class, null);
		table.addContainerProperty("created on", Timestamp.class, null);
		table.addContainerProperty("modified on", Timestamp.class, null);
		table.addContainerProperty("created by", String.class, null);
		table.addContainerProperty("modified by", String.class, null);
		table.addContainerProperty("action", TableActionComponent.class, null);
		//set cell alignment
		table.setColumnAlignment("image", Align.CENTER);
		table.setColumnAlignment("name", Align.CENTER);
		table.setColumnAlignment("description", Align.CENTER);
		table.setColumnAlignment("created on", Align.CENTER);
		table.setColumnAlignment("modified on", Align.CENTER);
		table.setColumnAlignment("created by", Align.CENTER);
		table.setColumnAlignment("modified by", Align.CENTER);
		table.setColumnAlignment("action", Align.CENTER);
		//reordering column
		table.setColumnReorderingAllowed(true);

		//--RIEMPIO TABELLA ROOT--
		path = client.getCurrentFolder().getPath();
		populateTable(path);
		
		//hide some column
		table.setColumnCollapsingAllowed(true);
		table.setColumnCollapsed("created by", true);
		table.setColumnCollapsed("modified by", true);

		layout.addComponent(table);
		setCompositionRoot(panel);

	}

	// -- NON SERVE PIu!!! --
	//	public Panel getInformationPanel(String name, String lastModifiedBy, GregorianCalendar lastModification, 
	//			final String description, final String author, final GregorianCalendar creationDate, final String fileId, final int itemId) {
	//		final Panel panel = new Panel();
	//		//which rows it refers to 
	//		panel.setData(itemId);
	//		final VerticalLayout layout = new VerticalLayout();
	//		
	////		LayoutClickListener doubleClick = new  LayoutClickListener() {
	////			private static final long serialVersionUID = 5527999180793601282L;
	////			@Override
	////			public void layoutClick(LayoutClickEvent event) {
	////				if (event.isDoubleClick()){
	////					FileView fileView = client.getFile(fileId);
	////					if(fileView.isFolder()){
	////						populateTable(fileView.getPath());
	////					}
	////				} 
	////			}
	////		};
	//
	////		layout.addLayoutClickListener(doubleClick);
	//		panel.setContent(layout);
	//		long millis = lastModification.getTimeInMillis();
	//		Timestamp date = new Timestamp(millis);
	//		layout.addComponent(new Label("name: " + name));
	//		layout.addComponent(new Label("modified by: " + lastModifiedBy));
	//		layout.addComponent(new Label("on: " + date));
	//		// image for arrow down
	//		final Image imageDown = new Image(null, new ThemeResource("img/arrow-down.png"));
	//		imageDown.setHeight("24px");
	//		imageDown.setWidth("24px");
	//		layout.addComponent(imageDown);
	//		layout.setComponentAlignment(imageDown, Alignment.BOTTOM_RIGHT);
	//		//image for arrow up
	//		final Image imageUp = new Image(null, new ThemeResource("img/arrow-up.png"));
	//		imageUp.setVisible(false);
	//		imageUp.setHeight("24px");
	//		imageUp.setWidth("24px");
	//		layout.addComponent(imageUp);
	//		layout.setComponentAlignment(imageUp, Alignment.BOTTOM_RIGHT);
	//		//listener for view more details
	//		ClickListener arrowListener = new ClickListener() {
	//			Boolean flag = true;
	//			long millis = creationDate.getTimeInMillis();
	//			Timestamp date = new Timestamp(millis);
	//			Label labelDescr = new Label("description: " + description);
	//			Label labelCreated = new Label("created by: " + author);
	//			Label labelDate = new Label("on: " + date);
	//			@Override
	//			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
	//				if (flag) {
	//					layout.addComponent(labelDescr);
	//					layout.addComponent(labelCreated);
	//					layout.addComponent(labelDate);
	//					imageDown.setVisible(false);
	//					imageUp.setVisible(true);
	//					flag = false;
	//				} else {
	//					layout.removeComponent(labelDescr);
	//					layout.removeComponent(labelCreated);
	//					layout.removeComponent(labelDate);
	//					imageDown.setVisible(true);
	//					imageUp.setVisible(false);
	//					flag = true;
	//				}
	//
	//			}
	//		};
	//		panel.addClickListener(panelListener);
	//		imageDown.addClickListener(arrowListener);
	//		imageUp.addClickListener(arrowListener);
	//		return panel;
	//	}

	// listener for select row
	ClickListener panelListener = new ClickListener() {
		//select table row
		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
			Integer id = (Integer)((Panel) event.getComponent()).getData();
			table.select(id);
		}
	};
	//select right icon
	public Image getFolderIcon(Boolean isFolder, final String fileId, Integer itemId) {
		Image icon = null;
		if (isFolder) {
			icon = new Image("folder", new ThemeResource("img/open_folder.png"));
			icon.setData(itemId);
			icon.setWidth("34px");
		} else {
			icon = new Image("document", new ThemeResource("img/profile-pic.png"));
			icon.setData(itemId);
			icon.setWidth("34px");
		}
		//open folder
		icon.addClickListener(new ClickListener() {
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				// select row
				Integer id = (Integer)((Image) event.getComponent()).getData();
				table.select(id);
				//open folder
				if(event.isDoubleClick() ){
					FileView fileView = client.getFile(fileId);
					if(fileView.isFolder()){
						populateTable(fileView.getPath());
					}
				}
			}
		});
		return icon;
	}


	public void populateTable(String path){
		table.removeAllItems();
		if(path == null){
			//display empty table
		} else {
			//populate table only if path is a folder
			if ((client.getFile(path)).isFolder()){
				client.navigateTo(path);
				FolderView currentFolder = client.getCurrentFolder();
				Collection<FileView> files = currentFolder.getChildren();
				int i = 0;
				for(FileView file : files){
					long creationDateMillis = file.getCreationDate().getTimeInMillis();
					Timestamp creationDate = new Timestamp(creationDateMillis);
					long modificationDateMillis = file.getLastModificationDate().getTimeInMillis();
					Timestamp modificationDate = new Timestamp(modificationDateMillis);
					if(file.isDocument()){
						table.addItem(
								new Object[] {
										getFolderIcon(false, file.getId(), i),
										file.getName(),
										file.getDescription(),
										creationDate,
										modificationDate, 
										file.getCreatedBy(),
										file.getLastModifiedBy(),
										new TableActionComponent(file.getPath(), i, table, client, false)},
										i);
					}else{
						table.addItem(
								new Object[] {
										getFolderIcon(true, file.getId(), i),
										file.getName(),
										file.getDescription(),
										creationDate,
										modificationDate,
										file.getCreatedBy(),
										file.getLastModifiedBy(),
										new TableActionComponent(file.getPath(), i, table, client, true)},
										i);
					}	
					++i;
				}
			}
		}
	}

	public void populateTableFromComboBox(String path){
		table.removeAllItems();
		if(path == null){
			//display empty table
		} else {
			//populate table only if path is a folder
			if ((client.getFile(path)).isFolder()){
				System.out.println(client.getCurrentFolder().toString());
				FolderView currentFolder = client.getCurrentFolder();
				Collection<FileView> files = currentFolder.getChildren();
				int i = 0;
				for(FileView file : files){
					long creationDateMillis = file.getCreationDate().getTimeInMillis();
					Timestamp creationDate = new Timestamp(creationDateMillis);
					long modificationDateMillis = file.getLastModificationDate().getTimeInMillis();
					Timestamp modificationDate = new Timestamp(modificationDateMillis);
					if(file.isDocument()){
						table.addItem(
								new Object[] {
										getFolderIcon(false, file.getId(), i),
										file.getName(),
										file.getDescription(),
										creationDate,
										modificationDate, 
										file.getCreatedBy(),
										file.getLastModifiedBy(),
										new TableActionComponent(file.getPath(), i, table, client, false)},
										i);
					}else{
						table.addItem(
								new Object[] {
										getFolderIcon(true, file.getId(), i),
										file.getName(),
										file.getDescription(),
										creationDate,
										modificationDate,
										file.getCreatedBy(),
										file.getLastModifiedBy(),
										new TableActionComponent(file.getPath(), i, table, client, true)},
										i);
					}	
					++i;
				}
			}
		}
	}

	public void addItemToFolderComponent(String fileId){
		FileView fileView = client.getFile(fileId);
		long creationDateMillis = fileView.getCreationDate().getTimeInMillis();
		Timestamp creationDate = new Timestamp(creationDateMillis);
		long modificationDateMillis = fileView.getLastModificationDate().getTimeInMillis();
		Timestamp modificationDate = new Timestamp(modificationDateMillis);
		int i = table.size() +1;
		if (fileView.isFolder()){
			FolderView file = fileView.asFolder();
			table.addItem(
					new Object[] {
							getFolderIcon(true, file.getId(), i),
							file.getName(),
							file.getDescription(),
							creationDate,
							modificationDate,
							file.getCreatedBy(),
							file.getLastModifiedBy(),
							new TableActionComponent(file.getPath(), i, table, client, true)},
							i);
		} else {
			DocumentView file = fileView.asDocument();
			table.addItem(
					new Object[] {
							getFolderIcon(false, file.getId(), i),
							file.getName(),
							file.getDescription(),
							creationDate,
							modificationDate,
							file.getCreatedBy(),
							file.getLastModifiedBy(), 
							new TableActionComponent(file.getPath(), i, table, client, false)},
							i);

		}
	}

}
