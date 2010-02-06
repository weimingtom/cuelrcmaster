package cn.hexen.software.cuelrcmaster;


import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemColor;
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.CharacterCodingException;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/** 
 * Copyright 2010 Hexen
 * 	
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * @project CueLrcMaster
 * @author Hexen
 * @email hexen@live.cn
 * @version 1.01
 */

public class MasterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel jContentPane = null;
	private Dimension screensize;  //  @jve:decl-index=0:
	private JTextArea jTextArea = null;
	private JScrollPane sPane = null;
	private JComboBox typeBox_musicType = null;
	private JButton jButton_convert = null;
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
	private JRadioButton jRadioButton_roma = null;
	private JRadioButton jRadioButton_kana = null;
	private JCheckBox jCheckBox_newfile = null;
	private JLabel jLabel_author = null;
	private JButton jButton_closeFile = null;
	private FloatPanel floatPanel=null;
	private JPanel jPanel_cuemode = null;
	private PopupMenu popup=null;
	private JButton jButton_save = null;
	private JCheckBox jCheckBox_autosave = null;
	
	////
	private CueFile oriCue;  //  @jve:decl-index=0:
	private LrcFile oriLrc;  //  @jve:decl-index=0:
    private OPFile opFile;  //  @jve:decl-index=0:
	private final static int TRAYABLE=1;
	private static Font f=new Font("宋体",Font.PLAIN,12);  //  @jve:decl-index=0:		
	private boolean working=false;
	private boolean autosave=false;
	private int lrcmode=LrcMode.KANJITOKANA;	

	private interface LrcMode{
		int KANJITOKANA=0;
		int KANJITOROMA=1;
	}

	
	//进阶开发内容	
	//private int convertMode = 0;

	
	
	/**
	 * This is the default constructor
	 */
	public MasterFrame() {
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
		UIManager.put("JLabel.font",f);   
		UIManager.put("JComboBox.font",f);   
		UIManager.put("JButton.font",f);
		
		///*windows样式
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}//*/
		
		Toolkit toolkit = getToolkit();
		screensize = toolkit.getScreenSize();

		initializeTrayIcon();
				
		floatPanel=new FloatPanel(this);

		
		this.setSize(649, 436);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/CLM.png")));
		this.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
		this.setResizable(false);		
		this.setLocation(screensize.width / 2 - getWidth() / 2,
				screensize.height / 2 - getHeight() / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("CueLrcMaster ( CLMaster )  v1.0");
		this.setVisible(true);
		
		new DropTarget(jTextArea, new TextDropTargetListener(jTextArea));
		
	}

	private void initializeTrayIcon(){
		if (SystemTray.isSupported()) {
			// get the SystemTray instance			
			tray= SystemTray.getSystemTray();			
			// load an image
			Image image = GraphicsUtils.loadImage("CLM.png");
			
			// create a popup menu
			popup = new PopupMenu();
			popup.setFont(f);

			final CheckboxMenuItem cmi=new CheckboxMenuItem("悬浮窗口",false);
			
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
		
			cmi.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 2) {
						cmi.setState(false);
						floatPanel.close();
					} else if(e.getStateChange()==1) {
						cmi.setState(true);
						floatPanel.open();
					}
				}
			});
			popup.insert(cmi, 1);

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
			jLabel_author.setForeground(new Color(102, 102, 255));
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
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jContentPane.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jContentPane.add(getSPane(), null);
			jContentPane.add(getJButton_convert(), null);
			jContentPane.add(getJButton_open(), null);
			jContentPane.add(getJPanel_save(), null);
			buttonGroup_mode=new ButtonGroup();
			jContentPane.add(getJPanel_lrcmode(), null);
			jContentPane.add(getJCheckBox_newfile(), null);
			jContentPane.add(jLabel_author, null);
			jContentPane.add(getJButton_closeFile(), null);
			jContentPane.add(getJPanel_cuemode(), null);
			jContentPane.add(getJButton_save(), null);
			jContentPane.add(getJCheckBox_autosave(), null);
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
						if (fileList.size()>1) {
							JOptionPane.showMessageDialog(null, "请一次拖动一个文件！");
							return;
						}
						

						// Iterator在多文件List中使用，这里可以不用，以后可以考虑把TextDropTargetListener类单独重写
						Iterator iterator = fileList.iterator();
						while (iterator.hasNext()) {
							File f = (File) iterator.next();
							if (f.toString().matches(".*\\.cue") || f.toString().matches(".*\\.lrc")){
								loadFile(f.toString(),false);
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

	
	
	public void loadFile(String fileURL,boolean force) {
		if(opFile!=null){
			if(force==false){
				if(closeFile()==1)
					return;
			}
		}
		if(fileURL.toString().matches(".*\\.cue")){
			try {
				oriCue = new CueFile(fileURL);
				opFile=oriCue;
				jTextArea.setText(oriCue.toText());
			} catch (FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "文件未找到");
				return;
			}
			jCheckBox_autosave.setEnabled(true);
			if(!jCheckBox_autosave.isSelected())
				jCheckBox_autosave.doClick();
			typeBox_musicType.setSelectedItem(oriCue.getsrcFileType());
			typeBox_musicType.setEnabled(true);
			jPanel_cuemode.setEnabled(true);
			jPanel_cuemode.setBorder(BorderFactory.createTitledBorder(null, "Cue\u76ee\u6807\u97f3\u9891\u6587\u4ef6\u7c7b\u578b:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jRadioButton_roma.setEnabled(false);
			jRadioButton_kana.setEnabled(false);
			jButton_convert.setEnabled(true);

			
		}else if(fileURL.toString().matches(".*\\.lrc")){
			try {
				oriLrc = new LrcFile(fileURL);
				opFile=oriLrc;
				jTextArea.setText(oriLrc.toText());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "文件未找到");
				return;
			}
			jCheckBox_autosave.setEnabled(true);
			if(jCheckBox_autosave.isSelected())
				jCheckBox_autosave.doClick();
			typeBox_musicType.setEnabled(false);
			jPanel_cuemode.setEnabled(false);
			jPanel_cuemode.setBorder(BorderFactory.createTitledBorder(null, "Cue\u76ee\u6807\u97f3\u9891\u6587\u4ef6\u7c7b\u578b:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jPanel_lrcmode.setEnabled(true);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
			jRadioButton_roma.setEnabled(true);
			jRadioButton_kana.setEnabled(true);
			jButton_convert.setEnabled(true);
			jTextArea.addCaretListener(new LrcCaretListener());
			jTextArea.addMouseListener(new LrcMouseAdapter());
		}
				
		
		jCheckBox_newfile.setEnabled(true);
		jCheckBox_savehere.setEnabled(true);
		jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
		jPanel_save.setEnabled(true);		
		if(!jCheckBox_newfile.isSelected()){
			jPanel_save.setEnabled(false);
			jCheckBox_savehere.setEnabled(false);
			jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160,160)));
			if(!jCheckBox_savehere.isSelected()){
				jLabel_save.setEnabled(false);
				jTextField_saveAsPath.setEnabled(false);
				jButton_saveas.setEnabled(false);
			}
		}else{
			jPanel_save.setEnabled(true);
			jCheckBox_savehere.setEnabled(true);
			jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
			if(!jCheckBox_savehere.isSelected()){
				jLabel_save.setEnabled(true);
				jTextField_saveAsPath.setEnabled(true);
				jButton_saveas.setEnabled(true);
			}
		}
		jButton_closeFile.setEnabled(true);
	}

	private String getnewfilename(){
		String newFilename=null;
		if(jCheckBox_newfile.isSelected()){
			if(jCheckBox_savehere.isSelected()){
				newFilename=opFile.toString().replaceFirst("\\."+opFile.getFileType(),"_CLM."+opFile.getFileType());
			}else{
				if(jTextField_saveAsPath.getText()!=""){
					newFilename=jTextField_saveAsPath.getText();
					if(new File(newFilename).canWrite()){
						return newFilename;
					}else{
						return null;
					}					
				}else {
					JOptionPane.showMessageDialog(null, "请正确输入保存路径", "出错啦", JOptionPane.WARNING_MESSAGE);
					return null;
				}
			}
		}
		return newFilename;
	}
	
	private void operate() {

				
		//转换操作
		//CUE
		if(opFile.getFileType()=="cue"){
			try {
				jTextArea.setText(oriCue.apptrans(typeBox_musicType.getSelectedItem().toString()));
			} catch(CharacterCodingException e){
				JOptionPane.showMessageDialog(null, "该文件已经是正确的编码", "CueMaster", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			jButton_convert.setEnabled(false);
		//LRC
		}else if(opFile.getFileType()=="lrc"){
			if(jTextArea.getSelectedText()!=null)
				jTextArea.replaceSelection(oriLrc.zhuyin((jTextArea.getSelectedText()),lrcmode));
		}
		
		
		//自动保存操作
		if(autosave){
			String newFilename=getnewfilename();
			if(newFilename==null){
				JOptionPane.showMessageDialog(null, "保存失败！请正确输入保存路径", "出错啦", JOptionPane.WARNING_MESSAGE);
				return;
			}
			opFile.saveAsFile(jTextArea.getText(), newFilename);
			loadFile(newFilename,true);
		}
	}

	
	
	
	private void openFile(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFont(f);
		updateFont(chooser, f);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Cue文件或Lrc文件", "cue","lrc");
	    chooser.setFileFilter(filter);
	    chooser.setAcceptAllFileFilterUsed(false);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
			loadFile(chooser.getSelectedFile().toString(),false);	    	   	
	    }		
	}
	
	private void selectSaveAsPath() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFont(f);
		updateFont(chooser, f);
		FileNameExtensionFilter filter=null;
		if(opFile==null) return;
		if(opFile.getFileType()=="cue"){
			filter = new FileNameExtensionFilter("Cue文件", "cue");
		}else if(opFile.getFileType()=="lrc"){
			filter = new FileNameExtensionFilter("Lrc文件", "lrc");
		}
	    chooser.setFileFilter(filter);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if(opFile.getFileType()=="cue"){
	    	chooser.setSelectedFile(new File(oriCue.toString().replaceFirst("\\.cue","_CLM.cue")));
	    }else if(opFile.getFileType()=="lrc"){
	    	chooser.setSelectedFile(new File(oriLrc.toString().replaceFirst("\\.lrc","_CLM.lrc")));
	    }
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	    	jTextField_saveAsPath.setText(chooser.getSelectedFile().toString());
	    }
	}
	
	private static void updateFont(Component comp,Font font){
		comp.setFont(f);
		if(comp instanceof Container){
			Container c=(Container)comp;
			int n=c.getComponentCount();
			for(int i=0;i<n;i++){
				updateFont(c.getComponent(i), f);
			}
		}
	}

	
	public void trayIconHide() {
		tray.remove(trayIcon);
		setVisible(true);
		setExtendedState(NORMAL);//设定窗口的弹出
	}


	private int closeFile() {
		if(JOptionPane.showConfirmDialog(null, "确认关闭当前文件么? (未保存的部分将会丢失)", "确认关闭", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==JOptionPane.OK_OPTION){
			oriCue=null;
			oriLrc=null;
			opFile=null;
			jTextArea.setText("您可以将Cue文件或Lrc文件拖放至此");
			typeBox_musicType.setEnabled(false);
			jPanel_cuemode.setEnabled(false);
			jPanel_cuemode.setBorder(BorderFactory.createTitledBorder(null, "Cue\u76ee\u6807\u97f3\u9891\u6587\u4ef6\u7c7b\u578b:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jRadioButton_roma.setEnabled(false);
			jRadioButton_kana.setEnabled(false);
			jButton_convert.setEnabled(false);
			jButton_closeFile.setEnabled(false);
			jButton_save.setEnabled(false);
			
			jCheckBox_autosave.setEnabled(false);
			jCheckBox_newfile.setEnabled(false);
			jCheckBox_savehere.setEnabled(false);
			////
			jLabel_save.setEnabled(false);
			jTextField_saveAsPath.setEnabled(false);
			jButton_saveas.setEnabled(false);
			////
			jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160)));
			jPanel_save.setEnabled(false);
			
			for (CaretListener cl : jTextArea.getCaretListeners()) {
				jTextArea.removeCaretListener(cl);
			}
		
			for (MouseListener ml : jTextArea.getMouseListeners()) {
				if(ml.getClass()==LrcMouseAdapter.class)
					jTextArea.removeMouseListener(ml);
			}
			
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
			jTextArea.setText("您可以将Cue文件或Lrc文件拖放至此");
			jTextArea.setEditable(true);
			jTextArea.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 13));
		}
		return jTextArea;
	}

	private class LrcCaretListener implements CaretListener{
		@Override
		public void caretUpdate(CaretEvent e) {
			if(jTextArea.getSelectedText()!=null && working==false){
				try {
					working=true;
					jTextArea.select(jTextArea.getLineStartOffset(jTextArea.getLineOfOffset(jTextArea.getSelectionStart())),jTextArea.getLineEndOffset(jTextArea.getLineOfOffset(jTextArea.getSelectionEnd())));
					jTextArea.grabFocus();
					working=false;
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class LrcMouseAdapter extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			try {
				jTextArea.setSelectionStart(jTextArea.getLineStartOffset(jTextArea.getLineOfOffset(jTextArea.getCaretPosition())));
				jTextArea.setSelectionEnd(jTextArea.getLineEndOffset(jTextArea.getLineOfOffset(jTextArea.getCaretPosition())));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}						
		}
		public void mouseDragged(MouseEvent e) {
			try {
				jTextArea.setSelectionEnd(jTextArea.getLineEndOffset(jTextArea.getLineOfOffset(jTextArea.getCaretPosition())));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}						
		}
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
	 * This method initializes typeBox_musicType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeBox_musicType() {
		if (typeBox_musicType == null) {
			typeBox_musicType = new JComboBox();
			for(Object a : MusicTypes.musictypes){
				typeBox_musicType.addItem(a);
			}			
			typeBox_musicType.setFont(new Font("Dialog", Font.BOLD, 12));
			typeBox_musicType.setEditable(true);
			typeBox_musicType.setEnabled(false);
		}
		return typeBox_musicType;
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
				operate();
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
			jCheckBox_savehere.setEnabled(false);
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
			TitledBorder titledBorder = BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160, 160));
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			jPanel_save = new JPanel();
			jPanel_save.setSize(new Dimension(272, 121));
			jPanel_save.setLocation(new Point(365, 225));
			jPanel_save.setPreferredSize(new Dimension(260, 92));
			jPanel_save.setEnabled(false);
			jPanel_save.setLayout(gridLayout);
			jPanel_save.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jPanel_save.setBorder(titledBorder);
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
			gridBagConstraints1.insets = new Insets(10, 10, 10, 0);
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.gridy = 2;
			jPanel_lrcmode = new JPanel();
			jPanel_lrcmode.setLayout(new GridBagLayout());
			jPanel_lrcmode.setBorder(BorderFactory.createTitledBorder(null, "Lrc\u6ce8\u97f3\u6a21\u5f0f\u9009\u62e9:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), SystemColor.controlShadow));
			jPanel_lrcmode.setLocation(new Point(365, 80));
			jPanel_lrcmode.setSize(new Dimension(272, 118));
			jPanel_lrcmode.setEnabled(false);
			jPanel_lrcmode.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPanel_lrcmode.add(getJRadioButton_roma(), gridBagConstraints1);
			jPanel_lrcmode.add(getJRadioButton_kana(), gridBagConstraints2);
		}
		return jPanel_lrcmode;
	}



	/**
	 * This method initializes jRadioButton_roma	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButton_roma() {
		if (jRadioButton_roma == null) {
			jRadioButton_roma = new JRadioButton();
			jRadioButton_roma.setText("罗马音注音");
			jRadioButton_roma.setMnemonic(KeyEvent.VK_UNDEFINED);
			jRadioButton_roma.setHorizontalAlignment(SwingConstants.LEADING);
			jRadioButton_roma.setEnabled(false);
			jRadioButton_roma.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));
			buttonGroup_mode.add(jRadioButton_roma);
			jRadioButton_roma.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					lrcmode=LrcMode.KANJITOROMA;
				}				
			});
		}
		return jRadioButton_roma;
	}



	/**
	 * This method initializes jRadioButton_kana	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButton_kana(){
		if (jRadioButton_kana == null) {
			jRadioButton_kana = new JRadioButton();
			jRadioButton_kana.setText("平假名注音");
			jRadioButton_kana.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 14));
			jRadioButton_kana.setEnabled(false);
			jRadioButton_kana.setActionCommand("平假名注音");
			jRadioButton_kana.setMnemonic(KeyEvent.VK_UNDEFINED);
			buttonGroup_mode.add(jRadioButton_kana);
			jRadioButton_kana.setSelected(true);
			jRadioButton_kana.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					lrcmode=LrcMode.KANJITOKANA;
				}				
			});
		}
		return jRadioButton_kana;
	}



	/**
	 * This method initializes jCheckBox_newfile	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_newfile() {
		if (jCheckBox_newfile == null) {
			jCheckBox_newfile = new JCheckBox();
			jCheckBox_newfile.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jCheckBox_newfile.setText("保存为新文件");
			jCheckBox_newfile.setSize(new Dimension(101, 21));
			jCheckBox_newfile.setLocation(new Point(366, 200));
			jCheckBox_newfile.setEnabled(false);
			jCheckBox_newfile.setSelected(true);
			jCheckBox_newfile.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.DESELECTED){
						jPanel_save.setEnabled(false);
						jCheckBox_savehere.setEnabled(false);
						jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160,160)));
						if(!jCheckBox_savehere.isSelected()){
							jLabel_save.setEnabled(false);
							jTextField_saveAsPath.setEnabled(false);
							jButton_saveas.setEnabled(false);
						}
					}else{
						jPanel_save.setEnabled(true);
						jCheckBox_savehere.setEnabled(true);
						jPanel_save.setBorder(BorderFactory.createTitledBorder(null, "\u65b0\u6587\u4ef6\u4fdd\u5b58\u8def\u5f84:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(51, 51, 51)));
						if(!jCheckBox_savehere.isSelected()){
							jLabel_save.setEnabled(true);
							jTextField_saveAsPath.setEnabled(true);
							jButton_saveas.setEnabled(true);
						}
					}
				}
			});
		}
		return jCheckBox_newfile;
	}



	/**
	 * This method initializes jButton_closeFile1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_closeFile() {
		if (jButton_closeFile == null) {
			jButton_closeFile = new JButton();
			jButton_closeFile.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jButton_closeFile.setEnabled(false);
			jButton_closeFile.setLocation(new Point(182, 5));
			jButton_closeFile.setSize(new Dimension(90, 29));
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







	/**
	 * This method initializes jPanel_cuemode	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel_cuemode() {
		if (jPanel_cuemode == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridheight = 1;
			gridBagConstraints3.gridwidth = 3;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.ipadx = 1;
			gridBagConstraints3.weightx = 1.0;
			jPanel_cuemode = new JPanel();
			jPanel_cuemode.setLayout(new GridBagLayout());
			jPanel_cuemode.setSize(new Dimension(272, 59));
			jPanel_cuemode.setLocation(new Point(365, 20));
			jPanel_cuemode.setBorder(BorderFactory.createTitledBorder(null, "Cue\u76ee\u6807\u97f3\u9891\u6587\u4ef6\u7c7b\u578b:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\u5b8b\u4f53", Font.BOLD, 12), new Color(160, 160,160)));
			jPanel_cuemode.add(getTypeBox_musicType(), gridBagConstraints3);
		}
		return jPanel_cuemode;
	}



	/**
	 * This method initializes jButton_save	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_save() {
		if (jButton_save == null) {
			jButton_save = new JButton();
			jButton_save.setLocation(new Point(96, 5));
			jButton_save.setText("保存");
			jButton_save.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
			jButton_save.setEnabled(false);
			jButton_save.setSize(new Dimension(81, 29));
			jButton_save.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (jCheckBox_newfile.isSelected()) {
						String name = getnewfilename();
						if (name == null)
							return;
						opFile.saveAsFile(jTextArea.getText(), name);
						loadFile(name, true);
					} else {
						opFile.saveFile(jTextArea.getText());
					}
				}				
			});
		}
		return jButton_save;
	}


	public PopupMenu getPopupMenu(){
		return popup;
	}
	
	public Dimension getScreensize(){
		return screensize;
	}


	/**
	 * This method initializes jCheckBox_autosave	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_autosave() {
		if (jCheckBox_autosave == null) {
			jCheckBox_autosave = new JCheckBox();
			jCheckBox_autosave.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jCheckBox_autosave.setSize(new Dimension(79, 21));
			jCheckBox_autosave.setLocation(new Point(475, 200));
			jCheckBox_autosave.setPreferredSize(new Dimension(97, 23));
			jCheckBox_autosave.setEnabled(false);
			jCheckBox_autosave.setText("自动保存");
			jCheckBox_autosave.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED)
						autosave=true;
					else
						autosave=false;					
				}				
			});			
		}
		return jCheckBox_autosave;
	}



	//没有main的话，eclipse运行默认为调试组件，所以JFrame会出现在屏幕左上角
	public static void main(String[] args) {
		new MasterFrame();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
