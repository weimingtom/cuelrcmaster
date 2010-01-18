package cn.hexen.software.cuelrcmaster;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.*;
import java.util.regex.*;

// TODO 大変だ(T_T)/~~~　既然是CueFile类，构造时应该是一个文件，输出时应该输出CueFile的对象，我的天啊，再想再改
// TODO 或者干脆定义成工作中的CueFile
public class CueFile extends OPFile {

	private static final long serialVersionUID = 1L;
	
	private String oriFile="";
	private String oriFileType="";

	
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
					oriFile = a.group().replaceAll("\"", "");
					break;
				}
			}
			
			br.close();
			fr.close();
			

			oriFileType = oriFile.replaceFirst(".*\\.", "").replaceFirst("\"","");

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
	
	
	public String getoriFileType(){
		return oriFileType;
	}
	
	
	
	
	public String getFileType(){
		return "cue";
	}
	
	
	
}
