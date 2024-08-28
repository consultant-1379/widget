package com.ericsson.eniq.events.widgets.client.dropdown.oracle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ericsson.eniq.events.widgets.client.ToString;
import com.google.gwt.user.client.ui.SuggestOracle;

public class LocalSuggestOracle<T> extends SuggestOracle implements DropDownSuggestOracle<T> {

    private ToString<T> converter;

    private final SuggestionTree tree = new SuggestionTree();

    private final HashMap<String, T> toReal = new HashMap<String, T>();

    private final HashMap<String, Set<String>> toCandidates = new HashMap<String, Set<String>>();

    private char[] whitespaceChars;

    private static final char WHITESPACE_CHAR = ' ';

    private static final String WHITESPACE_STRING = " ";

    private Response defaultResponse;

    public LocalSuggestOracle() {
    }

    public LocalSuggestOracle(final String whitespaceChars) {
        this.whitespaceChars = new char[whitespaceChars.length()];
        for (int i = 0; i < whitespaceChars.length(); i++) {
            this.whitespaceChars[i] = whitespaceChars.charAt(i);
        }
    }

    @Override
    public void update(final List<T> items) {
        clear();

        if (items != null && !items.isEmpty()) {
            final Collection<Suggestion> defaultSuggestions = new ArrayList<Suggestion>();
            for (final T item : items) {
                defaultSuggestions.add(createSuggestion(item));
                add(item);
            }

            defaultResponse = new Response(defaultSuggestions);
        }
    }

    public void clear() {
        defaultResponse = null;

        tree.clear();
        toCandidates.clear();
        toReal.clear();
    }

    public void add(final T item) {
        final String candidate = normalizeSuggestion(converter.toString(item));

        // candidates --> real suggestions.
        toReal.put(candidate, item);

        // word fragments --> candidates.
        final String[] words = candidate.split(WHITESPACE_STRING);
        for (final String word : words) {
            tree.add(word);
            Set<String> l = toCandidates.get(word);
            if (l == null) {
                l = new HashSet<String>();
                toCandidates.put(word, l);
            }
            l.add(candidate);
        }
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public void setConverter(final ToString<T> converter) {
        this.converter = converter;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        final String query = normalizeSearch(request.getQuery());
        final int limit = request.getLimit();

        // Get candidates from search words.
        final List<String> candidates = createCandidatesFromSearch(query);

        // Respect limit for number of choices.
        final int numberTruncated = Math.max(0, candidates.size() - limit);
        for (int i = candidates.size() - 1; i > limit; i--) {
            candidates.remove(i);
        }

        final List<DropDownSuggestion<T>> suggestions = new ArrayList<DropDownSuggestion<T>>();
        for (final String candidate : candidates) {
            final T item = toReal.get(candidate);
            if (item != null) {
                suggestions.add(createSuggestion(item));
            }
        }

        final Response response = new Response(suggestions);
        response.setMoreSuggestionsCount(numberTruncated);

        callback.onSuggestionsReady(request, response);
    }

    @Override
    public void requestDefaultSuggestions(final Request request, final Callback callback) {
        if (defaultResponse != null) {
            callback.onSuggestionsReady(request, defaultResponse);
        } else {
            super.requestDefaultSuggestions(request, callback);
        }
    }

    private DropDownSuggestion<T> createSuggestion(final T item) {
        return new DropDownSuggestion<T>(item, converter.toString(item));
    }

    private List<String> createCandidatesFromSearch(final String query) {
        final ArrayList<String> candidates = new ArrayList<String>();

        if (query.length() == 0) {
            return candidates;
        }

        // Find all words to search for.
        final String[] searchWords = query.split(WHITESPACE_STRING);
        HashSet<String> candidateSet = null;
        for (final String word : searchWords) {
            // Eliminate bogus word choices.
            if (word.length() == 0 || word.matches(WHITESPACE_STRING)) {
                continue;
            }

            // Find the set of candidates that are associated with all the
            // searchWords.
            final HashSet<String> thisWordChoices = createCandidatesFromWord(word);
            if (candidateSet == null) {
                candidateSet = thisWordChoices;
            } else {
                candidateSet.retainAll(thisWordChoices);

                if (candidateSet.size() < 2) {
                    // If there is only one candidate, on average it is cheaper to
                    // check if that candidate contains our search string than to
                    // continue intersecting suggestion sets.
                    break;
                }
            }
        }

        if (candidateSet != null) {
            candidates.addAll(candidateSet);
            Collections.sort(candidates);
        }

        return candidates;
    }

    private HashSet<String> createCandidatesFromWord(final String query) {
        final HashSet<String> candidateSet = new HashSet<String>();
        final List<String> words = tree.getSuggestions(query, Integer.MAX_VALUE);
        if (words != null) {
            // Find all candidates that contain the given word the search is a
            // subset of.
            for (final String word : words) {
                final Collection<String> belongsTo = toCandidates.get(word);
                if (belongsTo != null) {
                    candidateSet.addAll(belongsTo);
                }
            }
        }

        return candidateSet;
    }

    private String normalizeSearch(String search) {
        // Use the same whitespace masks and case normalization for the search
        // string as was used with the candidate values.
        search = normalizeSuggestion(search);

        // Remove all excess whitespace from the search string.
        search = search.replaceAll("\\s+", WHITESPACE_STRING);

        return search.trim();
    }

    private String normalizeSuggestion(String toNormalize) {
        toNormalize = toNormalize.toLowerCase();

        // Apply whitespace.
        if (whitespaceChars != null) {
            for (final char ignore : whitespaceChars) {
                toNormalize = toNormalize.replace(ignore, WHITESPACE_CHAR);
            }
        }
        return toNormalize;
    }

    // TODO: Implement correctly, as this solution is super slow
    // Possibly use http://en.wikipedia.org/wiki/Trie to speed things up
    public class SuggestionTree {

        private final Set<String> data = new HashSet<String>();

        List<String> getSuggestions(final String query, final int limit) {
            final ArrayList<String> strings = new ArrayList<String>();

            for (final String s : data) {
                if (s.equals(query) || s.contains(query)) {
                    strings.add(s);
                }
            }

            return strings;
        }

        public void add(final String word) {
            data.add(word);
        }

        public void clear() {
            data.clear();
        }
    }
}
