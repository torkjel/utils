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
package tuc.xml;

import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlBuilder {
    private StringBuilder sb = new StringBuilder();

    private State state = new State(null, -2);
    private Stack<State> stack = new Stack<State>();

    public XmlBuilder() {
    }

    public XmlBuilder open(String name) {
        if (state.tag != null && state.attr)
            sb.append(">");
        state.content = true;
        state.attr = false;
        stack.push(state);

        state = new State(name, state.indent + 2);
        if (state.tag != null)
            sb.append("\n");
        sb.append(indent()).append("<").append(name);

        return this;
    }

    public XmlBuilder attr(String name, Object value) {
        sb.append(" ").append(name).append("=\"").append(value).append("\"");
        return this;
    }

    public XmlBuilder text(String text) {
        if (state.attr)
            sb.append(">");
        sb.append(StringEscapeUtils.escapeXml(text));
        state.content = true;
        state.attr = false;
        return this;
    }

    public XmlBuilder close() {
        if (state.content)
            sb.append("</").append(state.tag).append(">");
        else sb.append("/>");
        state = stack.pop();
        return this;
    }

    private String indent() {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < state.indent; n++)
            sb.append(' ');
        return sb.toString();
    }

    private static class State {
        String tag;
        int indent;
        boolean content;
        boolean attr = true;

        State(String tag, int indent) {
            this.tag = tag;
            this.indent = indent;
        }
    }

    public String toString() {
        return sb.toString();
    }


}
