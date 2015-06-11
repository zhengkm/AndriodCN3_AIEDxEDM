package data;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class UserAttendingParse {

	public ArrayList<UserBreif> uList = new ArrayList<UserBreif>();
	
	public ArrayList<UserBreif> getIdList(String paperID) {
		try {
			URL url = new URL(ConferenceURL.AttendingList + "contentID="+paperID);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();

			AttendingListParseHandler handle = new AttendingListParseHandler();
			xmlreader.setContentHandler(handle);

			InputSource is = new InputSource(url.openStream());
			xmlreader.parse(is);

		} catch (Exception ee) {
			System.out.print(ee.toString());
		}
		
		return uList;
	}
	private class AttendingListParseHandler extends DefaultHandler {
		private int state = 0;
		private StringBuilder sb=new StringBuilder();
		private UserBreif u;

		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {
			sb=new StringBuilder(0);
			if (qName.equals("Item")) {
				u = new UserBreif();
				return;
			}

		}

		public void endElement(String namespaceURI, String localName,
				String qName) throws SAXException {
			if (qName.equals("userID")) {
				u.setid(sb.toString());
				return;
			}
			if (qName.equals("name")) {
				u.setname(sb.toString());
				return;
			}
			if (qName.equals("username")) {
				u.setusername(sb.toString());
				return;
			}
			if (qName.equals("Item")) {
				uList.add(u);
				return;
			}
		}

		public void characters(char ch[], int start, int length) {

			sb.append(ch, start, length);
		}
	}
}
