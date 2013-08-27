package com.danilov.orange.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;

public class MockService {
	
	private static Random random = new Random();
	
	private static String[] ARTISTS = {"Blink-182", 
									   "Green Day",
									   "The Beatles",
									   "Four Year Strong"};
	
	private static String[] ALBUM_NAMES = {"Enema of the State", 
										   "Warning",
										   "Abbey Road",
										   "Really"};

	public static List<Album> getAlbumList(final int quantity) {
		List<Album> albumList = new ArrayList<Album>();
		for (int i = 0; i < quantity; i++) {
			albumList.add(getAlbum());
		}
		return albumList;
	}
	
	public static Album getAlbum() {
		String albumId = new String(Integer.toString(random.nextInt(200)));
		String albumName = ARTISTS[random.nextInt(ARTISTS.length)];
		String artistName = ALBUM_NAMES[random.nextInt(ALBUM_NAMES.length)];;
		List<Song> songs = getSongsList(2);
		Album album = new Album(albumId, albumName, artistName, songs);
		return album;
	}
	
	public static List<Song> getSongsList(int listSize) {
		List<Song> songs = new ArrayList<Song>();
		for (int i = 0; i < listSize; i++) {
			songs.add(new Song("2", "1", "123", "456"));
		}
		return songs;
	}
	
}
