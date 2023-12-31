package org.gui;
// Destiny

import org.server.InternalServer;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Scanner;
import javax.swing.border.LineBorder;

enum PaintMode {Pixel, Area};

public class UI extends JFrame {
	private JTextField msgField;
//	private JTextArea chatArea;
	private ChatArea chatArea;
	private JPanel pnlColorPicker;
	private JPanel paintPanel;
	private JToggleButton tglPen;
	private JToggleButton tglBucket;
	private JButton undoButton;
	private JButton loadButton;
	private JButton saveButton;
	private JPanel sendTextArea;
	private JLabel sendTextPrompt;
	protected String username = " .. ";
	private static UI instance;
	private int selectedColor = -543230; 	//golden
	
	int[][] data = new int[50][50];			// pixel color data array
	int blockSize = 3;
	PaintMode paintMode = PaintMode.Pixel;
	
	/**
	 * get the instance of UI. Singleton design pattern.
	 * @return
	 */
	public static UI getInstance() {
		if (instance == null)
			instance = new UI();
		
		return instance;
	}
	
	/**
	 * private constructor. To create an instance of UI, call UI.getInstance() instead.
	 */
	private UI() {
		setTitle("KidPaint");
		
		JPanel basePanel = new JPanel();
		getContentPane().add(basePanel, BorderLayout.CENTER);
		basePanel.setLayout(new BorderLayout(0, 0));
		
		paintPanel = new JPanel() {
			
			// refresh the paint panel
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				Graphics2D g2 = (Graphics2D) g; // Graphics2D provides the setRenderingHints method
				
				// enable anti-aliasing
			    RenderingHints rh = new RenderingHints(
			             RenderingHints.KEY_ANTIALIASING,
			             RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setRenderingHints(rh);
			    
			    // clear the paint panel using black
				g2.setColor(Color.black);
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				// draw and fill circles with the specific colors stored in the data array
				for(int x=0; x<data.length; x++) {
					for (int y=0; y<data[0].length; y++) {
						g2.setColor(new Color(data[x][y]));
						g2.fillArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
						g2.setColor(Color.darkGray);
						g2.drawArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
					}
				}
			}
		};
		
		paintPanel.addMouseListener(new MouseListener() {

			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}

			// handle the mouse-up event of the paint panel
			@Override
			public void mouseReleased(MouseEvent e) {
				if (paintMode == PaintMode.Area && e.getX() >= 0 && e.getY() >= 0)
					paintArea(e.getX()/blockSize, e.getY()/blockSize);
			}
		});
		
		paintPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (paintMode == PaintMode.Pixel && e.getX() >= 0 && e.getY() >= 0)
					paintPixel(e.getX()/blockSize,e.getY()/blockSize);
			}

			@Override public void mouseMoved(MouseEvent e) {}
			
		});
		
		paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));
		
		JScrollPane scrollPaneLeft = new JScrollPane(paintPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		basePanel.add(scrollPaneLeft, BorderLayout.CENTER);
		
		JPanel toolPanel = new JPanel();
		basePanel.add(toolPanel, BorderLayout.NORTH);
		toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		
		pnlColorPicker = new JPanel();
		pnlColorPicker.setPreferredSize(new Dimension(24, 24));
		pnlColorPicker.setBackground(new Color(selectedColor));
		pnlColorPicker.setBorder(new LineBorder(new Color(0, 0, 0)));

		// show the color picker
		pnlColorPicker.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				ColorPicker picker = ColorPicker.getInstance(UI.instance);
				Point location = pnlColorPicker.getLocationOnScreen();
				location.y += pnlColorPicker.getHeight();
				picker.setLocation(location);
				picker.setVisible(true);
			}
			
		});
		
		toolPanel.add(pnlColorPicker);
		
		tglPen = new JToggleButton("Pen");
		tglPen.setSelected(true);
		toolPanel.add(tglPen);
		
		tglBucket = new JToggleButton("Bucket");
		toolPanel.add(tglBucket);

		undoButton = new JButton("Undo");
		toolPanel.add(undoButton);

		loadButton = new JButton("Load File");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(UI.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					loadDrawingFromFile(selectedFile);
					JOptionPane.showMessageDialog(UI.this,
							"Loading file: " + selectedFile.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(UI.this,
							"Load operation canceled");
				}
			}
		});
		toolPanel.add(loadButton);

		saveButton = new JButton("Save to file");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showSaveDialog(UI.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					saveDrawingToFile(selectedFile);
				} else {
					JOptionPane.showMessageDialog(UI.this,
							"Save command canceled");
				}
			}
		});

		toolPanel.add(saveButton);


		
		// change the paint mode to PIXEL mode
		tglPen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tglPen.setSelected(true);
				tglBucket.setSelected(false);
				paintMode = PaintMode.Pixel;
			}
		});
		
		// change the paint mode to AREA mode
		tglBucket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tglPen.setSelected(false);
				tglBucket.setSelected(true);
				paintMode = PaintMode.Area;
			}
		});

		undoButton.addActionListener((ActionEvent e) -> {
			//TODO
			//code for triggering undo
			System.out.println("Undo triggered");

		});
		
		JPanel msgPanel = new JPanel();
		
		getContentPane().add(msgPanel, BorderLayout.EAST);
		
		msgPanel.setLayout(new BorderLayout(0, 0));

		sendTextArea = new JPanel();
		sendTextArea.setLayout(new BorderLayout(0,0));
		sendTextPrompt = new JLabel(":::");
		sendTextArea.add(sendTextPrompt, BorderLayout.WEST);

		msgField = new JTextField();	// text field for inputting message
		sendTextArea.add(msgField, BorderLayout.CENTER);
		msgPanel.add(sendTextArea, BorderLayout.SOUTH);

		
		// handle key-input event of the message field
		msgField.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {		// if the user press ENTER
					onTextInputted(msgField.getText());
					msgField.setText("");
				}
			}
			
		});

		chatArea =  ChatArea.getInstance(this);
		
		JScrollPane scrollPaneRight = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneRight.setPreferredSize(new Dimension(300, this.getHeight()));
		msgPanel.add(scrollPaneRight, BorderLayout.CENTER);
		
		this.setSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * it will be invoked if the user selected the specific color through the color picker
	 * @param colorValue - the selected color
	 */
	public void selectColor(int colorValue) {
		SwingUtilities.invokeLater(()->{
			selectedColor = colorValue;
			pnlColorPicker.setBackground(new Color(colorValue));
		});
	}
		 
	/**
	 * it will be invoked if the user inputted text in the message field
	 * @param text - user inputted text
	 */
	private void onTextInputted(String text) {
		if (text.isEmpty()) return;

		SwingUtilities.invokeLater(() -> {
			//send message to server
			//TODO
			if (text.startsWith("me "))
				chatArea.addMessage(text, "Me");
			else
				chatArea.addMessage(text, "Person X");
		});
	}
	
	/**
	 * change the color of a specific pixel
	 * @param col, row - the position of the selected pixel
	 */
	public void paintPixel(int col, int row) {
		if (col >= data.length || row >= data[0].length) return;
		
		data[col][row] = selectedColor;
		paintPanel.repaint(col * blockSize, row * blockSize, blockSize, blockSize);
	}
	
	/**
	 * change the color of a specific area
	 * @param col, row - the position of the selected pixel
	 * @return a list of modified pixels
	 */
	public List paintArea(int col, int row) {
		LinkedList<Point> filledPixels = new LinkedList<Point>();

		if (col >= data.length || row >= data[0].length) return filledPixels;

		int oriColor = data[col][row];
		LinkedList<Point> buffer = new LinkedList<Point>();
		
		if (oriColor != selectedColor) {
			buffer.add(new Point(col, row));
			
			while(!buffer.isEmpty()) {
				Point p = buffer.removeFirst();
				int x = p.x;
				int y = p.y;
				
				if (data[x][y] != oriColor) continue;
				
				data[x][y] = selectedColor;
				filledPixels.add(p);
	
				if (x > 0 && data[x-1][y] == oriColor) buffer.add(new Point(x-1, y));
				if (x < data.length - 1 && data[x+1][y] == oriColor) buffer.add(new Point(x+1, y));
				if (y > 0 && data[x][y-1] == oriColor) buffer.add(new Point(x, y-1));
				if (y < data[0].length - 1 && data[x][y+1] == oriColor) buffer.add(new Point(x, y+1));
			}
			paintPanel.repaint();
		}
		return filledPixels;
	}
	
	/**
	 * set pixel data and block size
	 * @param data
	 * @param blockSize
	 */
	public void setData(int[][] data, int blockSize)  {
		this.data = data;
		this.blockSize = blockSize;
		paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));
		paintPanel.repaint();
	}

	public void loadusername(String username) {
		this.username = username;
		sendTextPrompt.setText("Send message as \"" + username + "\":");
	}

	private void loadDrawingFromFile(File file){
		try (Scanner in = new Scanner(file)) {
			int rows = Integer.valueOf(in.next());
			int cols = Integer.valueOf(in.next());
			int[][] newData = new int[rows][cols];
			int newBlockSize = Integer.valueOf(in.next());
			in.nextLine();


			for (int i = 0; in.hasNextLine();i++){
				String[] row = in.nextLine().split(" ");
				for (int j = 0; j < row.length; j++)
					newData[j][i] = Integer.valueOf(row[j]);

			}

			sendLoadDataToServer(newData, newBlockSize);
			paintPanel.repaint();
        } catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(UI.this,
					"File not found!");
        }
    }

	private void saveDrawingToFile( File selectedFile){
		System.out.println(data[0][0]);
		System.out.println(data.length);
		System.out.println(data[0].length);

		for (int col = 0; col < data.length; col++){
			for (int row = 0; row < data[col].length; row++){
				System.out.print(data[col][row]);
				System.out.printf(" ");
			}
			System.out.println();
		}

		try (PrintWriter writer = new PrintWriter(selectedFile)) {
			writer.write(String.valueOf(data.length));
			writer.write(" ");
			writer.write(String.valueOf(data[0].length));
			writer.write(" ");
			writer.write(String.valueOf(blockSize));
			writer.println();

			for (int col = 0; col < data.length; col++){
				for (int row = 0; row < data[col].length; row++){
					writer.write(String.valueOf(data[col][row]));
					writer.write(" ");
				}
				writer.println();
			}


			writer.flush();

			JOptionPane.showMessageDialog(UI.this,
					"File saved successfully!");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(UI.this,
					"Error saving file: " + ex.getMessage());
		}


	}


	private void sendLoadDataToServer(int[][] newData, int newBlockSize){
		if (InternalServer.isRunning()){
			data = newData;
			blockSize = newBlockSize;
			paintPanel.repaint();
			//send this update to all your connections
		} else {
			//send the update to the master server
		}

	}
}
