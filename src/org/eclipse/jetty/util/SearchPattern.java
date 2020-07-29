//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//
package org.eclipse.jetty.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * SearchPattern
 *
 * Fast search for patterns within strings and arrays of bytes. Uses an
 * implementation of the Boyer–Moore–Horspool algorithm with a 256 character
 * alphabet.
 *
 * The algorithm has an average-case complexity of O(n) on random text and O(nm)
 * in the worst case. where: m = pattern length n = length of data to search
 */
public class SearchPattern {

    static final int alphabetSize = 256;
    private int[] table;
    private byte[] pattern;

    /**
     * Produces a SearchPattern instance which can be used to find matches of
     * the pattern in data
     *
     * @param pattern byte array containing the pattern
     * @return a new SearchPattern instance using the given pattern
     */
    public static SearchPattern compile(byte[] pattern) {
        return new SearchPattern(Arrays.copyOf(pattern, pattern.length));
    }

    /**
     * Produces a SearchPattern instance which can be used to find matches of
     * the pattern in data
     *
     * @param pattern string containing the pattern
     * @return a new SearchPattern instance using the given pattern
     */
    public static SearchPattern compile(String pattern) {
        return new SearchPattern(pattern.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param pattern byte array containing the pattern used for matching
     */
    private SearchPattern(byte[] pattern) {
        this.pattern = pattern;

        if (pattern.length == 0) {
            throw new IllegalArgumentException("Empty Pattern");
        }

        //Build up the pre-processed table for this pattern.
        table = new int[alphabetSize];
        for (int i = 0; i < table.length; ++i) {
            table[i] = pattern.length;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            table[0xff & pattern[i]] = pattern.length - 1 - i;
        }
    }

    /**
     * Search for a complete match of the pattern within the data
     *
     * @param data The data in which to search for. The data may be arbitrary
     * binary data, but the pattern will always be
     * {@link StandardCharsets#US_ASCII} encoded.
     * @param offset The offset within the data to start the search
     * @param length The length of the data to search
     * @return The index within the data array at which the first instance of
     * the pattern or -1 if not found
     */
    public int match(byte[] data, int offset, int length) {
        validate(data, offset, length);

        int skip = offset;
        while (skip <= offset + length - pattern.length) {
            for (int i = pattern.length - 1; data[skip + i] == pattern[i]; i--) {
                if (i == 0) {
                    return skip;
                }
            }

            skip += table[0xff & data[skip + pattern.length - 1]];
        }

        return -1;
    }

    /**
     * Search for a partial match of the pattern at the end of the data.
     *
     * @param data The data in which to search for. The data may be arbitrary
     * binary data, but the pattern will always be
     * {@link StandardCharsets#US_ASCII} encoded.
     * @param offset The offset within the data to start the search
     * @param length The length of the data to search
     * @return the length of the partial pattern matched and 0 for no match.
     */
    public int endsWith(byte[] data, int offset, int length) {
        validate(data, offset, length);

        int skip = (pattern.length <= length) ? (offset + length - pattern.length) : offset;
        while (skip < offset + length) {
            for (int i = (offset + length - 1) - skip; data[skip + i] == pattern[i]; --i) {
                if (i == 0) {
                    return (offset + length - skip);
                }
            }

            if (skip + pattern.length - 1 < data.length) {
                skip += table[0xff & data[skip + pattern.length - 1]];
            } else {
                skip++;
            }
        }

        return 0;
    }

    /**
     * Search for a possibly partial match of the pattern at the start of the
     * data.
     *
     * @param data The data in which to search for. The data may be arbitrary
     * binary data, but the pattern will always be
     * {@link StandardCharsets#US_ASCII} encoded.
     * @param offset The offset within the data to start the search
     * @param length The length of the data to search
     * @param matched The length of the partial pattern already matched
     * @return the length of the partial pattern matched and 0 for no match.
     */
    public int startsWith(byte[] data, int offset, int length, int matched) {
        validate(data, offset, length);

        int matchedCount = 0;

        for (int i = 0; i < pattern.length - matched && i < length; i++) {
            if (data[offset + i] == pattern[i + matched]) {
                matchedCount++;
            } else {
                return 0;
            }
        }

        return matched + matchedCount;
    }

    /**
     * Performs legality checks for standard arguments input into SearchPattern
     * methods.
     *
     * @param data The data in which to search for. The data may be arbitrary
     * binary data, but the pattern will always be
     * {@link StandardCharsets#US_ASCII} encoded.
     * @param offset The offset within the data to start the search
     * @param length The length of the data to search
     */
    private void validate(byte[] data, int offset, int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset was negative");
        } else if (length < 0) {
            throw new IllegalArgumentException("length was negative");
        } else if (offset + length > data.length) {
            throw new IllegalArgumentException("(offset+length) out of bounds of data[]");
        }
    }

    /**
     * @return The length of the pattern in bytes.
     */
    public int getLength() {
        return pattern.length;
    }
}
