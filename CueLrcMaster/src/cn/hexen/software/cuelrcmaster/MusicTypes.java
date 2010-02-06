package cn.hexen.software.cuelrcmaster;

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
