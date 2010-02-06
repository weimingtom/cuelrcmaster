package cn.hexen.software.cuelrcmaster;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.*;
import java.util.regex.*;

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

public class CueFile extends OPFile {

	private static final long serialVersionUID = 1L;
	
	private String oriFileName="";	//Cue指向的源文件
	private String oriFileType="";	//Cue指向的源文件类型
	private String srcFileType="";	//实际源文件类型

	
	//用来建立现有cue文件的对象
	public CueFile(String filename) throws FileNotFoundException {
		super(filename);
		
		BufferedReader br;
		StringBuilder sb = null;
		FileReader fr=new FileReader(this);

		try {
			br = new BufferedReader(fr);

			for (;;) {
				try {
					sb = new StringBuilder(br.readLine());
				} catch (NullPointerException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (sb.toString().matches(".*\".*("+MusicTypes.toRegexString()+")\".*")) {
					Pattern p = Pattern.compile("\".*("+MusicTypes.toRegexString()+")\"");
					Matcher a = p.matcher(sb);
					a.find();
					oriFileName = a.group().replaceAll("\"", "");
					break;
				}
			}
			
			br.close();
			fr.close();
						
			oriFileType = oriFileName.replaceFirst(".*\\.", "").replaceFirst("\"","");
			
			//获取源音频文件的格式
			for(int i=0;i<MusicTypes.musictypes.length;i++){
				String srcfile=this.getAbsolutePath().substring(0, this.getAbsolutePath().lastIndexOf("\\")+1)+oriFileName.replaceFirst("\\..*", "\\.")+ MusicTypes.musictypes[i];
				if(new File(srcfile).exists()){
					srcFileType=MusicTypes.musictypes[i];
					break;
				}
			}
			if(srcFileType=="")
				srcFileType=oriFileType;

			// System.out.println("orifile: "+orifile);
			// System.out.println("orifiletype: "+orifiletype);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error");
			return;
		} catch (IOException e) {
			System.out.println("IOError");
			e.printStackTrace();
		}
	}
	
		
	public String apptrans(String tarfiletype) throws CharacterCodingException,
			IOException {
		
		RandomAccessFile inraf=null;
		RandomAccessFile outraf=null;
		
		try {
			File temp = new File("temp.clm");

			

			inraf = new RandomAccessFile(this, "r");
			outraf = new RandomAccessFile(temp, "rw");

			FileChannel finc = inraf.getChannel();
			FileChannel foutc = outraf.getChannel();

			MappedByteBuffer inmbb = finc.map(FileChannel.MapMode.READ_ONLY, 0,
					(int) this.length());

			Charset inCharset = Charset.forName("Shift-JIS");
			Charset outCharset = Charset.forName("UTF-8");
			// ISO-8859-1抛出UnmappableCharacterException,原因是Shift-JIS字符不能全部映射为ISO-8859-1字符，但可以全部映射为UTF字符

			/*
			 * public class UnmappableCharacterException extends
			 * CharacterCodingException Checked exception thrown when an input
			 * character (or byte) sequence is valid but cannot be mapped to an
			 * output byte (or character) sequence.
			 * 
			 * ——so It is not mapped to GBK!!
			 */

			CharsetDecoder inDecoder = inCharset.newDecoder();
			CharsetEncoder outEncoder = outCharset.newEncoder();

			CharBuffer cb = inDecoder.decode(inmbb);

			String sb = cb.toString();

			try {
				Pattern p = Pattern.compile("\\." + oriFileType
						+ ".*(WAVE|wave)");
				Matcher a = p.matcher(sb);
				sb = a.replaceFirst("\\." + tarfiletype + "\"" + " WAVE");

			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// cb.clear();不能使用，clear()并没有实际清除数据。新建一个CharBuffer
			CharBuffer cb2 = CharBuffer.wrap(sb);
			ByteBuffer outbb = outEncoder.encode(cb2);

			foutc.write(outbb);

			inraf.close();
			outraf.close();

			OPFile tempop = new OPFile(temp.toString());
			String out = tempop.toText();
			tempop.delete();
			temp.delete();

			return out;

		//catch块主要为了把打开的RandomAccessFile关闭，否则内存会有残留的垃圾信息，导致之后的转换文本有误
		} catch (CharacterCodingException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		}
	}
	

	
	public void apptrans(String outFilename, String tarfiletype)
			throws CharacterCodingException, IOException {
		
		RandomAccessFile inraf=null;
		RandomAccessFile outraf=null;
		
		try {
			File outfile = new File(outFilename);

			inraf = new RandomAccessFile(this, "r");
			outraf = new RandomAccessFile(outfile, "rw");

			FileChannel finc = inraf.getChannel();
			FileChannel foutc = outraf.getChannel();

			MappedByteBuffer inmbb = finc.map(FileChannel.MapMode.READ_ONLY, 0,
					(int) this.length());

			Charset inCharset = Charset.forName("Shift-JIS");
			Charset outCharset = Charset.forName("UTF-8");
			// ISO-8859-1抛出UnmappableCharacterException,原因是Shift-JIS字符不能全部映射为ISO-8859-1字符，但可以全部映射为UTF字符

			/*
			 * public class UnmappableCharacterException extends
			 * CharacterCodingException Checked exception thrown when an input
			 * character (or byte) sequence is valid but cannot be mapped to an
			 * output byte (or character) sequence.
			 * 
			 * ——so It is not mapped to GBK!!
			 */

			CharsetDecoder inDecoder = inCharset.newDecoder();
			CharsetEncoder outEncoder = outCharset.newEncoder();

			CharBuffer cb = inDecoder.decode(inmbb);

			String sb = cb.toString();

			try {
				Pattern p = Pattern.compile("\\." + oriFileType
						+ ".*(WAVE|wave)");
				Matcher a = p.matcher(sb);
				sb = a.replaceFirst("\\." + tarfiletype + "\"" + " WAVE");

			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// cb.clear();不能使用，clear()并没有实际清除数据。新建一个CharBuffer
			CharBuffer cb2 = CharBuffer.wrap(sb);
			ByteBuffer outbb = outEncoder.encode(cb2);

			foutc.write(outbb);

			inraf.close();
			outraf.close();
		} catch (CharacterCodingException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			if(inraf!=null)
				inraf.close();
			if(outraf!=null)
				outraf.close();
			e.printStackTrace();
			throw e;
		}
	}
	
	
	///另一种看似简单的写入方式，还没有测试
	/*public void app(JTextArea textArea){
		FileOutputStream fos = new FileOutputStream("test.txt");   
		Writer out = new OutputStreamWriter(fos, "utf-8");   
		out.write(contentString);   
		out.close();   
		fos.close();  
	}*/
	
	/**
	 * 获取Cue文件标注的音频文件类型
	 */
	public String getoriFileType(){
		return oriFileType;
	}
	
	/**
	 * 获取Cue所在目录下与Cue相应的音频文件类型
	 */
	public String getsrcFileType(){
		return srcFileType;
	}	
	
	
	public String getFileType(){
		return "cue";
	}
	
	
	
}
