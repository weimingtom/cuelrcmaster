package cn.hexen.software.cuelrcmaster;

public class IMEJNI {

	
	static
	{
		System.loadLibrary("JPImeCPPJAVA");//载入dll
	}
	
	public native static String KanjiToKana(String src,boolean sep);//函数声明 
	
}
