package com.danilov.orange.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.model.ArtistProperty;
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
				/*TODO: do smth with IDs*/
				album = new Album("tmp", albumName, artistName);
				album.addSong(song);
				albums.add(album);
			}
		}
		Collections.sort(albums, new AlbumComparator());
		return albums; 
	}
	
	static class AlbumComparator implements Comparator<Album> {

		@Override
		public int compare(Album first, Album second) {
			String str1 = first.getArtistName();
			String str2 = second.getArtistName();
			int res = 0;
			int compRes = str1.compareToIgnoreCase(str2);
			if (compRes > 0) {
				res = 1;
			} else if (compRes < 0) {
				res = -1;
			}
			return res;
		}
		
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
	
	private static ArtistProperty getArtistPropertyWithName(final List<ArtistProperty> artistProperties, final String name) {
		ArtistProperty result = null;
		for (ArtistProperty artistProperty : artistProperties) {
			if (artistProperty.getAtristName().equalsIgnoreCase(name)) {
				result = artistProperty;
				break;
			}
		}
		return result;
	}
	
	public static List<ArtistProperty> sortByAuthors(final List<Song> songs) {
		List<ArtistProperty> artistProperty = new ArrayList<ArtistProperty>();
		for (Song song : songs) {
			ArtistProperty aProperty = getArtistPropertyWithName(artistProperty, song.getArtist());
			if (aProperty != null) {
				aProperty.addSong(song);
			} else {
				String artistName = song.getArtist();
				aProperty = new ArtistProperty(artistName);
				aProperty.addSong(song);
				artistProperty.add(aProperty);
			}
		}
		return artistProperty; 
	}
	
}
