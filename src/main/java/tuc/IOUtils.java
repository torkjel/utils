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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility functions pertaining to IO.
 *
 * @author torkjelh
 *
 */
public final class IOUtils {

    private IOUtils() { }

    /**
     * Create an URL from a string.
     *
     * @param urlSpec
     * @return
     * @throws RuntimeException if the given string is not a well-formed URL.
     */
    public static URL asUrl(String urlSpec) {
        try {
            return new URL(urlSpec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an URL from a file.
     *
     * @param f
     * @return
     */
    public static URL asUrl(File f) {
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an absolute URL from an base URL and a relative path.
     * Only supports URLs on the form <code>protocol://host:port/path</code>.
     *
     * @param base
     * @param path
     * @return
     */
    public static String absoluteUrl(URL base, String path) {
        if (isAbsoluteURI(path))
            return path;
        else {
            String basePath = base.getPath();
            if (!basePath.endsWith("/"))
                basePath += "/";
            if (path.startsWith("/"))
                path = path.substring(1);
            try {
                return new URI(
                    base.getProtocol(), null, base.getHost(),
                    base.getPort(), basePath + path, null, null).toString();
            } catch (URISyntaxException e) {
                throw Exceptions.toRuntimeEx(e);
            }
        }
    }

    public static boolean isAbsoluteURI(String path) {
        return URI.create(path).isAbsolute();
    }


    /**
     * Copy all data from an input stream to an output stream, and close both
     * streams when finished.
     *
     * @param in
     * @param out
     * @throws RuntimeException if any IO-error occurs.
     */
    public static void pipe(InputStream in, OutputStream out) {
        byte[] data = new byte[4096];
        try {
            int len;
            while ((len = in.read(data)) != -1)
                out.write(data, 0, len);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try { in.close(); } catch (IOException e) { e.printStackTrace(); }
            try { out.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
