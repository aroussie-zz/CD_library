package com.example.alexandreroussiere.parser;

/**
 * Created by Alexandre Roussière on 22/02/2016.
 */
import android.util.Log;

import org.w3c.dom.Attr;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
    String elementValue = null;
    Boolean elementOn = false;
    public static XMLGettersSetters data = null;

    public static XMLGettersSetters getXMLData() {
        return data;
    }


    public static void setXMLData(XMLGettersSetters data) {
        XMLHandler.data = data;
    }

    /**
     * This will be called when the tags of the XML starts.
     **/
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        elementOn = true;

        if (localName.equals("CATALOG"))
        {
            data = new XMLGettersSetters();
        }
        //If we find the tag CD we want its attribute
        else if(localName.equalsIgnoreCase("CD")){

        try{
            String attributeValue = attributes.getValue("attr");
            data.setSoldOut(attributes.getValue("attr"));
        }//end try
        //catch error if thrown to avoid possible null pointer exception
        catch (Exception e) {
            Log.i("err on handler   ", e.getMessage());}
      }

    }

    /**
     * This will be called when the tags of the XML end.
     **/
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        elementOn = false;

        /**
         * Sets the values after retrieving the values from the XML tags
         * */
        if (localName.equalsIgnoreCase("title"))
            data.setTitle(elementValue);
        else if (localName.equalsIgnoreCase("artist"))
            data.setArtist(elementValue);
        else if (localName.equalsIgnoreCase("country"))
            data.setCountry(elementValue);
        else if (localName.equalsIgnoreCase("company"))
            data.setCompany(elementValue);
        else if (localName.equalsIgnoreCase("price"))
            data.setPrice(elementValue);
        else if (localName.equalsIgnoreCase("year"))
            data.setYear(elementValue);
    }
    /**
     * This is called to get the tags value
     **/
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (elementOn) {
            elementValue = new String(ch, start, length);
            elementOn = false;
        }

    }
}

