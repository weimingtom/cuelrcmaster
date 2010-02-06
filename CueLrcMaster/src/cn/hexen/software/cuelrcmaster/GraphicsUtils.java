package cn.hexen.software.cuelrcmaster;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

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
public class GraphicsUtils {

	public final static Toolkit toolKit = Toolkit.getDefaultToolkit();
	
	private final static MediaTracker mediaTracker = new MediaTracker(
			new Container());
	
	private final static Map<Object,Object> cacheImages = new WeakHashMap<Object,Object>(100);
	
	private final static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	
	public GraphicsUtils(){
		
	}
	
	
	/**
	 * 加载内部file转为Image
	 * 
	 * @param inputstream
	 * @return
	 */
	public static Image loadImage(String str){
		if (str == null) {
			return null;
		}
		Image cacheImage =(Image) cacheImages.get(str.toLowerCase());;
		if (cacheImage == null) {
			InputStream in = new BufferedInputStream(classLoader.getResourceAsStream(str));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				byte[] bytes = new byte[16384];
				int read;
				while ((read = in.read(bytes)) >= 0) {
					byteArrayOutputStream.write(bytes, 0, read);
				}
				bytes = byteArrayOutputStream.toByteArray();
				cacheImages.put(str.toLowerCase(), cacheImage = toolKit
						.createImage(bytes));
				mediaTracker.addImage(cacheImage, 0);
				mediaTracker.waitForID(0);
				waitImage(100, cacheImage);
			} catch (Exception e) {
				throw new RuntimeException(str + " not found!");
			} finally {
				try {
					if (byteArrayOutputStream != null) {
						byteArrayOutputStream.close();
						byteArrayOutputStream = null;
					}
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException e) {
				}
			}

		}
		if (cacheImage == null) {
			throw new RuntimeException(
					("File not found. ( " + str + " )").intern());
		}
		return cacheImage;		
	}
	
	
	
	/**
	 * 延迟加载image,以使其同步。
	 * 
	 * @param delay
	 * @param image
	 */
	private final static void waitImage(int delay, Image image) {
		try {
			for (int i = 0; i < delay; i++) {
				if (toolKit.prepareImage(image, -1, -1, null)) {
					return;
				}
				Thread.sleep(delay);
			}
		} catch (Exception e) {

		}
	}
}