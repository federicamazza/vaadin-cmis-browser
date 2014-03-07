package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

import com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient;
import com.github.atave.VaadinCmisBrowser.cmis.api.DocumentView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FileView;
import com.github.atave.VaadinCmisBrowser.cmis.api.FolderView;
import com.github.atave.VaadinCmisBrowser.cmis.impl.AlfrescoClient;
import com.github.atave.VaadinCmisBrowser.vaadin.ui.UploadView.LineBreakCounter;
import com.github.atave.VaadinCmisBrowser.vaadin.utils.DocumentUploader;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public class HomeView  extends VerticalLayout implements View  {
	CmisClient client;
//	AlfrescoClient client;
	FolderComponent table;
	Tree tree;

	public HomeView() {
		setSizeFull();
		addStyleName("dashboard-view");
		client = ((AppUI)UI.getCurrent()).getClient();
		client.navigateTo("/");

		//pannello intestazione e ricerca
		HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setSpacing(true);
		top.addStyleName("toolbar");

		addComponent(top);

		//pannello albero e tabella
		final Label title = new Label("My Alfresco");
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		top.setExpandRatio(title, 1);

		//middle: menù ricerca
		HorizontalLayout middle = new HorizontalLayout();
		middle.setWidth("100%");
		middle.setSpacing(true);
		middle.addStyleName("toolbar");
		addComponent(middle);

		//--- MENU DIRECTORY ---
		MenuBar menuBar = new MenuBar();
		middle.addComponent(menuBar);
		middle.setComponentAlignment(menuBar, Alignment.MIDDLE_LEFT);
		menuBar.setStyleName("h1");
		//		menuBar.setWidth("60%");
		middle.setExpandRatio(menuBar, 2);
		MenuBar.MenuItem dir1 = menuBar.addItem("directory1", null, null);
		MenuBar.MenuItem dir2 = menuBar.addItem("directory2", null, null);
		MenuBar.MenuItem dir3 = menuBar.addItem("directory3", null, null);

		MenuBar.Command command = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO Auto-generated method stub

			}
		};
		
		// ADD FOLDER
		Image addFolder = new Image(null, new ThemeResource(
				"img/arrow-up.png"));
		addFolder.setHeight("34px");
		addFolder.setWidth("34px");
		addFolder.addClickListener(addFolderListener);
		middle.addComponent(addFolder);
		middle.setComponentAlignment(addFolder, Alignment.MIDDLE_LEFT);

		//-- UPLOAD --
		Image upload = new Image(null, new ThemeResource(
				"img/document-download.png"));
		upload.setHeight("34px");
		upload.setWidth("34px");
		upload.addClickListener(uploadListener);
		middle.addComponent(upload);
		middle.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);

		// -- SEARCH COMBOBOX --
		final ComboBox documentSelectHome = new ComboBox();
		ArrayList<String> documents = new ArrayList();
		documents.add("alessia");
		documents.add("federica");
		for (String document : documents) {
			documentSelectHome.addItem(document);
		}
		documentSelectHome.setWidth("300px");
		middle.addComponent(documentSelectHome);
		middle.setComponentAlignment(documentSelectHome, Alignment.MIDDLE_RIGHT);

		// -- SEARCH BUTTON --
		Button add = new Button("Search");
		add.addStyleName("default");
		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//                addSelectedMovie(movieSelect);
			}
		});
		middle.addComponent(add);

		//--- TREE ---
		tree = new Tree();
		tree.addItem("/");
		createTree(tree, "/");
		tree.setSizeFull();		
		tree.addListener(treeListener);

		// --- TABLE ---
		table = new FolderComponent(null, client);
		table.setImmediate(true);
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setStyleName("toolbar");
		splitPanel.setSizeFull(); //mette barra in split panel
		splitPanel.setSplitPosition(15, Sizeable.UNITS_PERCENTAGE);
		splitPanel.setFirstComponent(tree);
		splitPanel.setSecondComponent(table);
		addComponent(splitPanel);
		setExpandRatio(splitPanel, 3);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public void createTree(Tree tree, String path){	
		Collection<FileView> files1 = client.getCurrentFolder().getChildren();
		for(FileView file1 : files1){
			if(file1.isDocument()){
				tree.addItem(file1.getName());
				tree.setParent(file1.getName(), path);
			}
			else if(file1.isFolder()){
				tree.addItem(file1.getName());
				tree.setParent(file1.getName(), path);
				client.navigateTo(file1.getName());
				createTree(tree, file1.getName());
				client.navigateTo("..");
			}
		}
	}

	MouseEvents.ClickListener uploadListener = new MouseEvents.ClickListener() {
		@SuppressWarnings("deprecation")
		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
	        //create and add window
			final Window window = new Window();
	        window.center();
	        final VerticalLayout verticalLayout = new VerticalLayout();      
	        UI.getCurrent().addWindow(window);
	        //create component
	        final Label state = new Label();
	        final Label result = new Label();
	        final Label fileName = new Label();
	        final Label textualProgress = new Label();
	        final ProgressIndicator pi = new ProgressIndicator();
	        final Button close = new Button("Finish");
//	        LineBreakCounter counter = new LineBreakCounter();
	        
	        // find parameters
	        FolderView folderView = client.getCurrentFolder();
	        final String path = folderView.getPath();
			DocumentUploader receiver = new DocumentUploader(client, path) {

				@Override
				protected void onCmisUploadReceived(DocumentView documentView) {
					// TODO Auto-generated method stub
					
				}
			};
	        //upload component        
	        final Upload upload = new Upload("Select file to upload", receiver);
	        upload.setImmediate(true);
	        upload.setButtonCaption("Upload File");
			upload.addSucceededListener(receiver);
			verticalLayout.addComponent(upload);
			verticalLayout.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
			verticalLayout.addComponent(new Label());	
	        
	        //details
	        Panel p = new Panel("Status");
	        p.setSizeUndefined();
	        FormLayout l = new FormLayout();
	        l.setMargin(true);
	        p.setContent(l);
	        HorizontalLayout stateLayout = new HorizontalLayout();
	        stateLayout.setSpacing(true);
	        stateLayout.addComponent(state);
	        stateLayout.setCaption("Current state");
	        state.setValue("Idle");
	        l.addComponent(stateLayout);
	        fileName.setCaption("File name");
	        l.addComponent(fileName);
	        pi.setCaption("Progress");
	        pi.setVisible(false);
	        l.addComponent(pi);
	        textualProgress.setVisible(false);
	        l.addComponent(textualProgress);
	        close.setVisible(false);
	        l.addComponent(close);
	        verticalLayout.addComponent(p);
	        verticalLayout.setComponentAlignment(p, Alignment.BOTTOM_CENTER);

	        upload.addListener(new Upload.StartedListener() {
	            public void uploadStarted(final StartedEvent event) {
	                // this method gets called immediatedly after upload is
	                // started
	                pi.setValue(0f);
	                pi.setVisible(true);
	                pi.setPollingInterval(500); // hit server frequantly to get
	                textualProgress.setVisible(true);
	                // updates to client
	                state.setValue("Uploading");
	                fileName.setValue(event.getFilename());
	            }
	        });
	        
	        upload.addListener(new Upload.ProgressListener() {
	            public void updateProgress(long readBytes, long contentLength) {
	                // this method gets called several times during the update
	                pi.setValue(new Float(readBytes / (float) contentLength));
	                textualProgress.setValue("Processed " + readBytes
	                        + " bytes of " + contentLength);
	            }

	        });

	        upload.addListener(new Upload.SucceededListener() {
	            public void uploadSucceeded(SucceededEvent event) {
	            }
	        });

	      

	        upload.addListener(new Upload.FinishedListener() {
	            public void uploadFinished(FinishedEvent event) {
	                state.setValue("Idle");
	                pi.setVisible(false);
	                textualProgress.setVisible(false);
	                table.populateTable(path);
	                close.setVisible(true);
	            }
	        });
	        
			close.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					window.close();
				}
			});
			
//			FinishedListener finishedListener = new FinishedListener() {			
//				@Override
//				public void uploadFinished(FinishedEvent event) {
//					// TODO Auto-generated method stub
//					table.populateTable(path);
//					window.close();
//				}
//			};
//			upload.addFinishedListener(finishedListener);
			
			
			//set size
			window.setHeight("300px");
			window.setWidth("500px");
			window.setContent(verticalLayout);
			
		}
	};

	MouseEvents.ClickListener addFolderListener = new MouseEvents.ClickListener() {

		@Override
		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
			// TODO Auto-generated method stub
			String path = client.getCurrentFolder().getPath();
			client.createFolder(path, "new folder");
			table.populateTable(path);
			
		}};
	
	
	ItemClickListener treeListener = new ItemClickEvent.ItemClickListener() {
		private static final long serialVersionUID = 1L;
		List<String> itemExpanded = new ArrayList<>();
		String itemId = null;
		String path = null;
		@SuppressWarnings("deprecation")
		public void itemClick(ItemClickEvent event) {
			// Pick only left mouse clicks
			itemId = (String) event.getItemId();
			if (event.getButton() == ItemClickEvent.BUTTON_LEFT && event.isDoubleClick()){
				path = getTreePath(tree, itemId);
				//update folder component
				table.populateTable(path);
				if(itemExpanded.contains(itemId)){
					//if expanded, collapse
					tree.collapseItem(itemId);
					itemExpanded.remove(itemId);
				}else{
					tree.expandItem(itemId);
					itemExpanded.add(itemId);
				}				
			}
		}
	};

	public static String getTreePath(Tree tree, String itemId){
		//return path of folder selected
		if(itemId.equals("/")){
			return "/";
		}		
		String path = "/" + itemId;
		while(!tree.getParent(itemId).equals("/") && tree.getParent(itemId) != null ){
			itemId = (String) tree.getParent(itemId);
			path = "/" + itemId +  path;
		}		
		return path;
	}
}
