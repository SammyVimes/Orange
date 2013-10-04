package com.danilov.orange.interfaces;

import java.util.List;

import com.danilov.orange.model.PlayList;
import com.danilov.orange.model.Song;

public interface Listable {
	
	public String getFirstLine();
	public String getSecondLine();
	public PlayList toPlayList();
	public List<Song> getSongs();
}
