package data;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;




public class PaperContentParse {

	public ArrayList<PaperContent> List = new ArrayList<PaperContent> ();

	
	public ArrayList<PaperContent> getData() {
			
			try {
				URL url = new URL("http://halley.exp.sis.pitt.edu/cn3mobile/allContentsAndAuthors.jsp?conferenceID=135&noAbstract=1");
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser saxParser = spf.newSAXParser();
				XMLReader xr = saxParser.getXMLReader();

				SessionParseHandler shandler = new SessionParseHandler();
				xr.setContentHandler(shandler);
		
				InputStreamReader isr = new InputStreamReader(url.openStream(), "iso-8859-1");

				xr.parse(new InputSource(isr));
				isr.close();
				
//				InputStream in = url.openStream();
//				String data = convertToString(in);
//				StringFilter sf = new StringFilter();
//				data = sf.FilterForXML(data);
//				InputSource is = new InputSource(new StringReader(data));
//				xr.parse(is);
//				in.close();

			} catch (Exception ee) {
				System.out.print(ee.toString());
			}
			
			return List;
		}
	public String convertToString(InputStream is){
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
				
				while ((line = reader.readLine()) != null) {
					sb.append(line).append(" ");
				}
			}
			catch(Exception e)
			{
				System.out.print(e.getMessage());
			}
			finally {
				try{
				is.close();
				}
				catch(Exception e)
				{
					
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}
	private class SessionParseHandler extends DefaultHandler {
		private int state = 0;
		private PaperContent se;
		private boolean contentStart=false;
		private StringBuilder sb=new StringBuilder();
		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String namespaceURI, String localName,
								 String qName, Attributes atts) throws SAXException {
			sb=new StringBuilder(0);
			if(localName.equals("contents")){
				//se = new PaperContent();
				contentStart=true;
				return;
			}
			if(localName.equals("content")){
				se = new PaperContent();
				se.authors="";
				return;
			}


		}

		public void endElement(String namespaceURI, String localName,
							   String qName) throws SAXException {

			if (localName.equals("contentID")) {
				se.id=sb.toString();
				se.paperAbstract= getAbstract(sb.toString());
				return;
			}
			if (localName.equals("title")) {
				se.title=sb.toString();
				return;
			}
			if(localName.equals("authorpresenters")&& contentStart==true){

				return;
			}
			if(localName.equals("name")&& contentStart==true){
				se.authors=se.authors+sb.toString()+", ";
				return;
			}
			if (localName.equals("contentType")) {
				se.type=sb.toString();
				return;
			}

			if (localName.equals("content")) {
				List.add(se);
				return;
			}
			if(localName.equals("contents")){
				se = new PaperContent();
				contentStart=false;
				return;
			}
		}

		public void characters(char ch[], int start, int length) {
			sb.append(ch, start, length);
		}

		private String getAbstract(String id) {
			// TODO Auto-generated method stub
			String ab = new String();
			PaperAbstractParse pap = new PaperAbstractParse();
			ab = pap.getPaperAbstract(id);
			return ab;
		}
	}

}
