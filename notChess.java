// import only what we need
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
*	notChess - Problem Set 3
*   CSIS-225 Spring 2023
*	@author Ira Goldstein (Solution)
*/

public class notChess implements Runnable, ActionListener {
    /**
     * The run method to set up the graphical user interface
     */	

	 private JLabel moveLabel;
	 private JTextField inputBox;
	 private JButton moveButton;
	 private String buttonMove;
	 private String textMove;
	 private String piece;
	 private String column1;
	 private String column2;
	 private int row1;
	 private int row2;
	 private Boolean isValid;
	 private Boolean firstPress = true;
  	 private myButton[][] squareButton = new myButton[9][9];
	 private myButton aButton = new myButton(0,0);
	 private final String COLUMN[] = {" ","A","B","C","D","E","F","G","H",};
	 private final String COLSTRING = "ABCDEFGH";

	 
	@Override
	public void run() {
		
        // JFrame setup 
		JFrame frame = new JFrame("Not Chess");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// outer panel
		JPanel panel = new JPanel(new BorderLayout());
		frame.add(panel);

		// top panel for moves
		JPanel movePanel = new JPanel(new FlowLayout());
		panel.add(movePanel, BorderLayout.NORTH);
		
		moveLabel = new JLabel("Move:");
		movePanel.add(moveLabel);

		Font currFont = moveLabel.getFont();
		Font newFont = new Font(currFont.getFontName(), currFont.getStyle(), 12);
		moveLabel.setFont(newFont);
		
		// centering the move label and placing it in the middle of the panel
		moveLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.add(moveLabel, BorderLayout.NORTH);

		// panel for the grid of buttons, including the row/column headings
		JPanel gridPanel = new JPanel(new GridLayout(9, 9));
		JLabel[] colLabel = new JLabel[9];
		JLabel[] rowLabel = new JLabel[9];
		
		// display the column labels
		for (int col=0; col < 9; col++){
			colLabel[col] = new JLabel(COLUMN[col], JLabel.CENTER);
			gridPanel.add(colLabel[col]);
		}
		
		// itterate over the rows
		for (int row=8; row>0; row--){
			gridPanel.add(new JLabel(" "));

			// add the row label
			rowLabel[row] = new JLabel(Integer.toString(row), JLabel.CENTER);
			gridPanel.add(rowLabel[row]);
			
			//itterate over the columns and add the buttons
			for (int col=1; col < 9; col++){
				squareButton[row][col] = new myButton(row, col);
				squareButton[row][col].addActionListener(this);
				gridPanel.add(squareButton[row][col]);
			}
		}
		
		//  Put the four pieces in their initial positions
		squareButton[5][4].setText("O");
		squareButton[5][5].setText("O");
		squareButton[4][4].setText("X");
		squareButton[4][5].setText("X");

		// Add the buttons and their labels to the panel
		panel.add(gridPanel, BorderLayout.CENTER);
		
			
		// new panel for typing in a move.
		JPanel textPanel = new JPanel();
		frame.add(textPanel, BorderLayout.PAGE_END);

		// label for text box to type in a move
		JLabel enterLabel = new JLabel("Enter Move:");
		textPanel.add(enterLabel);

		// text box for typing in a move
		inputBox = new JTextField(5);
		textPanel.add(inputBox);

		// button to accept the typed move
		moveButton = new JButton("Move");
		moveButton.addActionListener(this);
		textPanel.add(moveButton);

		//display everything
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		// Check to see if what they typed is valid
		if (e.getSource() == moveButton) {
			textMove = inputBox.getText().toUpperCase();

			if ( checkAndMove(inputBox.getText().toUpperCase()) ){
				moveLabel.setText("Move: " + textMove.toUpperCase());	
			}
			else {
				moveLabel.setText("Move: INVALID MOVE!");	
			}

		}
		// otherwise, look at the mouse clicks
		else {
			if (firstPress) {
				firstPress = false;
				
				// cast the getSource object as myButton so we can then just 
				// retrieve the row and column
				myButton aButton =  (myButton)e.getSource();	
				
				// check to see if the first click has a piece
				if (aButton.getText().equals("X") || aButton.getText().equals("O") ){				
					// start building the move as if it were typed
					buttonMove = aButton.getText() + COLUMN[aButton.getCol()]
								+ aButton.getRow();
				}
				else {
					// button did not have a piece
					firstPress = true;
					moveLabel.setText("Move: INVALID MOVE!");
				}
			}
			else {
				// second click
				// as above, cast the getSource object as myButton so we can then
				// just retrieve the row and column
				myButton aButton =  (myButton)e.getSource();	
				
				// add this press to the string
				buttonMove = buttonMove + aButton.getText() + COLUMN[aButton.getCol()]
							+ aButton.getRow();
	
				if ( checkAndMove(buttonMove) ){
					moveLabel.setText("Move: " + buttonMove);	
				}
				else {
					moveLabel.setText("Move: INVALID MOVE!");	
				}
			}
			
		}
	}

    /**
       Returns whether the move is valid.
	   If it is valid,  moves the piece
	   @param the text string with the move

       @return whether the move is valid.
    */	
	public Boolean checkAndMove(String textMove) {
		// assume that the move is valid
		isValid = true;
		
		// reset as if nothing has been pressed
		firstPress = true;
		
		// make sure that we have five characters
		if (textMove.length() !=  5) {
			isValid = false;
			return isValid;
		}
		
		// break out each of the five characters
		piece = textMove.substring(0,1);
		column1 = textMove.substring(1,2);
		
		// make sure to catch non-numerics
		try {
			row1 = Integer.parseInt(textMove.substring(2,3));
		}
		catch (NumberFormatException e) {
			isValid = false;
			return isValid;
		}	
		column2 = textMove.substring(3,4);
		
		// make sure to catch non-numerics
		try {
			row2 = Integer.parseInt(textMove.substring(4,5));
		}
		catch (NumberFormatException e) {
			isValid = false;
			return isValid;
		}
		
		// First, check to see if the string is formatted correctly
		// do we have a valid piece
		if (!piece.equals("X") && !piece.equals("O") ) {
			isValid = false;
			return isValid;
		}
		
		// do we have a valid first column
		if (!COLSTRING.contains(column1)) {
			isValid = false;
			return isValid;
		}	
		
		// do we have a valid first row
		if (row1 < 1 || row1 > 8) {
			isValid = false;
			return isValid;
		}	
		
		// do we have a valid second column
		if (!COLSTRING.contains(column2)) {
			isValid = false;
			return isValid;
		}
		
		// do we have a valid second row
		if (row2 < 1 || row2 > 8) {
			isValid = false;
			return isValid;
		}
		
		// if we get this far we know that the string is formatted correctly
		// now we need to see if we can make this move
		
		// Does the initial space have the expected piece?
		if (!squareButton[row1][COLSTRING.indexOf(column1) + 1].getText().equals(piece)) {
			isValid = false;
			return isValid;
		}
		// Is the destination space empty?
		if (!squareButton[row2][COLSTRING.indexOf(column2) + 1].getText().equals("")) {
			isValid = false;
			return isValid;
		}		
		
		
		// Are the two positions adjacent to one another?
		// differnce between the rows and columns need to be either 0 or 1
		// if so, move the piece
		if (Math.abs(row1 - row2) < 2  && Math.abs(COLSTRING.indexOf(column1) 
				- COLSTRING.indexOf(column2)) < 2) {
			squareButton[row1][COLSTRING.indexOf(column1) + 1].setText("");
			squareButton[row2][COLSTRING.indexOf(column2) + 1].setText(piece);
		}
		else {
			isValid = false;
			return isValid;
		}		
			
			
		return isValid;
	}
	
	public static void main(String args[]) {

		javax.swing.SwingUtilities.invokeLater(new notChess());
	}	
}

 // extending JButton to include the row and column
 // adding adding these methods so we do not need to itterate 
 // over all of the buttons
 class myButton extends JButton {
	protected int row;
	protected int col;
	
	public myButton(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
}