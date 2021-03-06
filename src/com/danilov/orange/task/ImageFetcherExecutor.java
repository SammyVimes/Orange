package com.danilov.orange.task;

import java.util.LinkedList;
import java.util.Queue;

import com.danilov.orange.util.Utilities;

public class ImageFetcherExecutor {
	
	private static final int MAX_QUANTITY = 4;
	private static Queue<ImageFetcher> fetchers = new LinkedList<ImageFetcher>();
	private static int fetchersQuantity = 0;
	
	public static void scheduleFetcher(final ImageFetcher fetcher) {
		if (fetchersQuantity < MAX_QUANTITY) {
			fetcher.execute();
			fetchersQuantity++;
		} else {
			fetchers.add(fetcher);
		}
	}
	
	public static void onFetchFinished() {
		fetchersQuantity--;
		if (fetchersQuantity < MAX_QUANTITY) {
			int querySize = fetchers.size();
			if (querySize > 0) {
				for (int i = 0; i < querySize; i++) {
					ImageFetcher fetcher = fetchers.remove();
					if (!fetcher.isCancelled()) {
						fetcher.execute();
						break;
					}
				}
			}
		}
	}
	
}
