package cn.hexen.software.cuelrcmaster;

public class MusicTypes{
	public static final String[] musictypes={"ape","tta","tak","wav","flac"};
	public static String toRegexString(){
		StringBuffer sb= new StringBuffer();
		for(int i=0;i<musictypes.length;i++){
			if(i!=musictypes.length-1)
				sb.append(musictypes[i]+"|");
			else
				sb.append(musictypes[i]);
		}
		return sb.toString();
	}
}
