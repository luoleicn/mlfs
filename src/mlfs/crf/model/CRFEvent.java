/*
 * CRFEvent.java 
 * 
 * Author : 罗磊，luoleicn@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Last Update:Jul 10, 2011
 * 
 */
package mlfs.crf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class CRFEvent.
 */
public class CRFEvent {

	/** 特征key是观测值，values是观测值对应的一系列特征，这个变量是static的，由所有CRFEvent共享. */
	public final static Map<String, List<String>> CHAR_FEAT = new HashMap<String, List<String>>();
	
	/** 输入. */
	public String[] inputs;
	
	/** 标签. */
	public int[] labels;
	
	/** 每一个输入对应一个特征序列,由用户自己提供的特征文件读取出来. */
	public List<List<String>> charFeat;
	
	/**
	 * Instantiates a new cRF event.
	 *
	 * @param inputs the inputs
	 * @param labels the labels
	 */
	public CRFEvent(String[] inputs, int[] labels)
	{
		if (labels.length != inputs.length)
			throw new IllegalArgumentException("labels.length != inputs.length");
		
		this.labels = labels;
		this.inputs = inputs;
		charFeat = new ArrayList<List<String>>();
	}
	
	/**
	 * 添加特征序列，如果输入不在CHAR_FEAT中，则向CHAR_FEAT加入
	 *
	 * @param feats the feats
	 */
	public void addCharFeat(String[] feats)
	{
		String input = feats[0];
		if (!CHAR_FEAT.containsKey(input))
		{
			List<String> featsLst = new ArrayList<String>();
			for (String s : feats)
				featsLst.add(s);
			CHAR_FEAT.put(input, featsLst);
		}
		charFeat.add(CHAR_FEAT.get(input));
	}
	
}
