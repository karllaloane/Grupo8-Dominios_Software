package br.ufg.sep.views.permissoes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.RoleUserService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;
import br.ufg.sep.views.MainLayout;

@Route(value="permissoes", layout= MainLayout.class)
@RolesAllowed({"ADMIN"})
@PageTitle("Permissoes")
public class PermissoesView extends VerticalLayout{
	
	CadastroRepository cadastroRepository;
	
	private Grid<Cadastro> cadastros;
	
	public PermissoesView(CadastroRepository cadastroRepository, RoleUserService roleUserService) {
		
		this.cadastroRepository = cadastroRepository;
		
		this.cadastros = new GridCadastroFactory(cadastroRepository).getGrid();
         
        cadastros.addComponentColumn(person -> {
        	 
        	 /* Cria um menu */
        	 MenuBar menuBar = new MenuBar();
        	 /* Nome do primeiro e único item do menu*/
        	 MenuItem options = menuBar.addItem("Alterar");
        	 
        	 /* */ 
        	 List<RoleUser> rolesUserPerson = roleUserService.getRoleUserRepository().findByUserCpf(person.getCpf());
        	 
        	 List<Role> roleList = new ArrayList<>();
        	 
        	 rolesUserPerson.forEach(rolePerson->{
        		 roleList.add(rolePerson.getRole());
        	 });
        	 
        	 /* Checkboxs que serão os subitens */
        	 
        	 /*ADM*/
        	 Checkbox checkbox = new Checkbox();
        	 checkbox.setLabel("Administrador(a) do Sistema");
        	 checkbox.setValue(roleList.contains(Role.ADMIN));
        	 
        	 checkbox.addValueChangeListener(event->{
        		 
        		 if(event.getValue()==true) {
        			 roleUserService.save(new RoleUser(Role.ADMIN,person.getCpf()));
        			 Notification.show("Salvo como Administrador(a)");
        			 return;
        		 }
        		 rolesUserPerson.forEach(roleUser->{
        			 if(roleUser.getRole().equals(Role.ADMIN)) {
        				 checkbox.setValue(true);
        		     //roleUserService.getRoleUserRepository().delete(roleUser);
        			 }
        		 });
        		 
        	 });
        	 
        	 
        	 
        	 /*ADM*/
        	 
        	 Checkbox checkbox2 = new Checkbox();
        	 checkbox2.setLabel("Pedagógico(a)");
        	 checkbox2.setValue(roleList.contains(Role.PED));
        	 
        	 checkbox2.addValueChangeListener(event->{
        		 if(event.getValue()==true) {
        			 roleUserService.save(new RoleUser(Role.PED,person.getCpf()));
        			 Notification.show("Salvo como Pedagógico(a)");
        			 return;
        		 }
        		 rolesUserPerson.forEach(roleUser->{
        			 if(roleUser.getRole().equals(Role.PED)) {roleUserService.getRoleUserRepository().delete(roleUser);
        			 Notification.show("Permisão removida - Pedagógico(a)");
        			 }
        		 });
        		 
        	 });
        	 
        	 
        	 Checkbox checkbox3 = new Checkbox();
        	 checkbox3.setLabel("Professor(a)");
        	 checkbox3.setValue(roleList.contains(Role.PROF));
        	 checkbox3.addValueChangeListener(event->{
        		 if(event.getValue()==true) {
        			 roleUserService.save(new RoleUser(Role.PROF,person.getCpf()));
        			 Notification.show("Salvo como Professor(a)");
        			 return;
        		 }
        		 rolesUserPerson.forEach(roleUser->{
        			 if(roleUser.getRole().equals(Role.PROF)) {roleUserService.getRoleUserRepository().delete(roleUser);
        			 Notification.show("Permisão removida - Professor");
        			 }
        		 });
        		 
        	 });

        	 Checkbox checkbox4 = new Checkbox();
        	 checkbox4.setLabel("Usuário");
        	 checkbox4.setValue(roleList.contains(Role.USER));
        	 checkbox4.addValueChangeListener(event->{
        		 if(event.getValue()==true) {
        			 roleUserService.save(new RoleUser(Role.USER,person.getCpf()));
        			 Notification.show("Salvo como Usuário(a)");
        			 return;
        		 }
        		 rolesUserPerson.forEach(roleUser->{
        			 if(roleUser.getRole().equals(Role.USER)) {roleUserService.getRoleUserRepository().delete(roleUser);
        			 Notification.show("Permisão removida - Usuário");
        			 }
        		 });
        		 
        	 });

        	 /* Cria o submenu */
             SubMenu subItems = options.getSubMenu();
             
             /* Adiciona checkbox nos sub itens*/ 
             MenuItem sub = subItems.addItem(checkbox);
             MenuItem sub2 = subItems.addItem(checkbox2);
             MenuItem sub3 = subItems.addItem(checkbox3);
             MenuItem sub4 = subItems.addItem(checkbox4);
             
              
             
             /* Entendendo ainda*/
        	 String cpfClicado = person.getCpf();

             sub.addClickListener(e->{
            	  if(roleUserService.getRoleUserRepository().findByUserCpf(cpfClicado)==null) {
            		  return; 
            	  }
            	  
            	  
              });
              
              
              return menuBar;
              
        	}).setHeader("Permissões");
         
         
        
		add(cadastros);
		
	}

}
