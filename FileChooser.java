//Megane Dandurand et Marc-Arthur Nougbode
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FileChooser extends JPanel
implements ActionListener {
static private final String newline = "\n";
JButton openButton;
JTextArea log;
JFileChooser fc;
Model textarea;
String[] array;

public FileChooser() {
super(new BorderLayout());

//Create the log first, because the action listeners
//need to refer to it.
log = new JTextArea(5,20);
log.setMargin(new Insets(5,5,5,5));
log.setEditable(false);
JScrollPane logScrollPane = new JScrollPane(log);

//Create a file chooser
fc = new JFileChooser();

fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

//Create the open button.  We use the image from the JLF
//Graphics Repository (but we extracted it from the jar).
openButton = new JButton("Ouvrir");
openButton.addActionListener(this);

//JE N'AI PAS FAIT DE SAVE BUTTON PUISQUE JE METS MON FICHIER ECRIT DIRECT DANS UN TEXTAREA
//For layout purposes, put the buttons in a separate panel
JPanel buttonPanel = new JPanel(); //use FlowLayout
buttonPanel.add(openButton);

//Add the buttons and the log to this panel.
add(buttonPanel, BorderLayout.PAGE_START);
add(logScrollPane, BorderLayout.CENTER);
}

public void actionPerformed(ActionEvent e) {

//Handle open button action.
if (e.getSource() == openButton) {
int returnVal = fc.showOpenDialog(FileChooser.this);

if (returnVal == JFileChooser.APPROVE_OPTION) {
openButton.setEnabled(false);
File file = fc.getSelectedFile();
textarea = new Model();
textarea.textAreaFile(file);
array = textarea.tokenizing();


log.append("Opening: " + file.getName() + "." + newline);
} else {
log.append("Open command cancelled by user." + newline);
}
log.setCaretPosition(log.getDocument().getLength());

} 
}
}