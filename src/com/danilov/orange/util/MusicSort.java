package com.danilov.orange.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.net.Uri;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.ArtistProperty;

public class MusicSort {
	
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
	
	public static List<ArtistProperty> sortByAuthors(final List<Album> albums) {
		List<ArtistProperty> artistProperty = new ArrayList<ArtistProperty>();
		for (Album album : albums) {
			ArtistProperty aProperty = getArtistPropertyWithName(artistProperty, album.getArtistName());
			if (aProperty == null) {
				String artistName = album.getArtistName();
				aProperty = new ArtistProperty(artistName);
				artistProperty.add(aProperty);
			}
			if (Utilities.fileExists(album.getThumbnailPath()) && (aProperty.getThumbnailPath() == null)) {
				aProperty.setThumbnailPath(album.getThumbnailPath());
			}
			aProperty.addAlbum(album);
		}
		return artistProperty; 
	}
	
}
