//Megane Dandurand et Marc-Arthur Nougbode
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/*MENU PRINCIPAL ON DOIT CHOISIR UN TEXTE (SOIT ON L'ECRIT OU ON OUVRE UN FICHIER TEXTE)
 puis selectionne le Dictionnaire donne et ensuite on fait la verif et clique avec le 
 bouton de gauche sur les mots surlignes en rouge pour les changer par une correction
  du dico. Le menu principal est toujours ouvert unless we close it alors on peut cliquer
  sur fichier et en ouvrir un et puis cliquer sur un bouton du meme menu*/
 
public class UserInterface extends JFrame implements ActionListener{ //menu avec boutons pour l'utilisateur
	private JButton b1;
	private JButton b2;
	private JButton b3;
	JFrame frame;
	JFileChooser chooser;
	Model textArea;
	FileChooser fc;
	JTextArea log;
	TextAreaHighlight high;
	String[] dictionnaire;
	String[] mots;

	public UserInterface(){ //constructeur qui montre le menu avec les 3 options
		//Le menu
	JFrame menu = new JFrame("Menu");
	//Les 3 boutons
	b1=new JButton("Fichier");    
	menu.add(b1, BorderLayout.WEST);
	b2=new JButton("Dictionnaire");   
	menu.add(b2, BorderLayout.CENTER);
	b3=new JButton("Verifier");  
	menu.add(b3, BorderLayout.EAST);
	//les actionListeners
	b1.addActionListener(this);
    b2.addActionListener(this);
    b3.addActionListener(this);

	menu.pack();   
	menu.setVisible(true);    
	menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	}
	
	public static void main(String[] args) {
		UserInterface test = new UserInterface();
	}
	@Override
	public void actionPerformed(ActionEvent e) { 
		//si clique sur fichier, Voir si input le texte ou lit d'un fichier
		if (e.getSource() == b1) {
			fileOrInput();
		}
		//si clique sur dictionnaire 
		/*-Afficher une selection de texte
	-Lire l'entree de l'user
	-Demander au Model de lire le fichier correspondant
	et garder un memoire*/
		//JFileChooser
		/*Quand on clique sur le menu «Dictionnaire », on veut charger un dictionnaire à 
		 * partir d’un fichier. Ce dictionnaire sera alors stocké dans une structure de 
		 * données interne (e.g. un tableau, une liste chaînée, ou une autre structure de
		 *  votre comme HashSet*/
		else if (e.getSource() == b2) {
			b2.setEnabled(false);
			dictionnaire();
			
		}
		/*-Afficher le texte du fichier a l'ecran dans une boite de
	texte
	-Detecter si aucun fichier n'a ete charge, dire a l'user
	de charger un fichier avant la verification
	  -Demander au model de nous donner les positions de mots
	  qui ne sont pas dans le dictionnaire
		-Appliquer une selection des mots aux positions
		retournees par le model*/
		else if (e.getSource() == b3) {
		//si tous les boutons ont ete clique alors on peut faire la verif.
			if((textArea!=null || fc!=null) && dictionnaire!=null) {
				b3.setEnabled(false);
		  erreurs();
			}
			else {
				//si il n'y a pas de dictionnaire mais il y a un textArea
				if((textArea!=null||fc!=null)) {
					System.out.println("Veuillez ouvrir un fichier dictionnaire avant la verification");
					dictionnaire();
					b3.setEnabled(false);
					erreurs();
					
				}
				//si il n'y a pas de textArea mais il y a un dico
				else if(dictionnaire!=null ) {
					System.out.println("Veuillez cliquer sur le bouton fichier");
				}
				//si aucun des deux n'est fait
				else {
					System.out.println("Veuillez cliquer sur le bouton fichier puis Dictionnaire");
				}
			}
				
		   }
	}
	public void fileOrInput(){//Selectionner si entre texte ou lit texte
		System.out.println("Veuillez ecrire 'ecrire' afin d'entrer votre texte dans la console ou 'lire' pour le lire d'un fichier: ");
		Scanner clavier = new Scanner(System.in);
	        String nom = clavier.next();
	        if(nom.equals("ecrire")) { //si ecrit en console
	        	inputText();
	        	 clavier.close();
	        }
	        else if(nom.equals("lire")) {//si ouvrir fichier
	        	menufichier();
	        	 clavier.close();
	        }
	        else {
	        	System.err.println("Probleme avec le input. Veuillez ecrire 'ecrire' ou bien 'lire'");
	        }
	        }
	//Si on doit lire un fichier
	public void menufichier() {//Sous menu de fichier
		JFrame frame = new JFrame("FileChooserDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fc = new FileChooser();
		//Add content to the window.
		frame.add(fc);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
		b1.setEnabled(false);
	
	}
	public void inputText() {//Ecrire texte dans la console
		//JE N'AI PAS FAIT DE SAVE BUTTON PUISQUE JE METS MON FICHIER ECRIT DIRECT DANS UN TEXTAREA
		System.out.print("Veuillez ecrire votre texte dans la console:  et ajoutez 'exit' pour signaler la fin du texte");
		textArea = new Model();
            textArea.textAreaWrite();
       	//null exception a textarea
       		textArea.setDefaultCloseOperation( EXIT_ON_CLOSE );
            textArea.pack();
            textArea.setVisible(true);
            b1.setEnabled(false);
       	 }
	//Pour choisir notre dictionnaire
	public void dictionnaire() {
		chooser = new JFileChooser();
		try {
			if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				BufferedReader in = new BufferedReader(new FileReader(chooser.getSelectedFile()));
				String line = in.readLine();
				ArrayList<String> list = new ArrayList<String>();
				while(line!=null) {
					list.add(line);
					line = in.readLine();
				}
				dictionnaire = new String[list.size()];
				dictionnaire = list.toArray(dictionnaire);
				System.out.println(dictionnaire);
				in.close();
			}
		}
		catch(IOException e){
			System.out.println("Fichier inexistant");
			
		}

	}
	//Pour faire la verification!
	public void erreurs() {
	   high = new TextAreaHighlight();
	  //si le texte est lu d'un fichier
	   if(fc != null) {
		   //mots du texte mis dans un array
		mots = new String[fc.array.length];
		for(int i = 0;i<fc.array.length;i++) {
			mots[i] = fc.array[i];
		}
	   //pour highlight les mots errones
	   			high.highlight(fc.textarea.texteAlire, dictionnaire, mots);	
	   }
	   //si cest un texte ecrit
	   			else {
	   				//array des mots du texte
	   				mots = new String[textArea.tokenizing().length];
	   				for(int i = 0;i<textArea.tokenizing().length;i++) {
	   					mots[i] = textArea.tokenizing()[i];
	   				}
	   				//highlight les mots errones
	   			   	high.highlight(textArea.texteAlire, dictionnaire, mots);
	   			}
	   //pour faire la suggestion/correction
	   high.sug();
		}
	}
