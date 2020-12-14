package swe_mo.fitnessfunction;

import java.util.HashMap;
import java.util.Map;

public class FunctionMap {
	private Map<String, MapEntry> map = new HashMap<String, MapEntry>();
	public String rootkey;
	

	public int add(String s) {
		map.put("["+map.size()+"]", new MapEntry(s, false));
		return map.size()-1;
	}
	public void edit(String key, String s, boolean checked) {
		map.put(key, new MapEntry(s, checked));
	}
	public void remove(String key) {
		map.put(key, null);
	}
	public String get(String key) {
		if(map.get(key) == null) return "";
		return map.get(key).s;
	}
	public boolean checked(String key) {
		if(map.get(key) == null) return true;
		return map.get(key).checked;
	}
	
	public String findAnchor(String key) {
		for(String k : map.keySet()) {
			if(map.get(k)!=null && map.get(k).s.contains(key))
				return k;
		}
		return "";
	}
	
	public int size() {
		return map.size();
	}
	
	public boolean allChecked() {
		for(int i=0; i<map.size(); i++) {
			if(map.get("["+i+"]") != null && !map.get("["+i+"]").checked)
				return false;
		}
		return true;
	}
	
	public void setAllUnchecked() {
		for(int i=0; i<map.size(); i++) {
			if(map.get("["+i+"]") != null)
				map.get("["+i+"]").checked = false;
		}
	}
	
	
	@Override
	public String toString() {
		String s="";
		for(int i=0; i<map.size(); i++) {
			String key = "["+i+"]";
			if(map.get(key) == null) continue;
			s += "\n"+key+"  "+map.get(key).s;//+"  ("+map.get(key).checked+")";
		}
		return s;
	}
	
	
	
	
	private class MapEntry{
		String s;
		boolean checked;
		
		public MapEntry(String s, boolean checked) {
			this.s = s;
			this.checked = checked;
		}
	}
	
}
