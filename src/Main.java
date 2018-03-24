import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	
	public static void main(String[] args) throws TransformerException {
		
		Properties prop = new Properties();
		try {
			prop.load(new InputStreamReader(new FileInputStream("wx_request.prop"), Charset.forName("utf-8")));
			
			Iterator<Object> it = prop.keySet().iterator();
			TreeMap<String, String> result = new TreeMap<String, String>();
			while(it.hasNext()) {
				String key = it.next().toString();
				result.put(key, prop.getProperty(key));
			}
			
			StringBuilder sb = new StringBuilder();
			Iterator<String> it1 = result.keySet().iterator();
			while(it1.hasNext()) {
				String key = it1.next();
				String value = result.get(key);
				if(value == null || value.trim().equals("")) {
					continue;
				}
				sb.append(key+"="+value+"&");
			}
			
//			sb.deleteCharAt(sb.length() - 1);
			
			sb.append("key=ec55a9fb7ece4c2ca620be44a774eed9");
			
			
			
			String strSign = MD5Util.MD5Encode(sb.toString(), "utf-8");
			
			System.out.println("sssssssssssssss=="+strSign);
			
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			document.setXmlStandalone(true);
			Element xml = document.createElement("xml");
			document.appendChild(xml);
			
			Iterator<String> it2 = result.keySet().iterator();
			while(it2.hasNext()) {
				String key = it2.next();
				String value = result.get(key);
				
				Element element = document.createElement(key);
				element.setTextContent(value);
				
				xml.appendChild(element);
			}
			
			Element sign = document.createElement("sign");
			sign.setTextContent(strSign);
			xml.appendChild(sign);
			
			// 创建TransformerFactory对象
			TransformerFactory tff = TransformerFactory.newInstance();
			// 创建Transformer对象
			Transformer tf = tff.newTransformer();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			// 使用Transformer的transform()方法将DOM树转换成XML
			tf.transform(new DOMSource(document), new StreamResult(new FileOutputStream("e:/text.xml")));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
