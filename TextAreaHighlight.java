//Megane Dandurand et Marc-Arthur Nougbode
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

class TextAreaHighlight extends JFrame
{
JTextArea texttt;
String[] dictionnaire;
    TextAreaHighlight()
    {
	
	
    }	
   
    // Creates highlights
    public void highlight(JTextArea textcomp, String[] dico, String[] token) {
      //notre textearea
    	texttt = textcomp;
    	//notre dico du fichier
    	dictionnaire = new String[dico.length];
    	for(int i = 0; i<dico.length;i++) {
    		dictionnaire[i] = dico[i];
    	}
    	  // First remove all old highlights
        removeHighlights(texttt);
    
        try {
            Highlighter hilite = texttt.getHighlighter();
            Document doc = texttt.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            // Cherche les mots pas dans le dico
            for(int i =0; i<token.length; i++) {
            	for(int j = 0; j<dico.length; j++) {
            		if(token[i].equals(dico[j])) {
            			i++;
            		}
         
            }
        			while ((pos = text.indexOf(token[i], pos)) >= 0) {
        			hilite.addHighlight(pos, pos+token[i].length(), myHighlightPainter);
        			pos += token[i].length();
        			}
        }
        }catch (BadLocationException e) {
        }
    }
    
    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
    
        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
    
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);
  //gére les choix de remplacement séparément 
  	private class Suggestion implements ActionListener {
  		
  		private JMenuItem suggestion;
  		private int start;
  		private int end;
  		
  		public Suggestion(JMenuItem suggestion, int start, int end) {
  			this.suggestion = suggestion;
  			this.start = start;
  			this.end = end;
  		}
  		
  		public void actionPerformed(ActionEvent event) {
  			suggestion(suggestion, start, end);
  		}
  	}
    
    public void sug() {//methode qui fait les suggestions

    texttt.addMouseListener( new MouseAdapter()
    {
        public void mouseClicked(MouseEvent e)
        {
            if ( SwingUtilities.isLeftMouseButton(e) )
                {
                    try
                        {
                            int offset = texttt.viewToModel( e.getPoint() );
                            System.out.println( texttt.modelToView( offset ) );
                            int start = Utilities.getWordStart(texttt, offset);
                            int end = Utilities.getWordEnd(texttt, offset);
                            String word = texttt.getDocument().getText(start, end-start);
                            System.out.println( "Selected word: " + word);
     
                            boolean trouver = false;
            				for(int i = 0; i < dictionnaire.length; i++) {
            					if(word.equalsIgnoreCase(dictionnaire[i])) {
            						trouver = true;
            					}
            				}
            				//Si le mot est inconnu, on affiche les cinq plus proches suggestions en distance.
            				if(trouver == false) {
            					int suggestions[][] = new int[dictionnaire.length][2];
            					for(int i = 0; i < dictionnaire.length; i++) {
            						suggestions[i][0] = i;
            						suggestions[i][1] = distance(word, dictionnaire[i]);
            					}
            					Arrays.sort(suggestions, (a, b) -> Integer.compare(a[1], b[1]));
            					JPopupMenu popup = new JPopupMenu();
            					for(int i = 0; i < 5; i++) {
            						JMenuItem suggestion = new JMenuItem("" + dictionnaire[suggestions[i][0]]);
            						suggestion.addActionListener(new Suggestion(suggestion, start, end));
            						popup.add(suggestion);
            					}
            					popup.show(texttt, e.getX(), e.getY());
            				}
            				 int rowStart = Utilities.getRowStart(texttt, offset);
                             int rowEnd = Utilities.getRowEnd(texttt, offset);
                             System.out.println( "Row start offset: " + rowStart );
                             System.out.println( "Row end   offset: " + rowEnd );
                             texttt.select(rowStart, rowEnd);
            			}
            			catch(BadLocationException e1) {
            				System.err.println("On ne peut pas lire le texte à l'index indiqué.");
            			}
            		}
                }                
    });

texttt.addCaretListener( new CaretListener(){
        public void caretUpdate(CaretEvent e)
        {
            int caretPosition = texttt.getCaretPosition();
            Element root = texttt.getDocument().getDefaultRootElement();
            int row = root.getElementIndex( caretPosition );
            int column = caretPosition - root.getElement( row ).getStartOffset();
            System.out.println( "Row   : " + ( row + 1 ) );
            System.out.println( "Column: " + ( column + 1 ) );
        }
    });
	

texttt.addKeyListener( new KeyAdapter()
    {
        public void keyPressed(KeyEvent e)
        {
            System.out.println( texttt.getDocument().getDefaultRootElement().getElementCount() );
        }
    });
    
   
    }
	public void suggestion(JMenuItem suggestion, int start, int end) {
		texttt.replaceRange(suggestion.getText(), start, end);
	}
	//Méthode obtenue de StudiUM de distance
		public static int distance(String s1, String s2) {
			
			int edits[][] = new int[s1.length()+1][s2.length()+1];
			for(int i = 0 ;i <= s1.length(); i++) {
				edits[i][0] = i;
			}
			for(int j = 1; j <= s2.length(); j++) {
				edits[0][j] = j;
			}
			for(int i = 1; i <= s1.length(); i++){
				for(int j = 1; j <= s2.length(); j++){
					int u = (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1);
					edits[i][j] = Math.min(edits[i - 1][j] + 1, Math.min(edits[i][j - 1] + 1,
					edits[i - 1][j - 1] + u));
				}
			}
			return edits[s1.length()][s2.length()];
			
		}
    
    // subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}