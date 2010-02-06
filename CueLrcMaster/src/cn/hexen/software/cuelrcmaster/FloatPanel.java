package cn.hexen.software.cuelrcmaster;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JWindow;

import com.sun.awt.AWTUtilities;

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
public class FloatPanel extends JWindow{

	private static final long serialVersionUID = 1L;
	
	private Point origin=new Point();
	private static Image background;
	private MasterFrame father;
	private Dimension size=new Dimension(70,70);
	
	static{
		background=GraphicsUtils.loadImage("fpback.jpg");
	}
	
	public FloatPanel(){}
	
	public FloatPanel(MasterFrame fatherframe) {	
		super();
		
		father=fatherframe;
		
		Dimension ss=father.getScreensize();
		setAlwaysOnTop(true);
		setLocation(ss.width-size.width-20,size.height-20);
		setSize(70, 70);
		AWTUtilities.setWindowOpacity(this, 0.8f);
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0,0,70,70,20,20));
	
		
		addMouseListener( 
				new MouseAdapter(){
					public void mousePressed(MouseEvent e){
						origin.x = e.getX();
						origin.y = e.getY();
					}
					public void mouseClicked(MouseEvent e) {
						if(e.getButton()==MouseEvent.BUTTON3){
							//TODO:通知任务栏菜单更改checkbox状态
							CheckboxMenuItem cmi=(CheckboxMenuItem)father.getPopupMenu().getItem(1);
							cmi.setState(false);
							close();
						}
					}
					public void mouseReleased(MouseEvent e) {
						super.mouseReleased(e);
					}
					public void mouseEntered(MouseEvent e) {
						repaint();							
					}						
				}
		);


		addMouseMotionListener(
				new MouseMotionAdapter(){
					public void mouseDragged(MouseEvent e){
						Point p =  getLocation();
						setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y );
					}
					
				}
		);
		
		
		new DropTarget(this,new TextDropTargetListener());
	}
	
	
	
	class TextDropTargetListener implements DropTargetListener {

		TextDropTargetListener() {
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
								father.loadFile(f.toString(),false);
								father.trayIconHide();
							}else {
								JOptionPane.showMessageDialog(null, "不是cue文件或lrc文件");
								return;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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

		
		public boolean isDragAcceptable(DropTargetDragEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}

		public boolean isDropAcceptable(DropTargetDropEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}

	}
	
	
	
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		g.clearRect(0, 0, 70, 70);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(0, 0, 70, 70, 20, 20);
		g.drawImage(background, 0, 0,67,67, null);
	}

	public void open(){
		setVisible(true);
	}
	
	public void close(){
		setVisible(false);
	}
}
 
