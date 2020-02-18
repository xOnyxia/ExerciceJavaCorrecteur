//Megane Dandurand et Marc-Arthur Nougbode
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class Model extends JFrame{
	  //creer un objet determinant la size du JTextArea 
	JTextArea texteAlire;
	JScrollPane panel;
	String texte;
	String ligne;   
	StringTokenizer st2;
	ArrayList<String> arraylisttoken;
	String[] arraydestokens;
	String nom="";

	public Model() {
	  
	// faire une textArea du texte
		
        
        	 texteAlire = new JTextArea();//le texte input est le textArea //mettre la size du TextArea
        	 texteAlire.setFont(new Font("Arial Black", Font.PLAIN, 20));
        	 texteAlire.setLineWrap(true);
        	 texteAlire.setSize( texteAlire.getPreferredSize() );
	}
	//si c'est le textarea d'un text input par le user
	public void textAreaWrite() {
			Scanner in = new Scanner(System.in);
			if(in.next().equals("exit")) {
				System.out.println("Please write a non-empty text");
				textAreaWrite();
			}
			else {
				texte = "";
			while(in.hasNext()) {
				nom = in.next();
				if(nom.equals("exit")) {
					break;
				}
				texte += (" " + nom);
			}
			in.close();
		    texte.replaceAll("\\p{Punct}", " ");
		    texteAlire.append(texte);
		 add(texteAlire);
		 texteAlire.setVisible(true);
	     	panel = new JScrollPane(texteAlire);
	         getContentPane().add(panel);
	         panel.setVisible(true);
	         setVisible(true);
		     	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
}
	//si c'est un textArea d'un texte venant d'un fichier
	public void textAreaFile(File file) { 
		try {    
			BufferedReader input = new BufferedReader(new FileReader(file)); 
		    // lire et traiter chaque ligne                  
			ligne = input.readLine();  
			texte ="";
			//cb de mots
			while (ligne != null) {       
				texte += ligne;
				ligne = input.readLine();
				} 
		    input.close(); 
		    texte.replaceAll("\\p{Punct}", " ");
		    texteAlire.append(texte);
		    add(texteAlire);
	     	texteAlire.setVisible(true);
	     	panel = new JScrollPane(texteAlire);
	         getContentPane().add(panel);
	         panel.setVisible(true);
	         setVisible(true);
		     	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    }        
		catch(IOException e1){            
		System.err.println("erreur fichier" + e1.toString());      
		}
	}
	//fait la tokenization et retourne un array des tokens
	public String[] tokenizing() {  
		st2 = new StringTokenizer(texte);
		arraylisttoken = new ArrayList<String>();
			while (st2.hasMoreElements()) {
			 String token2 = st2.nextElement().toString();
			 arraylisttoken.add(token2);
			}
			arraydestokens = arraylisttoken.toArray(new String[arraylisttoken.size()]);
			return arraydestokens;
			}    
}