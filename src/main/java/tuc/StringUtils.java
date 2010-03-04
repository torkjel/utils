/*
 * Copyright 2010 Torkjel Hongve. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package tuc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class StringUtils {

    private StringUtils() { }

    public static final String UTF8 = "UTF-8";

    public static final String SHA = "SHA";

    /**
     * Convert a string to an array of bytes, representing the string in the
     * given encoding. This works like {@link String#getBytes(String)}, but if
     * the encoding is unknown, the resulting {@Llink UnsupportedEncodingException}
     * is wrapped in a {@link RuntimeException}.
     *
     * @param data
     * @param encoding
     * @return
     */
    public static byte[] getBytes(String data, String encoding) {
        try {
            return data.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hash the given data
     * @param data
     * @return
     */
    public static String hash(String data) {
        return hash(data, UTF8);
    }

    /**
     * Hash the given data.
     * @see #hash(byte[], String)
     * @see #getBytes(String, String)
     *
     * @param data
     * @param encoding
     * @return
     */
    public static String hash(String data, String encoding) {
        return hash(getBytes(data, encoding));
    }

    /**
     * Hash the given data using the SHA algorithm.
     * @see #hash(byte[], String)
     * @param data
     * @return
     */
    public static String hash(byte[] data) {
        return hash(data, SHA);
    }

    /**
     * Hash the given data, using the given hashing algorithm. The Algorithm
     * must be supported by {@link MessageDigest}.
     *
     * @param data
     * @param algorighm
     * @return a string representation of the
     */
    public static String hash(byte[] data, String algorighm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorighm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(data);
        return toHex(hash);
    }

    /**
     * Convert data to a string of hex-digits.
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(nibbleToHex(b >>> 4));
            sb.append(nibbleToHex(b));
        }
        return sb.toString();
    }

    /**
     * Get the hex-digit representation of a value. Only the first 4 bits of
     * the value is considered.
     *
     * @param value
     * @return
     */
    public static char nibbleToHex(int value) {
        value &= 0xf;
        return (char)(value > 9 ? 'a' + (value - 10) : '0' + value);
    }
}
