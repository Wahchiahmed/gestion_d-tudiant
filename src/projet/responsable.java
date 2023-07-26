package projet;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class responsable extends utilisateur {
	
public responsable(String n , String p,String lg,String pwd) {
	super(n,p,lg,pwd);
}
responsable(String lg,String pwd){
	super(lg,pwd);
}
public void ImprimerListeAbsence() throws SQLException {
	Connection con = null;
	try {
	      Class.forName("com.mysql.cj.jdbc.Driver");

	      con = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
	      String sql="SELECT e.nom, e.prenom, a.numseance, a.date, en.nom, en.prenom"
	        		+ " FROM etudiant e ,absence a,enseignant en "
	        		+ " WHERE e.id_etudiant=a.id_etudiant and a.id_enseignant=en.id_enseignant ";
	      PreparedStatement stmt = con.prepareStatement(sql);
	      ResultSet rs = stmt.executeQuery();
	      // Create a new document
	      Document document = new Document();
	      PdfWriter.getInstance(document, new FileOutputStream("Absence_List1.pdf"));
	      document.open();
	      // Create a table and add the result of the query to the table
	      PdfPTable table = new PdfPTable(6);
	      while (rs.next()) {
	          for (int i = 1; i <= 6; i++) {
	              table.addCell(rs.getString(i));
	          }
	      }
	      document.add(table);
	      System.out.println("liste imprimer");
	      document.close();
	}catch(Exception e) {
		System.out.println(e.toString());
	}finally {
		if (con!=null) {
			con.close();
		}
	}
}
public void AnnulerAbsence() throws SQLException {
	Connection con = null;
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(
		          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
		Scanner sc = new Scanner (System.in);
		System.out.println("Entrer  id etudiant");
		int id_etudiant=sc.nextInt();
		System.out.println("Entrer la numéro de la seance ");
		int numseance=sc.nextInt();
		System.out.println("Entrer l'id de votre enseignant");
		int id_enseignant=sc.nextInt();
		System.out.println("Entrer l'id de la matiere");
		int id_matiere=sc.nextInt();
		sc.nextLine();
		String sql="DELETE FROM absence WHERE id_etudiant = ? AND id_enseignant = ? AND id_matiere = ? AND numseance = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, id_etudiant);
		stmt.setInt(2, id_enseignant);
		stmt.setInt(3, id_matiere);
		stmt.setInt(4, numseance);
		stmt.executeUpdate();
		int rowsDeleted = stmt.executeUpdate(sql);
	    if (rowsDeleted > 0) {
	      System.out.println("Absence annulée");
	    }
		sc.close();
		stmt.close();
		
	}catch(Exception e) {
		System.out.println(e.toString());
	}
	finally {
		if (con!=null) {
			con.close();
		}
	}
}
public void GénererGraphe() throws SQLException {
        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createBarChart(
                "Absenteeism Rate by Class Level", // chart title
                "Class Level", // domain axis label
                "Absenteeism Rate", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        javax.swing.JFrame frame = new javax.swing.JFrame("Absenteeism Rate by Class Level");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Double> results = getData();
        for (String classLevel : results.keySet()) {
            dataset.addValue(results.get(classLevel), "Absenteeism Rate", classLevel);
        }

        return dataset;
    }

    private Map<String, Double> getData() {
        Map<String, Double> results = new LinkedHashMap<>();

        // Connect to the database
        try (Connection connection = DriverManager.getConnection(
        		"jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123")) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Classe.niveau, COUNT(Absence.id_etudiant) / COUNT(Etudiant.id_etudiant) AS taux_absence\r\n"
            		+ "FROM Classe\r\n"
            		+ "JOIN Etudiant ON Classe.id_classe = Etudiant.id_classe\r\n"
            		+ "LEFT JOIN Absence ON Etudiant.id_etudiant = Absence.id_etudiant\r\n"
            		+ "GROUP BY Classe.niveau");
                
          

            while (rs.next()) {
                results.put(rs.getString("niveau"), rs.getDouble("taux_absence"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    
}
public void EnvoyerMailAlert(String recepient) {
	try {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(super.getLogin(),"btrumtkoyshmgtdh"));
        email.setStartTLSEnabled(true);
        email.setFrom(super.getLogin());
        email.setSubject("Alert d'absence !!");
        email.setMsg("vous avez 2 absence ! une autre absence et tu sera éliminer");
        email.addTo(recepient);
        email.send();
        System.out.println("Email sent!");
    } catch (EmailException e) {
        e.printStackTrace();
    }
}	
}