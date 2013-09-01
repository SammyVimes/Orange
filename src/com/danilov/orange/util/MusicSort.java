package com.danilov.orange.util;

import java.util.ArrayList;
import java.util.List;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.model.Unit;
import com.danilov.orange.test.MockService;

public class MusicSort {

	public static List<Album> sortByAlbums(final List<Song> songs) {
		List<Album> albums = new ArrayList<Album>();
		for (Song song : songs) {
			Album album = getAlbumWithTitle(albums, song.getAlbum());
			if (album != null) {
				album.addSong(song);
			} else {
				String albumName = song.getAlbum();
				String artistName = song.getArtist();
				album = new Album("tmp", albumName, artistName);
				album.addSong(song);
				albums.add(album);
			}
		}
		return albums; // TODO: Implement sorting
	}
	
	private static Album getAlbumWithTitle(final List<Album> albums, final String title) {
		Album result = null;
		for (Album album : albums) {
			if (album.getAlbumName().equalsIgnoreCase(title)) {
				result = album;
				break;
			}
		}
		return result;
	}
	
	public static List<Unit> sortByAuthors(final List<Song> songs) {
		return null;
	}
	
}
