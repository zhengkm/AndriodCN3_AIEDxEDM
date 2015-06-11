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


public class UserProfileParse {
	private User u;
	
	public User getUser(String userID) {
		try {
			URL url = new URL(ConferenceURL.UserProfile + "userID="+userID+"");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();

			UserProfileParseHandler handle = new UserProfileParseHandler();
			xmlreader.setContentHandler(handle);

			InputSource is = new InputSource(url.openStream());
			xmlreader.parse(is);

		} catch (Exception ee) {
			System.out.print(ee.toString());
		}
		
		return u;
	}

	private class UserProfileParseHandler extends DefaultHandler {

		private StringBuilder sb=new StringBuilder();

		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {
			sb=new StringBuilder(0);
			if (qName.equals("Item")) {
				u = new User();
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
			if (qName.equals("email")) {
				u.setemail(sb.toString());
				return;
			}
			if (qName.equals("userroleID")) {
				u.setuserroleID(sb.toString());
				return;
			}
			if (qName.equals("username")) {
				u.setusername(sb.toString());
				return;
			}
			if (qName.equals("bio")) {
				u.setbio(sb.toString());
				return;
			}
			if (qName.equals("personalWebSite")) {
				u.setpersonalwebsite(sb.toString());
				return;
			}
			if (qName.equals("position")) {
				u.setposition(sb.toString());
				return;
			}
			if (qName.equals("departmentAffiliation")) {
				u.setdepartment(sb.toString());
				return;
			}
			if (qName.equals("organizationAffiliation")) {
				u.setorganization(sb.toString());
				return;
			}
			if (qName.equals("city")) {
				u.setcity(sb.toString());
				return;
			}
			if (qName.equals("country")) {
				u.setcountry(sb.toString());
				return;
			}
			if (qName.equals("linkedin")) {
				u.setlinkedin(sb.toString());
				return;
			}
			if (qName.equals("Item")) {
				return;
			}
		}

		public void characters(char ch[], int start, int length) {

			sb.append(ch, start, length);
		}
	}
}
