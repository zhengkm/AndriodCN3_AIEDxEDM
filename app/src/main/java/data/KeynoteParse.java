package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;




public class KeynoteParse {

	private ArrayList<Keynote> knList = new ArrayList<Keynote>();
	
	
	public ArrayList<Keynote> getKeynoteData() {
		
		InputStreamReader isr=null;
		InputStream stream=null;
		try {
			//URL url = new URL("http://halley.exp.sis.pitt.edu/cn3mobile/allSessionsAndPresentations.jsp?eventid=86");
			
			//Use Post Method
			String urlString = new String("http://halley.exp.sis.pitt.edu/cn3mobile/allSessionsAndPapers.jsp?conferenceID=135&noAbstract=1");
			
			URL url = new URL(urlString);
			    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			    conn.setReadTimeout(10000 /* milliseconds */);
			    conn.setConnectTimeout(15000 /* milliseconds */);
			    conn.setRequestMethod("POST");
			    conn.setDoInput(true);
			    // Starts the query
			    conn.connect();
			    stream = conn.getInputStream(); 
			
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();

			KeynoteParseHandler shandler = new KeynoteParseHandler();
			xr.setContentHandler(shandler);
			isr = new InputStreamReader(stream, "iso-8859-1");
			//InputStreamReader isr = new InputStreamReader(entity.getContent(),"UTF-8");

			xr.parse(new InputSource(isr));
			stream.close();
			isr.close();
		} catch (Exception ee) {
			System.out.print(ee.toString());
		}
		finally{
			try {
				if(stream != null)
				stream.close();
				if(isr != null)
				isr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return knList;
	}

	
	private class KeynoteParseHandler extends DefaultHandler {
		private KeynoteAbstractParser KAP = new KeynoteAbstractParser();
		private int state = 0;
		private Keynote ke;
		private boolean isKeynote;
		private String contentId="";
		private boolean keynoteStart = false;
		private StringBuilder sb = new StringBuilder();

		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String namespaceURI, String localName,
								 String qName, Attributes atts) throws SAXException {

			sb = new StringBuilder(0);
			if (localName.equals("Items")) {
				keynoteStart = true;
				return;
			}

			if (localName.equals("Item")) {

				ke = new Keynote();
				//ke.speakerName=" ";
				ke.speakerAffiliation = " ";
				return;
			}

		}

		public void endElement(String namespaceURI, String localName,
							   String qName) throws SAXException {


			if (localName.equals("eventSessionID")) {
				ke.ID = sb.toString();
				return;
			}

			if (localName.equals("contentID")) {
				contentId=sb.toString();
				return;
			}

			if (localName.equals("paperTitle")) {
				ke.title = sb.toString();
				return;
			}
			if (localName.equals("sessionDate")) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				try {
					date = new SimpleDateFormat("MM-dd-yyyy").parse(sb.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str = formatter.format(date);
				Hashtable<String, String> Dtrans = new Hashtable<String, String>();
				Dtrans.put("2015-06-22", "1");
				Dtrans.put("2015-06-23", "2");
				Dtrans.put("2015-06-24", "3");
				Dtrans.put("2015-06-25", "4");
				Dtrans.put("2015-06-26", "5");
				Dtrans.put("2015-06-27", "6");
				Dtrans.put("2015-06-28", "7");
				Dtrans.put("2015-06-29", "8");

				Hashtable<String, String> Datetrans = new Hashtable<String, String>();
				Datetrans.put("2015-06-22", "Monday, Jun.22");
				Datetrans.put("2015-06-23", "Tuesday, Jun.23");
				Datetrans.put("2015-06-24", "Wednesday, Jun.24");
				Datetrans.put("2015-06-25", "Thursday, Jun.25");
				Datetrans.put("2015-06-26", "Friday, Jun.26");
				Datetrans.put("2015-06-27", "Saturday, Jun.27");
				Datetrans.put("2015-06-28", "Sunday, Jun.28");
				Datetrans.put("2015-06-29", "Monday, Jun.29");
				ke.date = Datetrans.get(str);
				ke.dayid = Dtrans.get(str);
				state = 2;
				return;
			}
			if (localName.equals("begintime") && keynoteStart == true) {
				ke.beginTime = sb.toString();
				return;
			}
			if (localName.equals("endtime") && keynoteStart == true) {
				ke.endTime = sb.toString();
				return;
			}
			if (localName.equals("location")) {
				ke.room = sb.toString();
				return;
			}

			if (localName.equals("authors")) {
				ke.speakerName = sb.toString();
				return;
			}
			if (localName.equals("contentType")) {
				if ("Keynote".equals(sb.toString())) {
					isKeynote = true;
				} else
					isKeynote = false;
				return;
			}
			if (localName.equals("Item")) {
				if (isKeynote) {
					ke.description=KAP.getKeynoteAbstract(contentId);
					knList.add(ke);
				}
				isKeynote = false;
				return;
			}
			if (localName.equals("Items")) {
				keynoteStart = false;
				return;
			}
		}

		public void characters(char ch[], int start, int length) {

			sb.append(ch, start, length);
		}
	}

		public class KeynoteAbstractParser {

			public String getKeynoteAbstract(String ID) {
				String ab = "";
				try {
					URL url = new URL("http://halley.exp.sis.pitt.edu/cn3mobile/contentAbstract.jsp?contentID=" + ID);

					InputStream in = url.openStream();
					String data = convertToString(in);

					ab = data;

					in.close();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ab;
			}

			public String convertToString(InputStream is) {
				if (is != null) {
					StringBuilder sb = new StringBuilder();
					String line;
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

						while ((line = reader.readLine()) != null) {
							sb.append(line).append(" ");
						}
					} catch (Exception e) {
						System.out.print(e.getMessage());
					} finally {
						try {
							is.close();
						} catch (Exception e) {

						}
					}
					return sb.toString();
				} else {
					return "";
				}
			}
		}
	}



