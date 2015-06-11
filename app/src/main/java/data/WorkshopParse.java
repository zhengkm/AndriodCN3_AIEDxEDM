package data;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class WorkshopParse {

//    private ArrayList<Workshop> wsList = new ArrayList<Workshop>();
//
//
//    public ArrayList<Keynote> getWorkshopData() {
//
//        InputStreamReader isr=null;
//        InputStream stream=null;
//        try {
//            //URL url = new URL("http://halley.exp.sis.pitt.edu/cn3mobile/allSessionsAndPresentations.jsp?eventid=86");
//
//            //Use Post Method
//            String urlString = new String("http://halley.exp.sis.pitt.edu/cn3mobile/allSessionsAndPapers.jsp?conferenceID=135&noAbstract=1");
//
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            // Starts the query
//            conn.connect();
//            stream = conn.getInputStream();
//
//
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            SAXParser saxParser = spf.newSAXParser();
//            XMLReader xr = saxParser.getXMLReader();
//
//            KeynoteParseHandler shandler = new KeynoteParseHandler();
//            xr.setContentHandler(shandler);
//            isr = new InputStreamReader(stream, "iso-8859-1");
//            //InputStreamReader isr = new InputStreamReader(entity.getContent(),"UTF-8");
//
//            xr.parse(new InputSource(isr));
//            stream.close();
//            isr.close();
//        } catch (Exception ee) {
//            System.out.print(ee.toString());
//        }
//        finally{
//            try {
//                if(stream != null)
//                    stream.close();
//                if(isr != null)
//                    isr.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        return knList;
//    }
//
//
//    private class KeynoteParseHandler extends DefaultHandler {
//        private KeynoteAbstractParser KAP=new KeynoteAbstractParser();
//        private int state = 0;
//        private Keynote ke;
//        private boolean isKeynote;
//        private boolean keynoteStart = false;
//
//        public void startDocument() throws SAXException {
//        }
//
//        public void endDocument() throws SAXException {
//        }
//
//        public void startElement(String namespaceURI, String localName,
//                                 String qName, Attributes atts) throws SAXException {
//            if (localName.equals("Items")) {
//
//                keynoteStart=true;
//                return;
//            }
//            if (localName.equals("Item")) {
//
//                ke = new Keynote();
//                //ke.speakerName=" ";
//                ke.speakerAffiliation=" ";
//                return;
//            }
//            if(localName.equals("eventSessionID")){
//                state=8;
//                return;
//            }
//
//            if(localName.equals("contentID")){
//                state=9;
//                return;
//            }
//
//            if (localName.equals("paperTitle")) {
//                state = 1;
//                return;
//            }
//            if (localName.equals("sessionDate")) {
//                state = 2;
//                return;
//            }
//            if (localName.equals("begintime")&& keynoteStart==true) {
//                state = 3;
//                return;
//            }
//            if (localName.equals("endtime")&& keynoteStart == true) {
//                state = 4;
//                return;
//            }
//            if (localName.equals("location")) {
//                state = 5;
//                return;
//            }
//
//            if(localName.equals("authors")){
//                state=6;
//                return;
//            }
//            if(localName.equals("contentType")){
//                state=7;
//                return;
//            }
//        }
//
//        public void endElement(String namespaceURI, String localName,
//                               String qName) throws SAXException {
//            if (localName.equals("Item")) {
//                if(isKeynote) {
//                    knList.add(ke);
//                }
//                isKeynote=false;
//                return;
//            }
//            if (localName.equals("Items")) {
//                keynoteStart=false;
//                return;
//            }
//        }
//
//        public void characters(char ch[], int start, int length) {
//
//            String content = new String(ch, start, length);
//            switch (state) {
//                case 1:
//                    ke.title = content;
//                    state=0;
//                    break;
//                case 2:
//                    SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");
//                    Date date=new Date();
//                    try {
//                        date=new SimpleDateFormat("MM-dd-yyyy").parse(content);
//                    } catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    String   str   =   formatter.format(date);
//                    Hashtable<String, String> Dtrans = new Hashtable<String, String>();
//                    Dtrans.put("2015-06-22", "1");
//                    Dtrans.put("2015-06-23", "2");
//                    Dtrans.put("2015-06-24", "3");
//                    Dtrans.put("2015-06-25", "4");
//                    Dtrans.put("2015-06-26", "5");
//                    Dtrans.put("2015-06-27", "6");
//                    Dtrans.put("2015-06-28", "7");
//                    Dtrans.put("2015-06-29", "8");
//
//                    Hashtable<String, String> Datetrans = new Hashtable<String, String>();
//                    Datetrans.put("2015-06-22", "Monday, Jun.22");
//                    Datetrans.put("2015-06-23", "Tuesday, Jun.23");
//                    Datetrans.put("2015-06-24", "Wednesday, Jun.24");
//                    Datetrans.put("2015-06-25", "Thursday, Jun.25");
//                    Datetrans.put("2015-06-26", "Friday, Jun.26");
//                    Datetrans.put("2015-06-27", "Saturday, Jun.27");
//                    Datetrans.put("2015-06-28", "Sunday, Jun.28");
//                    Datetrans.put("2015-06-29", "Monday, Jun.29");
//                    ke.date = Datetrans.get(str);
//                    ke.dayid= Dtrans.get(str);
//                    state=0;
//                    break;
//                case 3:
//                    ke.beginTime = content;
//                    state=0;
//                    break;
//                case 4:
//
//                    ke.endTime = content;
//                    state=0;
//                    break;
//                case 5:
//                    ke.room = content;
//                    state=0;
//                    break;
//
//                case 6:
//                    ke.speakerName=content;
//                    state=0;
//                    break;
//
//                case 7:
//                    if("Keynote".equals(content)){
//                        isKeynote=true;
//                    }else
//                        isKeynote=false;
//                    state=0;
//                    break;
//                case 8:
//
//                    ke.ID=content;
//                    state=0;
//                    break;
//
//                case 9:
//
//                    ke.description=KAP.getKeynoteAbstract(content);
//                    state=0;
//                    break;
//
//                default:
//                    state=0;
//                    return;
//            }
//        }
//    }
//
//
//    public class KeynoteAbstractParser {
//
//        public String getKeynoteAbstract(String ID){
//            String ab="";
//            try {
//                URL url = new URL("http://halley.exp.sis.pitt.edu/cn3mobile/contentAbstract.jsp?contentID="+ID);
//
//                InputStream in = url.openStream();
//                String data = convertToString(in);
//
//                ab=data;
//
//                in.close();
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return ab;
//        }
//        public String convertToString(InputStream is){
//            if (is != null) {
//                StringBuilder sb = new StringBuilder();
//                String line;
//                try {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line).append(" ");
//                    }
//                }
//                catch(Exception e)
//                {
//                    System.out.print(e.getMessage());
//                }
//                finally {
//                    try{
//                        is.close();
//                    }
//                    catch(Exception e)
//                    {
//
//                    }
//                }
//                return sb.toString();
//            } else {
//                return "";
//            }
//        }
//    }
}
