package com.danilov.orange.util;

import java.util.List;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.model.Unit;
import com.danilov.orange.test.MockService;

public class MusicSort {

	public static List<Album> sortByAlbums(final List<Song> songs) {
		return MockService.getAlbumList(5); // TODO: Implement sorting
	}
	
	public static List<Unit> sortByAuthors(final List<Song> songs) {
		return null;
	}
	
}
