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
package tuc.file;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;

import tuc.collections.ArrayIterator;
import tuc.collections.ItemProducer;
import tuc.collections.ItemProducerIterator;

/**
 * Class for traversing the files in a directory tree.
 * As this implements {@link Iterable} it is convenient to iterate through
 * files using a simple for loop:
 * <pre>
 * for (String f : new DirectoryTreeTraverser("/home/foo/") {
 *   System.out.println(f);
 * }
 * </pre>
 *
 * @author torkjelh
 */
public class DirectoryTreeTraverser implements Iterable<String>, ItemProducer<String> {

    private Stack<Directory> stack = new Stack<Directory>();

    private Directory dir;

    public DirectoryTreeTraverser(String directory) {
        dir = new Directory(directory);
    }

    @Override
    public Iterator<String> iterator() {
        return new ItemProducerIterator<String>(this);
    }

    @Override
    public String produce() {

        // depth-first traversal of directory tree.
        File produced = null;
        while (produced == null) {

            // are we done with this directory?
            while (dir != null && !dir.hasNext())
                dir = stack.isEmpty() ? null : stack.pop();

            // are we done?
            if (dir == null)
                return null;

            // descend into directories
            File next = null;
            while (dir.hasNext() && (next = dir.next()).isDirectory()) {
                stack.push(dir);
                dir = new Directory(next);
            }

            // did we find a file?
            if (next.isFile()) {
                produced = next;
            }
        }

        return produced.getPath();
    }

    private static class Directory extends ArrayIterator<File> {

        Directory(File dir) {
            super(dir.listFiles());
        }

        Directory(String path) {
            this(new File(path));
        }

    }
}
