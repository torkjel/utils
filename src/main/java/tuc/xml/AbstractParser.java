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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;

import tuc.Exceptions;

/**
 * Abstract base class for XML document parsers. Attempts to take some of the
 * pain out of DOM...
 *
 * @author torkjelh
 */
public abstract class AbstractParser {

    private Document doc;

    /**
     * Create a parser using the {@link SimpleClasspathEntityResolver] entity
     * resolver.
     *
     * @param in
     */
    protected AbstractParser(InputStream in) {
        this(in, new SimpleClasspathEntityResolver());
    }

    /**
     * Create a parser using using the given {@link EntityResolver}.
     *
     * @param in
     * @param er
     */
    protected AbstractParser(InputStream in, EntityResolver er) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.setEntityResolver(er);
            this.doc = builder.parse(in);
        } catch (Exception e) {
            throw Exceptions.toRuntimeEx(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the root element of the document.
     *
     * @return
     */
    protected Element root() {
        return doc.getDocumentElement();
    }

    /**
     * Get the first child element of the given element with the given name.
     *
     * @param elem
     * @param name
     * @return an element, or <code>null</code> if no such element exists.
     */
    protected Element getChildElement(Element elem, String name) {
        List<Element> children = getChildElements(elem, name);
        return children.size() > 0 ? children.get(0) : null;
    }

    /**
     * Get all child elements of the given element with the given name.
     *
     * @param elem
     * @param name
     * @return
     */
    protected List<Element> getChildElements(Element elem, String name) {
        List<Element> children = new ArrayList<Element>();
        NodeList nl = elem.getChildNodes();
        for (int n = 0; n < nl.getLength(); n++) {
            Node node = nl.item(n);
            if (node instanceof Element) {
                Element e = (Element)node;
                if (e.getTagName().equals(name))
                    children.add(e);
            }
        }
        return children;
    }
}
