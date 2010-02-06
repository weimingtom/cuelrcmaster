package cn.hexen.software.cuelrcmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO:部分字更改 心（しん）->（こころ）

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
public class LrcFile extends OPFile{
	
	private static final long serialVersionUID = 1L;
	
	public LrcFile(String filename) throws FileNotFoundException{
		super(filename);		
	}
	
	public String zhuyin(String src,int mode){
		if(mode==0)
			return KanjiToKana(src);
		else if(mode==1)
			return KanjiToRoma(src);
		
		return src;
	}

	
	public String KanjiToKana(String src){
		try {
			String tmp=new String();
			//String str=toText();
			StringWriter sw=new StringWriter();
			BufferedReader br = new BufferedReader(new StringReader(src));		
			BufferedWriter bw = new BufferedWriter(sw);
			
			Pattern p1=Pattern.compile("(\\([あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんぁぃぅぇぉゅょがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽっー]+?\\))");
			Pattern p2=Pattern.compile("[アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォュョガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポッーabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\\s]+?\\(.*?\\)");
			Matcher m;
			
			while((tmp=br.readLine())!=null){
				if(tmp.matches("\\[.{2}?:.{2}?\\..{2}?\\].*")){
					if(/*tmp.matches(".*[a-zA-Z].*")||*/  tmp.matches("\\[.{2}?:.{2}?\\..{2}?\\]")){
						bw.append(tmp);
						bw.append("\r\n");
					}else{
						m=p1.matcher(tmp);
						while(m.find()){
							tmp=tmp.replace(m.group(), "");
						}
						String trans=IMEJNI.KanjiToKana(tmp.substring(10),true);
						m=p2.matcher(trans);
						while(m.find()){
							trans=trans.replace(m.group(), m.group().replaceFirst("\\(.*\\)",""));
						}
						trans=trans.replace("(（)", "").replace(")(）", "");
						
						//trans=trans.replaceAll("\\(\\s*?\\)", "　");//消除翻译空格造成的括号
						
						bw.append(tmp.substring(0, 10)+trans);
						bw.append("\r\n");
					}
				}				
			}			
			
			br.close();
			bw.close();
			
			String out=sw.toString();
			sw.close();
			
			return out;
			
			
		} catch (RuntimeException e) {
			System.out.println("!!!!!!!!!!!!!!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("!!!!!!!!!!!!!!!");
			e.printStackTrace();
		}
		
		return src;		
	}
	
	
	
	
	public String KanjiToRoma(String src){
		try {
			String tmp=new String();
			//String str=toText();
			StringWriter sw=new StringWriter();
			BufferedReader br = new BufferedReader(new StringReader(src));		
			BufferedWriter bw = new BufferedWriter(sw);
			
			Pattern p1=Pattern.compile("(\\([あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんぁぃぅぇぉゅょがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽっー]+?\\))");
			//Pattern p2=Pattern.compile("[アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォュョガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポッー]+?\\(.*?\\)");
			Matcher m;
			
			while((tmp=br.readLine())!=null){
				if(tmp.matches("\\[.{2}?:.{2}?\\..{2}?\\].*")){
					if(/*tmp.matches(".*[a-zA-Z].*")||*/  tmp.matches("\\[.{2}?:.{2}?\\..{2}?\\]")){
						bw.append(tmp);
						bw.append("\r\n");
					}else{
						m=p1.matcher(tmp);
						while(m.find()){
							tmp=tmp.replace(m.group(), "");
						}
						String trans=IMEJNI.KanjiToKana(tmp.substring(10),false);
						
						bw.append(tmp.substring(0, 10)+trans);
						bw.append("\r\n");
					}
				}				
			}
			
			br.close();
			bw.close();
			String out=sw.toString();
			sw.close();
			
			StringBuffer sb=new StringBuffer();
			PushbackReader pbr=new PushbackReader(new StringReader(out));
			int c1,c2;
			//防止出现java堆溢出，所以设置65535断点			
			while((c1=pbr.read())!=-1 && c1!=65535){
				String r=new String();
				String t=String.valueOf((char)c1)+String.valueOf((char)(c2=pbr.read()));
				if((r=KanaMap.kanaMap.get(t))!=null){
					sb.append(r);
				}else if((r=KanaMap.kanaMap.get(String.valueOf((char)c1)))!=null){
					if((char)c2=='ー'){
						sb.append(r+r.charAt(r.length()-1));
					}else{
						pbr.unread(c2);
						sb.append(r);
					}
				}else if((char)c1=='っ'){					
					sb.append(KanaMap.kanaMap.get(String.valueOf((char)(c2))).charAt(0));
					pbr.unread(c2);			
				}else{
					pbr.unread(c2);
					sb.append((char)c1);
				}
			}
			
			pbr.close();
			
			return sb.toString();
			
			
		} catch (RuntimeException e) {
			System.out.println("!!!!!!!!!!!!!!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("!!!!!!!!!!!!!!!");
			e.printStackTrace();
		}
		
		return src;		
	}
	
	
	
	public String getFileType(){
		return "lrc";
	}	

}
