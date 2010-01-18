package cn.hexen.software.cuelrcmaster;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.AWTException;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.UnmappableCharacterException;
import java.util.Iterator;
import java.util.List;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.SwingConstants;


import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;

import java.awt.Insets;
import java.awt.SystemColor;

//TODO:日文汉字转换

public class CopyOfMasterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel jContentPane = null;	
	private Dimension screensize;	
	private JTextArea jTextArea = null;
	private JScrollPane sPane = null;
	private JComboBox typeBox = null;
	private JButton jButton_convert = null;
	private JLabel jLabel_cuetype = null;
	private JButton jButton_open = null;
	private JCheckBox jCheckBox_savehere = null;
	private JPanel jPanel_save = null;
	private JLabel jLabel_save = null;
	private JTextField jTextField_saveAsPath = null;
	private JButton jButton_saveas = null;
	private JPanel jPanel_saveas = null;
	private TrayIcon trayIcon = null;  //  @jve:decl-index=0:
	private SystemTray tray=null;  //  @jve:decl-index=0:
	private JPanel jPanel_lrcmode = null;
	private ButtonGroup buttonGroup_mode = null;  //  @jve:decl-index=0:
	private JRadioButton jRadioButton_kanjitokana = null;
	private JRadioButton jRadioButton_normal = null;
	private JCheckBox jCheckBox_autosave = null;
	private JLabel jLabel_author = null;
	
	////
	private CueFile oriCue;  //  @jve:decl-index=0:
	private LrcFile oriLrc;
    private OPFile opFile;
	private final String[] musictype = { "ape", "tta", "tak", "wav", "flac" };
	private final static int TRAYABLE=1;
	private Font f=new Font("宋体",Font.PLAIN,12);  //  @jve:decl-index=0:	
	private JButton jButton_closeFile = null;
	
	//进阶开发内容	
	//private int convertMode = 0;
	//private boolean autosave = true;
	
	
	/**
	 * This is the default constructor
	 */
	public CopyOfMasterFrame() {
		super();
		initialize();
	
		addWindowListener(new WindowAdapter(){			
			
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}			
			public void windowIconified(WindowEvent e){				
				if(TRAYABLE==1){
					setVisible(false);
					try {
						tray.add(trayIcon);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			}			
			public void windowDeiconified(WindowEvent e){
				if(TRAYABLE==1){
					tray.remove(trayIcon);
				}
			}
		});
	}



	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {		
		
 
		UIManager.put("Label.font",f);   
		UIManager.put("ComboBox.font",f);   
		UIManager.put("Button.font",f); 
		UIManager.put("MenuItem.font",f); 
		UIManager.put("Menu.font",f);
		UIManager.put("ToolTip.font",f);
		

		initializeTrayIcon();	
				
		this.setSize(649, 436);
		this.setResizable(false);
		Toolkit toolkit = getToolkit();
		screensize = toolkit.getScreenSize();
		this.setLocation(screensize.width / 2 - getWidth() / 2,
				screensize.height / 2 - getHeight() / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("CueLrcMaster ( CLMaster )");
		this.setVisible(true);
		
		new DropTarget(jTextArea, new TextDropTargetListener(jTextArea));
		
	}

	private void initializeTrayIcon(){
		if (SystemTray.isSupported()) {
			// get the SystemTray instance			
			tray= SystemTray.getSystemTray();			
			// load an image
			Image image = Toolkit.getDefaultToolkit().getImage("CueMaster.png");
			
			// create a popup menu
			PopupMenu popup = new PopupMenu();
			popup.setFont(f);
			// create menu item for the default action
			String itemName[]={"打开主程序","-","退出"};
			final MenuItem item[]=new MenuItem[3];
			
			// create a action listener to listen for default action executed on the tray icon
			ActionListener listener = new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(e.getSource()==item[0]){
						trayIconHide();
					}
					if(e.getSource()==item[2]){
						System.exit(0);
					}
				}
			};
			
			for(int i=0;i<itemName.length;i++){
				item[i]=new MenuItem(itemName[i]);
				item[i].addActionListener(listener);
				popup.add(item[i]);
			}

			// construct a TrayIcon
			trayIcon = new TrayIcon(image, "CueMaster", popup);
			// set the TrayIcon properties
			trayIcon.addActionListener(listener);
			trayIcon.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getButton()==MouseEvent.BUTTON1)
						trayIconHide();
				}
			});			
			
		}else {
			// disable tray option in your application or
			// perform other actions
		}	
		
	}


	

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel_author = new JLabel();
			jLabel_author.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jLabel_author.setFont(new Font("HGSoeiKakupoptai", Font.BOLD, 12));
			jLabel_author.setForeground(new Color(125, 7, 125));
			jLabel_author.setSize(new Dimension(74, 18));
			jLabel_author.setLocation(new Point(564, 1));
			jLabel_author.setText("By Hexen");
			jLabel_save = new JLabel();
			jLabel_save.setText(" 保存在别的目录:");
			jLabel_save.setName("jLabel_save");
			jLabel_save.setHorizontalTextPosition(SwingConstants.TRAILING);
			jLabel_save.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jLabel_save.setVerticalTextPosition(SwingConstants.BOTTOM);
			jLabel_save.setVerticalAlignment(SwingConstants.BOTTOM);
			jLabel_save.setEnabled(false);
			jLabel_save.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jLabel_cuetype = new JLabel();
			jLabel_cuetype.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jLabel_cuetype.setSize(new Dimension(133, 21));
			jLabel_cuetype.setLocation(new Point(367, 17));
			jLabel_cuetype.setEnabled(false);
			jLabel_cuetype.setText("Cue目标音频文件类型:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jContentPane.setFont(new Font("Dialog", Font.PLAIN, 12));
			jContentPane.add(getSPane(), null);
			jContentPane.add(getTypeBox(), null);
			jContentPane.add(getJButton_convert(), null);
			jContentPane.add(jLabel_cuetype, null);
			jContentPane.add(getJButton_open(), null);
			jContentPane.add(getJPanel_save(), null);
			buttonGroup_mode=new ButtonGroup();
			jContentPane.add(getJPanel_lrcmode(), null);
			jContentPane.add(getJCheckBox_autosave(), null);
			jContentPane.add(jLabel_author, null);
			jContentPane.add(getJButton_closeFile(), null);
		}
		return jContentPane;
	}

	class TextDropTargetListener implements DropTargetListener {

		private JTextArea textArea;

		TextDropTargetListener(JTextArea ta) {
			textArea = ta;
		}

		public void dragEnter(DropTargetDragEvent dtde) {
			if (!isDragAcceptable(dtde)) {
				dtde.rejectDrag();
				return;
			}
		}

		public void dragExit(DropTargetEvent dte) {
		}

		public void dragOver(DropTargetDragEvent dtde) {
			// you can provide visual feedback here
		}

		@SuppressWarnings("unchecked")
		public void drop(DropTargetDropEvent dtde) {
			if (!isDropAcceptable(dtde)) {
				dtde.rejectDrop();
				return;
			}

			// 进行文件匹配

			// 默认的COPY DROP
			dtde.acceptDrop(DnDConstants.ACTION_COPY);

			Transferable transferable = dtde.getTransferable();

			DataFlavor[] flavors = transferable.getTransferDataFlavors();

			if (flavors.length > 1) {
				JOptionPane.showMessageDialog(null, "请一次拖动一个文件！");
				return;
			}

			for (int i = 0; i < flavors.length; i++) {
				DataFlavor d = flavors[i];

				// TestOpcode:输出MIME类型
				// textArea.append("MIME type="+d.getMimeType()+"\n");

				try {
					if (d.equals(DataFlavor.javaFileListFlavor)) {
						List fileList = (List) transferable.getTransferData(d);

						// Iterator在多文件List中使用，这里可以不用，以后可以考虑把TextDropTargetListener类单独重写
						Iterator iterator = fileList.iterator();
						while (iterator.hasNext()) {
							File f = (File) iterator.next();
							if (f.toString().matches(".*\\.cue") || f.toString().matches(".*\\.lrc")){
								if(opFile!=null){
									if(closeFile()==1)
										return;
								}
								loadFile(f.toString());
							}else {
								JOptionPane.showMessageDialog(null, "不是cue文件或lrc文件");
								return;
							}
						}
					}
				} catch (Exception e) {
					textArea.append("Error:" + e + "\n");
				}
			}
			dtde.dropComplete(true);
		}
		
		

		public void dropActionChanged(DropTargetDragEvent dtde) {
			if (!isDragAcceptable(dtde)) {
				dtde.rejectDrag();
				return;
			}
		}

		
		// TODO 在这里进行文件DRAP和DROP可否的判断
		public boolean isDragAcceptable(DropTargetDragEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}

		public boolean isDropAcceptable(DropTargetDropEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}

	}

	
	
	private void loadFile(String fileURL) {
		if(fileURL.toString().matches(".*\\.cue")){
			try {
				oriCue = new CueFile(fileURL);
				opFile=oriCue;
				jTextArea.setText(oriCue.toString());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "文件未找到");
				return;
			}
			typeBox.setSelectedItem(oriCue.getoriFileType());
			typeBox.setEnabled(true);
			jLabel_cuetype.setEnabled(true);
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jRadioButton_kanjitokana.setEnabled(false);
			jRadioButton_normal.setEnabled(false);
			jButton_convert.setEnabled(true);
			
		}else if(fileURL.toString().matches(".*\\.lrc")){
			try {
				oriLrc = new LrcFile(fileURL);
				opFile=oriLrc;
				jTextArea.setText(oriLrc.toString());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "文件未找到");
				return;
			}
			typeBox.setEnabled(false);
			jLabel_cuetype.setEnabled(false);
			jPanel_lrcmode.setEnabled(true);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
			jRadioButton_kanjitokana.setEnabled(true);
			jRadioButton_normal.setEnabled(true);
			jButton_convert.setEnabled(true);
		}
		jButton_closeFile.setEnabled(true);
	}

	
	
	private void convert() {
		String newFilename=null;
		if(jCheckBox_autosave.isSelected()){
			if(jCheckBox_savehere.isSelected()){
				newFilename=opFile.toString().replaceFirst("\\."+opFile.getFileType(),"_CLM."+opFile.getFileType());
			}else{
				if(jTextField_saveAsPath.getText()!=""){
					newFilename=jTextField_saveAsPath.getText();
				}else {
					JOptionPane.showMessageDialog(null, "请正确输入保存路径", "出错啦", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		}
		
		//转换操作
		if(opFile.getFileType()=="cue"){
			try {
				if(newFilename==null)
					newFilename="temp.cue";
				oriCue.apptrans(newFilename, typeBox.getSelectedItem().toString());
			} catch(UnmappableCharacterException e){
				JOptionPane.showMessageDialog(null, "注意:\n该文件已经是正确的编码，将仅进行注音操作", "CueMaster", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			jButton_convert.setEnabled(false);
		}else if(opFile.getFileType()=="lrc"){
			if(newFilename==null)
				newFilename="temp.lrc";
			oriLrc.KanjiToKana(newFilename);
		}
		
		//转换后的操作
		loadFile(newFilename);
	}

	
	
	
	private void openFile(){
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Cue文件或Lrc文件", "cue","lrc");
	    chooser.setFileFilter(filter);
	    chooser.setAcceptAllFileFilterUsed(false);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
			loadFile(chooser.getSelectedFile().toString());	    	   	
	    }		
	}
	
	
	private void selectSaveAsPath() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Cue文件", "cue");
	    chooser.setFileFilter(filter);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if(oriCue!=null)
	    	chooser.setSelectedFile(new File(oriCue.toString().replaceFirst("\\.cue","_UTF.cue")));
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	    	jTextField_saveAsPath.setText(chooser.getSelectedFile().toString());
	    }
	}
	
	

	
	private void trayIconHide() {
		tray.remove(trayIcon);
		setVisible(true);
		setExtendedState(NORMAL);//设定窗口的弹出
	}


	private int closeFile() {
		if(JOptionPane.showConfirmDialog(null, "确认关闭当前文件么? (未保存的部分将会丢失)", "确认关闭", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.OK_OPTION){
			oriCue=null;
			oriLrc=null;
			opFile=null;
			jTextArea.setText("");
			typeBox.setEnabled(false);
			jLabel_cuetype.setEnabled(false);
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jRadioButton_kanjitokana.setEnabled(false);
			jRadioButton_normal.setEnabled(false);
			jButton_convert.setEnabled(false);
			jButton_closeFile.setEnabled(false);
			return 0;
		}else{
			return 1;
		}		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jTextArea.setText("您可以将Cue文件拖放至此");
			jTextArea.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 13));
		}
		return jTextArea;
	}

	/**
	 * This method initializes sPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSPane() {
		if (sPane == null) {
			sPane = new JScrollPane();
			sPane.setSize(new Dimension(350, 358));
			sPane.setLocation(new Point(10, 40));
			sPane.setViewportView(getJTextArea());			
		}
		return sPane;
	}

	/**
	 * This method initializes typeBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeBox() {
		if (typeBox == null) {
			typeBox = new JComboBox();
			for(Object a : musictype) {
				typeBox.addItem(a);
			}			
			typeBox.setFont(new Font("Dialog", Font.BOLD, 12));
			typeBox.setSize(new Dimension(268, 28));
			typeBox.setEnabled(false);
			typeBox.setLocation(new Point(367, 41));
		}
		return typeBox;
	}

	/**
	 * This method initializes jButtonConvert	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_convert() {
		if (jButton_convert == null) {
			jButton_convert = new JButton();
			jButton_convert.setBounds(new Rectangle(365, 354, 274, 37));
			jButton_convert.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jButton_convert.setEnabled(false);
			jButton_convert.setText("转  码 / 注  音");
		}
		jButton_convert.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				convert();
			}			
		});
		return jButton_convert;
	}
	
	
	/**
	 * This method initializes jButtonOpen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_open() {
		if (jButton_open == null) {
			jButton_open = new JButton();
			jButton_open.setBounds(new Rectangle(10, 5, 81, 29));
			jButton_open.setActionCommand("打开");
			jButton_open.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jButton_open.setText("打开...");
			jButton_open.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					openFile();
				}
			});
		}
		return jButton_open;
	}

	
	

	/**
	 * This method initializes jCheckBox_savehere	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_savehere() {
		if (jCheckBox_savehere == null) {
			jCheckBox_savehere = new JCheckBox();
			jCheckBox_savehere.setText("保存在原文件所在目录");
			jCheckBox_savehere.setName("jCheckBox");
			jCheckBox_savehere.setSelected(true);
			jCheckBox_savehere.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jCheckBox_savehere.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(jCheckBox_savehere.isSelected()){
						jLabel_save.setEnabled(false);
						jTextField_saveAsPath.setEnabled(false);
						jButton_saveas.setEnabled(false);
					}else {
						jLabel_save.setEnabled(true);
						jTextField_saveAsPath.setEnabled(true);
						jButton_saveas.setEnabled(true);
					}						
				}
			});
		}
		return jCheckBox_savehere;
	}


	/**
	 * This method initializes jPanel_save	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_save() {
		if (jPanel_save == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			jPanel_save = new JPanel();
			jPanel_save.setSize(new Dimension(272, 121));
			jPanel_save.setLocation(new Point(365, 220));
			jPanel_save.setPreferredSize(new Dimension(260, 92));
			jPanel_save.setLayout(gridLayout);
			jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel_save.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jPanel_save.add(getJCheckBox_savehere(), null);
			jPanel_save.add(jLabel_save, null);
			jPanel_save.add(getJPanel_saveas(), null);
		}
		return jPanel_save;
	}


	/**
	 * This method initializes jTextField_saveAsPath	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_saveAsPath() {
		if (jTextField_saveAsPath == null) {
			jTextField_saveAsPath = new JTextField();
			jTextField_saveAsPath.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jTextField_saveAsPath.setPreferredSize(new Dimension(190, 19));
			jTextField_saveAsPath.setEnabled(false);
			jTextField_saveAsPath.setName("jTextField_saveAsPath");
		}
		return jTextField_saveAsPath;
	}


	/**
	 * This method initializes jButton_saveas	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_saveas() {
		if (jButton_saveas == null) {
			jButton_saveas = new JButton();
			jButton_saveas.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 10));
			jButton_saveas.setName("jButton_saveas");
			jButton_saveas.setPreferredSize(new Dimension(60, 23));
			jButton_saveas.setEnabled(false);
			jButton_saveas.setText("浏览");
			jButton_saveas.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectSaveAsPath();
				}				
			});
		}
		return jButton_saveas;
	}


	/**
	 * This method initializes jPanel_saveas	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_saveas() {
		if (jPanel_saveas == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.weightx = 1.0;
			jPanel_saveas = new JPanel();
			jPanel_saveas.setLayout(new GridBagLayout());
			jPanel_saveas.add(getJTextField_saveAsPath(), gridBagConstraints);
			jPanel_saveas.add(getJButton_saveas(), new GridBagConstraints());
		}
		return jPanel_saveas;
	}


	

	
	/**
	 * This method initializes jPanel_lrcmode	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_lrcmode() {
		if (jPanel_lrcmode == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 0.0;
			gridBagConstraints2.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints2.ipadx = 5;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.ipadx = 5;
			gridBagConstraints1.insets = new Insets(0, 10, 10, 0);
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.gridy = 0;
			jPanel_lrcmode = new JPanel();
			jPanel_lrcmode.setLayout(new GridBagLayout());
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), SystemColor.controlShadow));
			jPanel_lrcmode.setLocation(new Point(365, 75));
			jPanel_lrcmode.setSize(new Dimension(272, 118));
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.add(getJRadioButton_kanjitokana(), gridBagConstraints1);
			jPanel_lrcmode.add(getJRadioButton_normal(), gridBagConstraints2);
		}
		return jPanel_lrcmode;
	}



	/**
	 * This method initializes jRadioButton_kanjitokana	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButton_kanjitokana() {
		if (jRadioButton_kanjitokana == null) {
			jRadioButton_kanjitokana = new JRadioButton();
			jRadioButton_kanjitokana.setText("日文汉字注音");
			jRadioButton_kanjitokana.setMnemonic(KeyEvent.VK_UNDEFINED);
			jRadioButton_kanjitokana.setHorizontalAlignment(SwingConstants.LEADING);
			jRadioButton_kanjitokana.setEnabled(false);
			jRadioButton_kanjitokana.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));
			buttonGroup_mode.add(jRadioButton_kanjitokana);
		}
		return jRadioButton_kanjitokana;
	}



	/**
	 * This method initializes jRadioButton_normal	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButton_normal() {
		if (jRadioButton_normal == null) {
			jRadioButton_normal = new JRadioButton();
			jRadioButton_normal.setText("一般模式(不注音)");
			jRadioButton_normal.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));
			jRadioButton_normal.setEnabled(false);
			buttonGroup_mode.add(jRadioButton_normal);
			jRadioButton_normal.setSelected(true);
		}
		return jRadioButton_normal;
	}



	/**
	 * This method initializes jCheckBox_autosave	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_autosave() {
		if (jCheckBox_autosave == null) {
			jCheckBox_autosave = new JCheckBox();
			jCheckBox_autosave.setBounds(new Rectangle(366, 195, 144, 21));
			jCheckBox_autosave.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jCheckBox_autosave.setText("转码/注音后自动保存");
			jCheckBox_autosave.setSelected(true);
			jCheckBox_autosave.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.DESELECTED){
						jPanel_save.setEnabled(false);
						jCheckBox_savehere.setEnabled(false);
						if(!jCheckBox_savehere.isSelected()){
							jLabel_save.setEnabled(false);
							jTextField_saveAsPath.setEnabled(false);
							jButton_saveas.setEnabled(false);
						}
					}else{
						jPanel_save.setEnabled(true);
						jCheckBox_savehere.setEnabled(true);
						if(!jCheckBox_savehere.isSelected()){
							jLabel_save.setEnabled(true);
							jTextField_saveAsPath.setEnabled(true);
							jButton_saveas.setEnabled(true);
						}
					}
				}
			});
		}
		return jCheckBox_autosave;
	}



	/**
	 * This method initializes jButton_closeFile1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_closeFile() {
		if (jButton_closeFile == null) {
			jButton_closeFile = new JButton();
			jButton_closeFile.setBounds(new Rectangle(100, 6, 86, 25));
			jButton_closeFile.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jButton_closeFile.setEnabled(false);
			jButton_closeFile.setText("关闭文件");
			jButton_closeFile.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					closeFile();					
				}				
			});
		}
		return jButton_closeFile;
	}







	//没有main的话，eclipse运行默认为调试组件，所以JFrame会出现在屏幕左上角
	public static void main(String[] args) {
		new CopyOfMasterFrame();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
