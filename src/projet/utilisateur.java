package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class utilisateur {
private int id_utilisateur;
private String nom,prenom,login,password;
public utilisateur(String n,String p ,String lg,String pwd) {
	Random r = new Random();
	this.id_utilisateur=r.nextInt();
	this.nom=n;
	this.password=p;
	this.login=lg;
	this.password=pwd;
}
public utilisateur(String lg,String pwd) {
	this.login=lg;
	this.password=pwd;
}
public int getId_utilisateur() {
	return id_utilisateur;
}
public void setId_utilisateur(int id_utilisateur) {
	this.id_utilisateur = id_utilisateur;
}
public String getNom() {
	return nom;
}
public void setNom(String nom) {
	this.nom = nom;
}
public String getPrenom() {
	return prenom;
}
public void setPrenom(String prenom) {
	this.prenom = prenom;
}
public String getLogin() {
	return login;
}
public void setLogin(String login) {
	this.login = login;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public void login(String login,String password) throws Exception {
	Connection con = null;
	try {
	      Class.forName("com.mysql.cj.jdbc.Driver");

	      con = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
	      Scanner scanner=new Scanner (System.in);
	      int num=0;
	      do {
	    	  System.out.println("se connceter tant que : \n\t 1:etudiant \n\t 2:enseignant \n\t 3:responsable");
	    	  num=scanner.nextInt();
	      }while(num==0 || num>3);
	      String sql;
	      if (num==1) {
	    	  sql = "SELECT * FROM etudiant WHERE login = ? AND pwd = ?";
	      }else if (num==2) {
	    	  sql = "SELECT * FROM enseignant WHERE login = ? AND pwd = ?";
	      }
	      else {
	    	  sql = "SELECT * FROM responsable WHERE login = ? AND pwd = ?";
	      }
	      
	      PreparedStatement statement = con.prepareStatement(sql);
	      statement.setString(1, login);
	      statement.setString(2, password);
	      ResultSet result = statement.executeQuery();
	      if (result.next()) {
	          System.out.println("Connexion réussie!");
	        } else {
	          System.out.println("Connexion échouée.");
	        }
	      scanner.close();
	      
	}catch(Exception e) {
		System.out.println(e.toString());
	}finally {
		if (con!=null) {
			con.close();
		}
	}
}
public void consulterAbsence() throws SQLException {
	Connection con = null;
	try {
	      Class.forName("com.mysql.cj.jdbc.Driver");

	      con = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
	      String sql="SELECT e.nom,e.prenom,a.numseance FROM etudiant as e ,absence as a WHERE e.id_etudiant=a.id_etudiant ";
	      PreparedStatement stmt = con.prepareStatement(sql);
	      ResultSet rs = stmt.executeQuery();
	      while (rs.next()) {
	            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getInt(3));
	          }
	}catch(Exception e) {
		System.out.println(e.toString());
	}finally {
		if (con!=null) {
			con.close();
		}
	}
}
}
