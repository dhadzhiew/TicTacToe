import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class TicTacToe implements WindowListener, ActionListener, MouseListener {

	private JFrame window;
	private JButton exitBtn, resetBtn, changeMode;
	private int actions = 0;
	private JPanel panel;
	private int resultX = 0;
	private JLabel score;
	private int resultO = 0;
	private JLabel score1;
	public List<Integer> userX, userO;
	private boolean gameOver = false;
	private ImageIcon imageX, imageO;
	private Map<Integer, Integer> btns;
	private List<JButton> allButtons;
	private boolean PVC = true;

	public TicTacToe() {
		window = new JFrame();
		window.setTitle("Tic Tac Toe");
		window.setSize(530, 410);
		window.getContentPane().setBackground(Color.decode("#999999"));
		window.setVisible(true);
		window.addWindowListener(this);
		window.setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width / 2 - window.getSize().width / 2,
				dim.height / 2 - window.getSize().height / 2);

		score = new JLabel();
		score.setBounds(380, 200, 200, 20);
		window.add(score);

		score1 = new JLabel();
		score1.setBounds(380, 180, 200, 20);
		window.add(score1);
		setScore();

		resetBtn = new JButton("Reset");
		resetBtn.setBounds(380, 20, 120, 30);
		window.add(resetBtn);
		resetBtn.addActionListener(this);

		exitBtn = new JButton("Exit");
		exitBtn.setBounds(380, 60, 120, 30);
		window.add(exitBtn);
		exitBtn.addActionListener(this);

		changeMode = new JButton("Change mode");
		changeMode.setBounds(380, 100, 120, 30);
		window.add(changeMode);
		changeMode.addActionListener(this);

		panel = new JPanel();
		addBlocks();
		panel.setBackground(Color.decode("#cccccc"));
		panel.setBounds(10, 10, 350, 350);
		window.add(panel);

		userX = new ArrayList<Integer>();
		userO = new ArrayList<Integer>();

		imageX = new ImageIcon(getClass().getResource("imgs/x.png"));
		imageO = new ImageIcon(getClass().getResource("imgs/o.png"));
		Object[] possibilities = { "Player vs Player", "Player vs Computer" };
		String vs = (String) JOptionPane.showInputDialog(window, "",
				"Choose game mode", JOptionPane.QUESTION_MESSAGE, null,
				possibilities, "Player vs Computer");

		if (vs == null) {
			System.exit(0);
		} else if (vs.equals("Player vs Player")) {
			PVC = false;
		}
		window.repaint();

	}

	public static void main(String[] args) {
		new TicTacToe();
	}

	private void addBlocks() {
		int x = 20, y = 20;
		btns = new HashMap<Integer, Integer>();
		allButtons = new ArrayList<JButton>();
		for (int i = 1; i <= 9; i++) {
			JButton btn = new JButton();
			btn.setBounds(x, y, 100, 100);
			Border border = new LineBorder(Color.black, 1);
			btn.setBorder(border);
			btn.addMouseListener(this);
			btn.setName(String.valueOf(i));
			btn.addActionListener(this);
			btn.setIcon(new ImageIcon("imgs/transparent.png"));
			btn.setPressedIcon(new ImageIcon("imgs/semi-transparent.png"));
			btn.setBackground(Color.decode("#ffffff"));
			btn.setOpaque(false);
			allButtons.add(btn);
			panel.setLayout(null);
			panel.add(btn);
			if (i % 3 == 0) {
				y += 100;
				x -= 200;
			} else {
				x += 100;
			}
			btns.put(i, 0);
		}

		window.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == exitBtn) {
			System.exit(0);
		} else if (ae.getSource() == resetBtn) {
			reset();
		} else if (ae.getSource() == changeMode) {
			reset();
			resultO = 0;
			resultX = 0;
			setScore();
			if (PVC == true) {
				PVC = false;
				JOptionPane pane = new JOptionPane("Player vs Player");
				JDialog dialog = pane.createDialog("Player vs Player");
				dialog.setVisible(true);
			} else {
				PVC = true;
				JOptionPane pane = new JOptionPane("Player vs Computer");
				JDialog dialog = pane.createDialog("Player vs Computer");
				dialog.setVisible(true);
			}
		} else {
			JButton btn = (JButton) ae.getSource();
			if (btns.get(Integer.parseInt(btn.getName())) == 0) {
				btns.replace(Integer.parseInt(btn.getName()), 1);
				actions++;
				if (actions % 2 == 0) {
					btn.setIcon(imageO);
					userO.add(Integer.parseInt(btn.getName()));
				} else {
					btn.setIcon(imageX);
					userX.add(Integer.parseInt(btn.getName()));
					if (actions < 9 && !isWinner(userX) && PVC == true) {
						int nextPos = nextPossiblePosition(userX, userO);
						JButton nextBtn = allButtons.get(nextPos - 1);
						nextBtn.setIcon(imageO);
						userO.add(nextPos);
						btns.replace(nextPos, 1);
						actions++;
					}

				}
				btn.setFont(new Font("Serif", Font.BOLD, 60));
				if (isWinner(userX)) {
					gameOver = true;
					JOptionPane pane = new JOptionPane("X wins.");
					JDialog dialog = pane.createDialog("Game over.");
					dialog.setVisible(true);
					resultX++;

					reset();
				} else if (isWinner(userO)) {
					gameOver = true;
					JOptionPane pane = new JOptionPane("O wins.");
					JDialog dialog = pane.createDialog("Game over.");
					dialog.setVisible(true);
					resultO++;

					reset();
				}
				if (actions == 9 && !gameOver) {
					JOptionPane pane = new JOptionPane("Its a tie.");
					JDialog dialog = pane.createDialog("Game over");
					dialog.setVisible(true);
					reset();
				}
			}

		}

	}

	public boolean isWinner(List<Integer> p) {
		if (p.contains(1) && p.contains(2) && p.contains(3) || p.contains(4)
				&& p.contains(5) && p.contains(6) || p.contains(7)
				&& p.contains(8) && p.contains(9) || p.contains(1)
				&& p.contains(5) && p.contains(9) || p.contains(3)
				&& p.contains(5) && p.contains(7) || p.contains(1)
				&& p.contains(4) && p.contains(7) || p.contains(2)
				&& p.contains(5) && p.contains(8) || p.contains(3)
				&& p.contains(6) && p.contains(9)) {
			return true;
		}
		return false;
	}

	public int nextPossiblePosition(List<Integer> positionsX,
			List<Integer> positionsO) {
		for (int i = 1; i <= 9; i++) {
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(positionsO);
			if (btns.get(i) == 0) {
				temp.add(i);
			}

			if (isWinner(temp)) {
				return i;
			}
		}
		for (int i = 1; i <= 9; i++) {
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(positionsX);
			if (btns.get(i) == 0) {
				temp.add(i);
			}

			if (isWinner(temp)) {
				return i;
			}
		}

		List<Integer> possiblePositions = new ArrayList<Integer>();
		for (int j = 1; j <= 9; j++) {
			if (btns.get(j) == 0) {
				possiblePositions.add(j);
			}
		}
		Random rand = new Random();
		int randPosition = rand.nextInt(possiblePositions.size());
		return possiblePositions.get(randPosition);

	}

	public void reset() {
		userX.clear();
		userO.clear();
		panel.removeAll();
		addBlocks();
		window.repaint();
		actions = 0;
		gameOver = false;
		window.toFront();
		window.setState(Frame.NORMAL);
		setScore();
	}

	public void setScore() {
		score1.setText("Score O: " + resultO);
		score.setText("Score X: " + resultX);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
