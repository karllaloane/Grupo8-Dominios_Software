
package br.ufg.sep.views;

import java.util.Optional;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.security.AuthenticatedUser;
import br.ufg.sep.test.TestView;
import br.ufg.sep.views.home.HomeView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private static H2 viewTitle;

    private AccessAnnotationChecker accessChecker;
    private VerticalLayout header;
    public  Tabs secondaryMenu;
    AuthenticatedUser authenticatedUser;
    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER); // o drawer que estará de forma primária
        addDrawerContent();
        addHeaderContent();
        
        
    }

   

	private void addHeaderContent() {
		
		
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle"); // botao de ocutar o drawer

        viewTitle = new H2(); // titulo da página em exibição no momento 
        
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        viewTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
        .set("margin", "0");
        
        HorizontalLayout upperHeader = new HorizontalLayout(toggle,viewTitle);
        upperHeader.setAlignItems(Alignment.CENTER);
        upperHeader.setSpacing(true);
        
         header = new VerticalLayout(upperHeader);
        addToNavbar(true, header);
    }
	
	
    

/***************************/
	//Adição do "SEP" como titulo, do botaozinho de deslogar, e da barra de navegação com o createNavigation.
	private void addDrawerContent() {
        H1 appName = new H1("SEP");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        //Scroller scroller = new Scroller(createNavigation());
        /*
        verticalLayout.add(header);
        verticalLayout.setAlignSelf(Alignment.START);
        */        
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(header);
        verticalLayout.setAlignSelf(Alignment.START); // tentadno alinhar o SEP
        
        addToDrawer(verticalLayout,createNavigation(), createFooter());
    }
	/***************************/


    private Tabs createNavigation() {
  
        Tabs nav = new Tabs();

        if (accessChecker.hasAccess(HomeView.class)) {
            nav.add(new Tab(new RouterLink("Home",HomeView.class)));

        }
        if (accessChecker.hasAccess(TestView.class)) {
            nav.add(new Tab( new RouterLink("Test",TestView.class)));
        }
        
        nav.setOrientation(Orientation.VERTICAL);

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<Cadastro> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
        	Cadastro user = maybeUser.get();

            Avatar avatar = new Avatar(user.getNome());
//            StreamResource resource = new StreamResource("profile-pic",
//                    () -> new ByteArrayInputStream(user.getProfilePicture()));
//            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getNome());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
//            userName.getSubMenu().addItem("Sign out", e -> {
//                authenticatedUser.logout();
//            });
            userName.getSubMenu().addItem("Sair", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
