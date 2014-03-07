package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.github.atave.VaadinCmisBrowser.cmis.api.DocumentView;
import com.vaadin.ui.Image;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class DownloadDocumentComponent extends CustomComponent {


	public DownloadDocumentComponent(String documentId, Integer itemId , final Table table) {
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
		ClickListener documentDownloadListener = new ClickListener() {
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				//TODO download dei documenti
				Notification.show("start download document nomedoc");
			}
		};
		documentDownload.addClickListener(documentDownloadListener);

		ClickListener panelListener = new ClickListener() {
		//select table row
		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
			Integer id = (Integer)((Panel) event.getComponent()).getData();
			table.select(id);
		}
	};
		panel.addClickListener(panelListener);
		layout.addComponent(documentDownload);
		layout.setComponentAlignment(documentDownload, Alignment.MIDDLE_CENTER);
		setCompositionRoot(panel);

	}

}