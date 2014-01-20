package com.github.atave.VaadinCmisBrowser.vaadin.ui;

import java.util.HashMap;
import java.util.Iterator;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.servlet.annotation.WebServlet;

@Theme("dashboard")
@SuppressWarnings("serial")
public class AppUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "com.github.atave.VaadinCmisBrowser.vaadin.ui.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    CssLayout root = new CssLayout();
	VerticalLayout loginLayout;
	CssLayout menu = new CssLayout();
	CssLayout content = new CssLayout();
	private Navigator nav;

	HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>() {
		{
			put("/home", HomeView.class);
			put("/search", SearchView.class);
		}
	};

	HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();

	@Override
	protected void init(VaadinRequest request) {
		setContent(root);
		root.addStyleName("root");
		root.setSizeFull();
        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);
		buildLoginView(false);
	}
	
	 private void buildLoginView(boolean exit) {
	        if (exit) {
	            root.removeAllComponents();
	        }
//	        helpManager.closeAll();
//	        HelpOverlay w = helpManager
//	                .addOverlay(
//	                        "Welcome to the Dashboard Demo Application",
//	                        "<p>This application is not real, it only demonstrates an application built with the <a href=\"http://vaadin.com\">Vaadin framework</a>.</p><p>No username or password is required, just click the â€˜Sign Inâ€™ button to continue. You can try out a random username and password, though.</p>",
//	                        "login");
//	        w.center();
//	        addWindow(w);

	        addStyleName("login");

	        loginLayout = new VerticalLayout();
	        loginLayout.setSizeFull();
	        loginLayout.addStyleName("login-layout");
	        root.addComponent(loginLayout);

	        final CssLayout loginPanel = new CssLayout();
	        loginPanel.addStyleName("login-panel");

	        HorizontalLayout labels = new HorizontalLayout();
	        labels.setWidth("100%");
	        labels.setMargin(true);
	        labels.addStyleName("labels");
	        loginPanel.addComponent(labels);

	        Label welcome = new Label("Welcome");
	        welcome.setSizeUndefined();
	        welcome.addStyleName("h4");
	        labels.addComponent(welcome);
	        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

	        Label title = new Label("QuickTickets Dashboard");
	        title.setSizeUndefined();
	        title.addStyleName("h2");
	        title.addStyleName("light");
	        labels.addComponent(title);
	        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

	        HorizontalLayout fields = new HorizontalLayout();
	        fields.setSpacing(true);
	        fields.setMargin(true);
	        fields.addStyleName("fields");

	        final TextField username = new TextField("Username");
	        username.focus();
	        fields.addComponent(username);

	        final PasswordField password = new PasswordField("Password");
	        fields.addComponent(password);

	        final Button signin = new Button("Sign In");
	        signin.addStyleName("default");
	        fields.addComponent(signin);
	        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

//	        final ShortcutListener enter = new ShortcutListener("Sign In",
//	                KeyCode.ENTER, null) {
//	            @Override
//	            public void handleAction(Object sender, Object target) {
//	                signin.click();
//	            }
//	        };

	        signin.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	                if (username.getValue() != null
	                        && username.getValue().equals("")
	                        && password.getValue() != null
	                        && password.getValue().equals("")) {
//	                    signin.removeShortcutListener(enter);
	                    buildMainView();
	                } else {
	                    if (loginPanel.getComponentCount() > 2) {
	                        // Remove the previous error message
	                        loginPanel.removeComponent(loginPanel.getComponent(2));
	                    }
	                    // Add new error message
	                    Label error = new Label(
	                            "Wrong username or password. <span>Hint: try empty values</span>",
	                            ContentMode.HTML);
	                    error.addStyleName("error");
	                    error.setSizeUndefined();
	                    error.addStyleName("light");
	                    // Add animation
	                    error.addStyleName("v-animate-reveal");
	                    loginPanel.addComponent(error);
	                    username.focus();
	                }
	            }
	        });

//	        signin.addShortcutListener(enter);

	        loginPanel.addComponent(fields);

	        loginLayout.addComponent(loginPanel);
	        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	    }

	private void buildMainView() {

		nav = new Navigator(this, content);

		for (String route : routes.keySet()) {
			nav.addView(route, routes.get(route));
		}

		removeStyleName("login");
		root.removeComponent(loginLayout);
		root.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				addStyleName("main-view");
				addComponent(new VerticalLayout() {
					// Sidebar
					{
						addStyleName("sidebar");
						setWidth(null);
						setHeight("100%");

						// Branding element
						addComponent(new CssLayout() {
							{
								addStyleName("branding");
								Label logo = new Label(
										"<span>QuickTickets</span> Dashboard",
										ContentMode.HTML);
								logo.setSizeUndefined();
								addComponent(logo);
								// addComponent(new Image(null, new
								// ThemeResource(
								// "img/branding.png")));
							}
						});

						// Main menu
						addComponent(menu);
						setExpandRatio(menu, 1);

						// User menu
						addComponent(new VerticalLayout() {
							{
								setSizeUndefined();
								addStyleName("user");
								Image profilePic = new Image(
										null,
										new ThemeResource("img/profile-pic.png"));
								profilePic.setWidth("34px");
								addComponent(profilePic);
								Label userName = new Label("Mario Rossi");
								userName.setSizeUndefined();
								addComponent(userName);

								Command cmd = new Command() {
									@Override
									public void menuSelected(
											MenuItem selectedItem) {
										Notification
										.show("Not implemented in this demo");
									}
								};
								MenuBar settings = new MenuBar();
								MenuItem settingsMenu = settings.addItem("",
										null);
								settingsMenu.setStyleName("icon-cog");
								settingsMenu.addItem("Settings", cmd);
								settingsMenu.addItem("Preferences", cmd);
								settingsMenu.addSeparator();
								settingsMenu.addItem("My Account", cmd);
								addComponent(settings);

								Button exit = new NativeButton("Exit");
								exit.addStyleName("icon-cancel");
								exit.setDescription("Sign Out");
								addComponent(exit);
								exit.addClickListener(new ClickListener() {
									@Override
									public void buttonClick(ClickEvent event) {
										buildLoginView(true);
									}
								});
							}
						});
					}
				});
				// Content
				addComponent(content);
				content.setSizeFull();
				content.addStyleName("view-content");
				setExpandRatio(content, 1);
			}

		});

		menu.removeAllComponents();

		for (final String view : new String[] { "home", "search" }) {
			Button b = new NativeButton(view.substring(0, 1).toUpperCase()
					+ view.substring(1).replace('-', ' '));
			b.addStyleName("icon-" + view);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					clearMenuSelection();
					event.getButton().addStyleName("selected");
					if (!nav.getState().equals("/" + view))
						nav.navigateTo("/" + view);
				}
			});


			if (view.equals("home")) {
				// Add drop target to reports button
				menu.addComponent(b);
			} else {
				menu.addComponent(b);
			}
			viewNameToMenuButton.put("/" + view, b);
		}

		menu.addStyleName("menu");
		menu.setHeight("100%");

		String f = Page.getCurrent().getUriFragment();
		if (f != null && f.startsWith("!")) {
			f = f.substring(1);
		}
		if (f == null || f.equals("") || f.equals("/")) {
			nav.navigateTo("/home");
			menu.getComponent(0).addStyleName("selected");
			
		} else {
			nav.navigateTo(f);
			viewNameToMenuButton.get(f).addStyleName("selected");
		}

	}
	
	private void clearMenuSelection() {
	    for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
	        Component next = it.next();
	        if (next instanceof NativeButton) {
	            next.removeStyleName("selected");
	        } else if (next instanceof DragAndDropWrapper) {
	            // Wow, this is ugly (even uglier than the rest of the code)
	            ((DragAndDropWrapper) next).iterator().next()
	                    .removeStyleName("selected");
	        }
	    }
	}

}