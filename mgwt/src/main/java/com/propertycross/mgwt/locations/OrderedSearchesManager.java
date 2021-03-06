package com.propertycross.mgwt.locations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.googlecode.mgwt.storage.client.Storage;

public final class OrderedSearchesManager implements SearchesManager {

	private final SearchesStorage storage;
	private final Queue<Search> cache;
	private final int historySize;

	OrderedSearchesManager(SearchesStorage storage, int historySize) {
		this.storage = storage;
		this.historySize = historySize;
		cache = storage.load();
	}

	public OrderedSearchesManager(Storage storage, int historySize) {
		this(new MgwtStorage(storage), historySize);
	}

	public List<Search> recentSearches() {
		ArrayList<Search> arrayList = new ArrayList<Search>(cache);
		Collections.reverse(arrayList);
		return arrayList;
	}

	public void add(Search s) {
		removeSameLocationSearches(s);
		cache.add(s);
		while (cache.size() > historySize) {
			cache.poll();
		}
		storage.save(cache);
	}

	private void removeSameLocationSearches(Search newSearch) {
		for (Iterator<Search> it = cache.iterator(); it.hasNext();) {
			Search s = it.next();
			if (s.displayText().equalsIgnoreCase(newSearch.displayText())) {
				it.remove();
				return;
			}
		}
	}

}
