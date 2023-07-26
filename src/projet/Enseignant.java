package projet;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Enseignant extends utilisateur {
	
	Enseignant(String n , String p,String lg,String pwd) {
		super(n,p,lg,pwd);
	}
	Enseignant(String lg,String pwd){
		super(lg,pwd);
	}
	public void RemplirListeAbsence() throws SQLException,inputException  {
		Connection con = null;
		try {
		      Class.forName("com.mysql.cj.jdbc.Driver");

		      con = DriverManager.getConnection(
			          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
		      Scanner sc = new Scanner (System.in);
		      int response;
		      int id_enseignant,id_etudiant,id_matiere,numseance;
		      String date;
		      do {
		    	  System.out.println("Entrer l'id de l'enseignant :");
		    	  
		    		  id_enseignant=sc.nextInt();
		    		  sc.nextLine();
		    	 
		    	  System.out.println("Entrer l'id de l'etudiant : ");
		    	  id_etudiant=sc.nextInt();
		    	  System.out.println("Entrer l'id de la matière : ");
		    	  id_matiere=sc.nextInt();
		    	  System.out.println("Entrer le numéro de la séance : ");
		    	  numseance=sc.nextInt();
		    	  System.out.print("Date de l'absence (YYYY-MM-DD) : ");
		    	  sc.nextLine();
		          date = sc.nextLine();
		          String sql = "INSERT INTO absence VALUES (?, ?, ?, ?, ?)";
		          PreparedStatement stmt = con.prepareStatement(sql);
		          stmt.setInt(1, id_etudiant);
		          stmt.setInt(2, id_enseignant);
		          stmt.setInt(3, id_matiere);
		          stmt.setInt(4, numseance);
		          stmt.setDate(5, java.sql.Date.valueOf(date));
		          stmt.executeUpdate();
		          do {
		        	  System.out.println("vous avez terminée ? \n 1:oui \n 2:non");
		        	  response=sc.nextInt();
		          }while (response==0 || response>3);
		      }while (response==2 );
		      sc.close();
		      
		}catch (Exception e) {
			System.out.println(e.toString());
		}finally{
			if (con!=null) {
				con.close();
			}
		}
	}
}