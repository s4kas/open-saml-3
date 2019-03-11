import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.w3c.dom.Element;

public class OpenSAMLUtils {

	@SuppressWarnings("unchecked")
	public static <T> T buildSAMLObject(final Class<T> clazz)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
		QName defaultElementName = (QName) clazz.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);
		T object = (T) builderFactory.getBuilder(defaultElementName).buildObject(defaultElementName);
		return object;
	}

	public static String toString(XMLObject xmlObject) throws MarshallingException, TransformerException {
		Element element = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(xmlObject)
				.marshall(xmlObject);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(element);
		StreamResult result = new StreamResult(new StringWriter());

		transformer.transform(source, result);

		return result.getWriter().toString();
	}
}
