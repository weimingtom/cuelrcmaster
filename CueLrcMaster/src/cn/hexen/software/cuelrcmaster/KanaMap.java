package cn.hexen.software.cuelrcmaster;

import java.util.HashMap;

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
public class KanaMap{

	private static final long serialVersionUID = 1L;
	
	public static HashMap<String, String> kanaMap;
	
	static {
		kanaMap=new HashMap<String, String>();
		kanaMap.put("あ", "a");
		kanaMap.put("ア", "a");
		kanaMap.put("い", "i");
		kanaMap.put("イ", "i");
		kanaMap.put("う", "u");
		kanaMap.put("ウ", "u");
		kanaMap.put("え", "e");
		kanaMap.put("エ", "e");
		kanaMap.put("お", "o");
		kanaMap.put("オ", "o");
		kanaMap.put("か", "ka");
		kanaMap.put("カ", "ka");
		kanaMap.put("き", "ki");
		kanaMap.put("キ", "ki");
		kanaMap.put("く", "ku");
		kanaMap.put("ク", "ku");
		kanaMap.put("け", "ke");
		kanaMap.put("ケ", "ke");
		kanaMap.put("こ", "ko");
		kanaMap.put("コ", "ko");
		kanaMap.put("が", "ga");
		kanaMap.put("ガ", "ga");
		kanaMap.put("ぎ", "gi");
		kanaMap.put("ギ", "gi");
		kanaMap.put("ぐ", "gu");
		kanaMap.put("グ", "gu");
		kanaMap.put("げ", "ge");
		kanaMap.put("ゲ", "ge");
		kanaMap.put("ご", "go");
		kanaMap.put("ゴ", "go");		
		kanaMap.put("さ", "sa");
		kanaMap.put("サ", "sa");
		kanaMap.put("し", "shi");
		kanaMap.put("シ", "shi");
		kanaMap.put("す", "su");
		kanaMap.put("ス", "su");
		kanaMap.put("せ", "se");
		kanaMap.put("セ", "se");
		kanaMap.put("そ", "so");
		kanaMap.put("ソ", "so");
		kanaMap.put("ざ", "za");
		kanaMap.put("ザ", "za");
		kanaMap.put("じ", "ji");
		kanaMap.put("ジ", "ji");
		kanaMap.put("ず", "zu");
		kanaMap.put("ズ", "zu");
		kanaMap.put("ぜ", "ze");
		kanaMap.put("ゼ", "ze");
		kanaMap.put("ぞ", "zo");
		kanaMap.put("ゾ", "zo");
		kanaMap.put("た", "ta");
		kanaMap.put("タ", "ta");
		kanaMap.put("ち", "chi");
		kanaMap.put("チ", "chi");
		kanaMap.put("つ", "tsu");
		kanaMap.put("ツ", "tsu");
		kanaMap.put("て", "te");
		kanaMap.put("テ", "te");
		kanaMap.put("と", "to");
		kanaMap.put("ト", "to");
		kanaMap.put("だ", "da");
		kanaMap.put("ダ", "da");
		kanaMap.put("ぢ", "di");
		kanaMap.put("ヂ", "di");
		kanaMap.put("づ", "du");
		kanaMap.put("ヅ", "du");
		kanaMap.put("で", "de");
		kanaMap.put("デ", "de");
		kanaMap.put("ど", "do");
		kanaMap.put("ド", "do");		
		kanaMap.put("な", "na");
		kanaMap.put("ナ", "na");
		kanaMap.put("に", "ni");
		kanaMap.put("ニ", "ni");
		kanaMap.put("ぬ", "nu");
		kanaMap.put("ヌ", "nu");
		kanaMap.put("ね", "ne");
		kanaMap.put("ネ", "ne");
		kanaMap.put("の", "no");
		kanaMap.put("ノ", "no");
		kanaMap.put("は", "ha");
		kanaMap.put("ハ", "ha");
		kanaMap.put("ひ", "hi");
		kanaMap.put("イ", "hi");
		kanaMap.put("ふ", "fu");
		kanaMap.put("フ", "fu");
		kanaMap.put("へ", "he");
		kanaMap.put("ヘ", "he");
		kanaMap.put("ほ", "ho");
		kanaMap.put("ホ", "ho");
		kanaMap.put("ば", "ba");
		kanaMap.put("バ", "ba");
		kanaMap.put("び", "bi");
		kanaMap.put("ビ", "bi");
		kanaMap.put("ぶ", "bu");
		kanaMap.put("ブ", "bu");
		kanaMap.put("べ", "be");
		kanaMap.put("ベ", "be");
		kanaMap.put("ぼ", "bo");
		kanaMap.put("ボ", "bo");
		kanaMap.put("ぱ", "pa");
		kanaMap.put("パ", "pa");
		kanaMap.put("ぴ", "pi");
		kanaMap.put("ピ", "pi");
		kanaMap.put("ぷ", "pu");
		kanaMap.put("プ", "pu");
		kanaMap.put("ぺ", "pe");
		kanaMap.put("ペ", "pe");
		kanaMap.put("ぽ", "po");
		kanaMap.put("ポ", "po");		
		kanaMap.put("ま", "ma");
		kanaMap.put("マ", "ma");
		kanaMap.put("み", "mi");
		kanaMap.put("ミ", "mi");
		kanaMap.put("む", "mu");
		kanaMap.put("ム", "mu");
		kanaMap.put("め", "me");
		kanaMap.put("メ", "me");
		kanaMap.put("も", "mo");
		kanaMap.put("モ", "mo");
		kanaMap.put("や", "ya");
		kanaMap.put("ヤ", "ya");
		kanaMap.put("ゆ", "yu");
		kanaMap.put("ユ", "yu");
		kanaMap.put("よ", "yo");
		kanaMap.put("ヨ", "yo");
		kanaMap.put("ら", "ra");
		kanaMap.put("ラ", "ra");
		kanaMap.put("り", "ri");
		kanaMap.put("リ", "ri");
		kanaMap.put("る", "ru");
		kanaMap.put("ル", "ru");
		kanaMap.put("れ", "re");
		kanaMap.put("レ", "re");
		kanaMap.put("ろ", "ro");
		kanaMap.put("ロ", "ro");
		kanaMap.put("わ", "wa");
		kanaMap.put("ワ", "wa");
		kanaMap.put("を", "wo");
		kanaMap.put("ヲ", "wo");
		kanaMap.put("ん", "n");
		kanaMap.put("ン", "n");
		kanaMap.put("きゃ", "kya");
		kanaMap.put("キャ", "kya");
		kanaMap.put("きゅ", "kyu");
		kanaMap.put("キュ", "kyu");
		kanaMap.put("きょ", "kyo");
		kanaMap.put("キョ", "kyo");
		kanaMap.put("しゃ", "sya");
		kanaMap.put("シャ", "sya");
		kanaMap.put("しゅ", "syu");
		kanaMap.put("シュ", "syu");
		kanaMap.put("しょ", "syo");
		kanaMap.put("ショ", "syo");
		kanaMap.put("ちゃ", "cya");
		kanaMap.put("チャ", "cya");
		kanaMap.put("ちゅ", "cyu");
		kanaMap.put("チュ", "cyu");
		kanaMap.put("ちょ", "cyo");
		kanaMap.put("チョ", "cyo");
		kanaMap.put("にゃ", "nya");
		kanaMap.put("ニャ", "nya");
		kanaMap.put("にゅ", "nyu");
		kanaMap.put("ニュ", "nyu");
		kanaMap.put("にょ", "nyo");
		kanaMap.put("ニョ", "nyo");
		kanaMap.put("ひゃ", "hya");
		kanaMap.put("ヒャ", "hya");
		kanaMap.put("ひゅ", "hyu");
		kanaMap.put("ヒュ", "hyu");
		kanaMap.put("ひょ", "hyo");
		kanaMap.put("ヒョ", "hyo");
		kanaMap.put("みゃ", "mya");
		kanaMap.put("ミャ", "mya");
		kanaMap.put("みゅ", "myu");
		kanaMap.put("ミュ", "myu");
		kanaMap.put("みょ", "myo");
		kanaMap.put("ミョ", "myo");
		kanaMap.put("りゃ", "rya");
		kanaMap.put("リャ", "rya");
		kanaMap.put("りゅ", "ryu");
		kanaMap.put("リュ", "ryu");
		kanaMap.put("りょ", "ryo");
		kanaMap.put("リョ", "ryo");
		kanaMap.put("ぎゃ", "gya");
		kanaMap.put("ギャ", "gya");
		kanaMap.put("ぎゅ", "gyu");
		kanaMap.put("ギュ", "gyu");
		kanaMap.put("ぎょ", "gyo");
		kanaMap.put("ギョ", "gyo");
		kanaMap.put("じゃ", "ja");
		kanaMap.put("ジャ", "ja");
		kanaMap.put("じゅ", "ju");
		kanaMap.put("ジュ", "ju");
		kanaMap.put("じょ", "jo");
		kanaMap.put("ジョ", "jo");
		kanaMap.put("びゃ", "bya");
		kanaMap.put("ビャ", "bya");
		kanaMap.put("びゅ", "byu");
		kanaMap.put("ビュ", "byu");
		kanaMap.put("びょ", "byo");
		kanaMap.put("ビョ", "byo");
		kanaMap.put("ぴゃ", "pya");
		kanaMap.put("ピャ", "pya");
		kanaMap.put("ぴゅ", "pyu");
		kanaMap.put("ピュ", "pyu");
		kanaMap.put("ぴょ", "pyo");
		kanaMap.put("ピョ", "pyo");		
	}
}
