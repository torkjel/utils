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

import java.util.Iterator;

import tuc.collections.ArrayIterator;

/**
 * Helper class for parsing command lines.<p>
 *
 * This class supports both short (one-character) and long argument names,
 * with and without argument values. Several short arguments can be combined
 * into one.<p>
 *
 * E.g:<br>
 * <code>-a -b foo -cde --bar --xyzzy=wizzy</pre><br>
 * <ul>
 * <li>'-a' is the short argument 'a', without a value.
 * <li>'-b foo' is the short argument 'b' with the value 'foo'.
 * <li>'-cde' is the three short arguments 'c', 'd' and 'e' combined.
 * <li>'--bar' is the long argument 'bar' without a value.
 * <li>'--xyzzy=wizzy' is the long argument 'xyzzy' with the argument 'wizzy'.
 * </ul>
 *
 * Shortcomings:<br>
 * - This does not support arguments that take lists of values. <br>
 * - Values can not start with a dash. <br>
 * - Short arguments with values should not be combined, as it's impossible to
 * know which short argument the valuee belongs to.<p>
 *
 * @author torkjelh
 */
public class CommandLine {

    private String[] args;

    public CommandLine(String[] args) {
        this.args = args;
    }

    public boolean hasShortArg(char c) {
        for (String arg : args)
            if (arg.startsWith("-") && !arg.startsWith("--") && arg.indexOf(c) > -1)
                return true;
        return false;
    }

    public boolean hasLongArg(String a) {
        String aa = "--" + a;
        String aaa = aa + "=";
        for (String arg : args)
            if (arg.equals(aa) || arg.startsWith(aaa))
                return true;
        return false;
    }

    public String getShortArg(char c) {
        for (int n = 0; n < args.length - 1; n++) {
            String arg = args[n];
            if (arg.startsWith("-") && arg.indexOf(c) > -1) {
                String val = args[n + 1];
                if (!val.startsWith("-"))
                    return val;
            }
        }
        return null;
    }

    public String getShortArg(char c, String defaultValue) {
        String val = getShortArg(c);
        return val != null ? val : defaultValue;
    }

    public int getShortIntArg(char c, int defaultValue) {
        return getInt(getShortArg(c), defaultValue);
    }

    public String getLongArg(String a) {
        String aa = "--" + a + "=";
        for (String arg : args)
            if (arg.startsWith(aa))
                return arg.substring(arg.indexOf(aa) + aa.length());
        return null;
    }

    public String getLongArg(String c, String defaultValue) {
        String val = getLongArg(c);
        return val != null ? val : defaultValue;
    }

    public int getLongIntArg(String c, int defaultValue) {
        return getInt(getLongArg(c), defaultValue);
    }

    private int getInt(String val, int def) {
        return val != null ? Integer.parseInt(val) : def;
    }

    public String getArg(char s, String l) {
        return hasShortArg(s) ? getShortArg(s) : getLongArg(l);
    }

    public String getArg(char s, String l, String defaultValue) {
        return hasShortArg(s) ? getShortArg(s, defaultValue) : getLongArg(l, defaultValue);
    }

    public int getIntArg(char s, String l, int defaultValue) {
        return hasShortArg(s) ? getShortIntArg(s, defaultValue) : getLongIntArg(l, defaultValue);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + ":[");
        for (Iterator<String> i = new ArrayIterator<String>(args); i.hasNext(); ) {
            sb.append(i.next());
            if (i.hasNext())
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
