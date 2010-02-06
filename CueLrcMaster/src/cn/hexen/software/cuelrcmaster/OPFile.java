package cn.hexen.software.cuelrcmaster;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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

public class OPFile extends File{
	
	private static final long serialVersionUID = 1L;

	public OPFile(String filename) throws FileNotFoundException{
		super(filename);
		if(!this.exists())
			throw new FileNotFoundException();
	}

	
	public synchronized String toText(){
		StringBuffer sb=new StringBuffer();
		FileInputStream inp = null;
		InputStreamReader inpReader = null;
		try {			
			inp=new FileInputStream(this);					
			inpReader=new InputStreamReader(inp,get_charset(this));
			int i;
			do{
				i=inpReader.read();
				if(i!=-1) sb.append(String.valueOf((char)i)); 
			}while(i!=-1);
			
			inpReader.close();
			inp.close();			
			
			return sb.toString();
		} catch (FileNotFoundException e) {			
			System.out.println("File not found");
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(inpReader!=null)
					inpReader.close();
				if(inp!=null)
					inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		try {
			if(inpReader!=null)
				inpReader.close();
			if(inp!=null)
				inp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public String getFileType(){
		return "op";
	}
	
	public void saveFile(String src){
		FileWriter fw = null;
		try {
			fw=new FileWriter(this);
			fw.write(src);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void saveAsFile(String src,String path){
		FileWriter fw = null;
		try {
			fw=new FileWriter(new File(path));
			fw.write(src);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//可以单提出去做Toolkit
	public static String get_charset(File file) {
		String charset = "GBK";
		byte [] first3Bytes = new byte[3];
		FileInputStream fis=null;
		BufferedInputStream bis = null;
		try{
			boolean checked = false;
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1){
				try {
					bis.close();
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
				return charset;
			}
			if (first3Bytes[0] == (byte)0xFF && first3Bytes[1] == (byte)0xFE) {
				charset = "UTF-16LE";
				checked = true;
			}
			else if(first3Bytes[0] == (byte)0xFE && first3Bytes[1] == (byte)0xFF) {
				charset = "UTF-16BE";
				checked = true;
			}
			else if(first3Bytes[0] == (byte)0xEF && first3Bytes[1] == (byte)0xBB && first3Bytes[2] == (byte)0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc ++;
					if (read >= 0xF0)
						break;
					if (0x80<=read && read <= 0xBF) //单独出现BF以下的，也算是GBK
						break;
					if (0xC0<=read && read <= 0xDF) {
						read = bis.read();
						if (0x80<= read && read <= 0xBF)//双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {//也有可能出错，但是几率较小
						read = bis.read();
						if (0x80<= read && read <= 0xBF) {
							read = bis.read();
							if (0x80<= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
				//System.out.println(loc + " " + Integer.toHexString(read));
			}
			bis.close();
			fis.close();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		return charset;
	}
	
}
