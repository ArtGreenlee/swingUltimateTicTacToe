import javax.swing.*;
import java.awt.*;

public class UltimateTicTacToe extends JFrame {

  public static void main(String[] args) {
    UltimateTicTacToe gameFrame = new UltimateTicTacToe();
  }

  public UltimateTicTacToe() {
    super("Ultimate Tic Tac Toe!");
    JFrame.setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    iconX = new ImageIcon(getClass().getResource("/images/smallX.jpg"));
    iconO = new ImageIcon(getClass().getResource("/images/smallO.jpg"));
    largeX = new ImageIcon(getClass().getResource("/images/X.jpg"));
    largeO = new ImageIcon(getClass().getResource("/images/O.jpg"));

    GridLayout layout = new GridLayout(3 , 3);
    layout.setHgap(10);
    layout.setVgap(10);
    gamePanel = new JPanel();
    gamePanel.setLayout(layout);
    updateBoard();
    add(gamePanel);
    gamePanel.setBackground(Color.BLACK);
    pack();
    setSize(600,600);
    setVisible(true);
  }

  private final void updateBoard() {
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        bigSpaces[x][y] = new JPanel();
        bigSpaces[x][y].setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
          buttons[x][y][i] = new JButton(" ");
          int finalX = x;int finalY = y;int finalI = i;
          buttons[x][y][i].addActionListener(event -> buttonPressed(finalX, finalY, finalI));
          bigSpaces[x][y].add(buttons[x][y][i]);
        }
        gamePanel.add(bigSpaces[x][y]);
      }
    }
  }

  private void buttonPressed(Integer x, Integer y, Integer i) {
    System.out.println(i + " " + x + " " + y);
    buttons[x][y][i].setText("");
    buttons[x][y][i].setEnabled(false);
    if (currentPlayer == 'X') {
      smallSquareOwners[x][y][i] = 'X';
      checkSmallSquare(x, y);
      buttons[x][y][i].setIcon(iconX);
      currentPlayer = 'O';
    }
    else {
      smallSquareOwners[x][y][i] = 'O';
      checkSmallSquare(x, y);
      buttons[x][y][i].setIcon(iconO);
      currentPlayer = 'X';
    }
    buttons[x][y][i].setDisabledIcon(buttons[x][y][i].getIcon());
    checkSmallSquare(x, y);
    disableAll(x,y,i);
    repaint();
  }

  private void disableAll(Integer xIn, Integer yIn, Integer spot) {
    Integer enableX;
    Integer enableY;
    if (spot == 0) {
      enableX = 0;
      enableY = 0;
    }
    else if (spot == 1) {
      enableX = 1;
      enableY = 0;
    }
    else if (spot == 2) {
      enableX = 2;
      enableY = 0;
    }
    else if (spot == 3) {
      enableX = 0;
      enableY = 1;
    }
    else if (spot == 4) {
      enableX = 1;
      enableY = 1;
    }
    else if (spot == 5) {
      enableX = 2;
      enableY = 1;
    }
    else if (spot == 6) {
      enableX = 0;
      enableY = 2;
    }
    else if (spot == 7) {
      enableX = 1;
      enableY = 2;
    }
    else {
      enableX = 2;
      enableY = 2;
    }
    if (bigSquareOwners[enableX][enableY] != null) {
      for (int y = 0; y < 3; y++) {
        for (int x = 0; x < 3; x++) {
          for (int i = 0; i < 9; i++) {
            if (smallSquareOwners[x][y][i] == null) {
              buttons[x][y][i].setEnabled(true);
            }
          }
        }
      }
    }
    else {
      for (int y = 0; y < 3; y++) {
        for (int x = 0; x < 3; x++) {
          for (int i = 0; i < 9; i++) {
            buttons[x][y][i].setEnabled(false);
          }
        }
      }
      System.out.println(enableX + " " + enableY);
      for (int i = 0; i < 9; i++) {
        if (smallSquareOwners[enableX][enableY][i] == null) {
          buttons[enableX][enableY][i].setEnabled(true);
        }
      }
    }
    repaint();
  }

  private void checkSmallSquare(Integer x, Integer y) {
    //check Vertical
    for (int i = 0; i < 3; i++) {
      if (smallSquareOwners[x][y][i] == currentPlayer &&
          smallSquareOwners[x][y][i + 3] == currentPlayer &&
          smallSquareOwners[x][y][i + 6] == currentPlayer) {
        setBigSquareOwner(x, y, currentPlayer);
      }
    }
    //check Horizantal
    for (int i = 0; i <= 6; i += 3) {
      if (smallSquareOwners[x][y][i] == currentPlayer &&
          smallSquareOwners[x][y][i + 1] == currentPlayer &&
          smallSquareOwners[x][y][i + 2] == currentPlayer) {
        setBigSquareOwner(x, y, currentPlayer);
      }
    }
    //check diagonal
    if (smallSquareOwners[x][y][0] == currentPlayer &&
        smallSquareOwners[x][y][4] == currentPlayer &&
        smallSquareOwners[x][y][8] == currentPlayer) {
      setBigSquareOwner(x, y, currentPlayer);
    }
    else if (smallSquareOwners[x][y][2] == currentPlayer &&
        smallSquareOwners[x][y][4] == currentPlayer &&
        smallSquareOwners[x][y][6] == currentPlayer) {
      setBigSquareOwner(x, y, currentPlayer);
    }
    //check for small stalemate
    Boolean AllTaken = true;
    for (int i = 0; i < 9; i++) {
      if (smallSquareOwners[x][y][i] != null) {
        AllTaken = false;
      }
    }
    if (AllTaken) {
      setBigSquareOwner(x,y, 'N');
    }
  }

  private void setBigSquareOwner(Integer x, Integer y, Character in) {
    bigSquareOwners[x][y] = in;
    if (in != 'N') {
      bigSpaces[x][y].removeAll();
      bigSpaces[x][y].setLayout(new FlowLayout());
      if (in == 'X') {
        bigSpaces[x][y].add(new JLabel(largeX));
      }
      else {
        bigSpaces[x][y].add(new JLabel(largeO));
      }
      repaint();
    }
    checkForVictory();
  }

  private void checkForVictory() {
    for (int i = 0; i < 3; i++) {
      if (bigSquareOwners[i][0] == currentPlayer &&
          bigSquareOwners[i][1] == currentPlayer &&
          bigSquareOwners[i][2] == currentPlayer) {
        victorDialog();
      }
      else if (bigSquareOwners[0][i] == currentPlayer &&
          bigSquareOwners[1][i] == currentPlayer &&
          bigSquareOwners[2][i] == currentPlayer) {
        victorDialog();
      }
    }
    if (bigSquareOwners[0][0] == currentPlayer &&
        bigSquareOwners[1][1] == currentPlayer &&
        bigSquareOwners[2][2] == currentPlayer) {
      victorDialog();
    }
    else if (bigSquareOwners[0][2] == currentPlayer &&
        bigSquareOwners[1][1] == currentPlayer &&
        bigSquareOwners[2][0] == currentPlayer) {
      victorDialog();
    }
    boolean noneTaken = true;
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        if (bigSquareOwners[x][y] != null) {
          noneTaken = false;
        }
      }
    }
    if (noneTaken) {
      JOptionPane.showMessageDialog(this,
          "Stalemate",
          "StaleMate",
          JOptionPane.ERROR_MESSAGE);
      dispose();
    }
  }

  private void victorDialog() {
    JOptionPane.showMessageDialog(this,
        String.valueOf(currentPlayer) + " WINS!",
        "VICTORY",
        JOptionPane.INFORMATION_MESSAGE);
    dispose();
  }

  JPanel gamePanel;
  private ImageIcon iconX;
  private ImageIcon iconO;
  private ImageIcon largeX;
  private ImageIcon largeO;
  private Character currentPlayer = 'X';
  private JPanel bigSpaces[][] = new JPanel[3][3];
  private JButton[][][] buttons = new JButton[3][3][9];
  private Character[][][] smallSquareOwners = new Character[3][3][9];
  private Character[][] bigSquareOwners = new Character[3][3];
}
