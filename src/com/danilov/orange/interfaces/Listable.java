package com.danilov.orange.interfaces;

import com.danilov.orange.model.PlayList;

public interface Listable {
	
	public String getFirstLine();
	public String getSecondLine();
	public PlayList toPlayList();
}
