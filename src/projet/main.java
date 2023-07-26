package projet;
import java.sql.*;
import java.util.Scanner;

public class main {
	public static void main(String[] args) throws SQLException {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
			          "jdbc:mysql://localhost:3306/javaproject", "root", "AhmedAhmed123");
			
			 Scanner scanner = new Scanner(System.in);
			 System.out.println("Entrer votre login");
			 String login=scanner.nextLine();
			 System.out.println("Entrer votre password");
			 String password=scanner.nextLine();
			 int num ;
			 do {
				 System.out.println("Vous etes : \n\t 1:Etudiant \n\t 2:Enseignant \n\t 3:responsable");
				 num=scanner.nextInt();
			 }while(num==0 && num>4);
			 scanner.nextLine();
			 int choix;
			 if (num==1) {
				 try {
					 Etudiant e = new Etudiant (login,password);
					 e.login(login, password);
					 e.consulterAbsence();
				 }catch(Exception e) {
					 System.out.println(e.toString());
				 } 
			 }else if (num==2) {
				 try {
					 Enseignant e1 = new Enseignant (login,password);
					 System.out.println("Qu'est ce que vous voulez faire ?: \n\t1- Remplir la liste de Présence ? \n\t2- Consulter Les Absences ?");
					 choix=scanner.nextInt();
					 e1.login(login, password);
					 if (choix == 1) {
						 e1.RemplirListeAbsence();
					 } else {
						 e1.consulterAbsence();
					 }
				 }catch(Exception e) {
					 System.out.println(e.toString());
				 }
			 }else {
				 responsable r = new responsable(login,password);
				 System.out.println("1:Annuler Absence \n 2:Imprimer Liste absence \n 3:Envoyer mail alert \n 4: génerer graphe");
				choix=scanner.nextInt();
				if(choix==1) {
					r.AnnulerAbsence();
				}else if (choix==2) {
					r.ImprimerListeAbsence();
				}else if (choix==3) {
					String query = "select login,count(numseance) from etudiant as e ,absence as a where e.id_etudiant=a.id_etudiant group by login having (count(numseance)=2)";
					PreparedStatement stmt = con.prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						String column1 = rs.getString("login");
					    r.EnvoyerMailAlert(column1);
					}
					rs.close();
					stmt.close();
					
				}else {
					r.GénererGraphe();
				}				 
			 }
		      scanner.close();
		}catch(Exception e) {
			System.out.println(e.toString());
		}finally {
			if(con!=null) {
				con.close();
			}
		}

	}
}