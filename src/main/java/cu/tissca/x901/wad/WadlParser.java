package cu.tissca.x901.wad;

import com.google.common.base.Verify;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import cu.tissca.x901.wad.model.ResourceDescriptor;
import cu.tissca.x901.wad.model.WebApplicationDescriptor;
import cu.tissca.x901.wad.xmlutils.NsHelper;

/**
 *
 * Methods in this parser are lenient. In the sense that they will tolerate failure.
 *
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class WadlParser extends AbstractParser {

    private final MethodParser methodParser;

    public WadlParser(String wadlPrefix) {
        super(wadlPrefix);
        this.methodParser = new MethodParser(wadlPrefix);
    }

    public static WebApplicationDescriptor parse(String xml) throws MalformedWadlException {
        Document document = XMLParser.parse(xml);
        String wadlPrefix = null;
        if(NsHelper.hasNamespaces(document.getDocumentElement())) {
            wadlPrefix = NsHelper.getNamespacePrefix(document.getDocumentElement(), "http://wadl.dev.java.net/2009/02");
        }
        WadlParser parser = new WadlParser(wadlPrefix);
        parser.setWadlPrefix(wadlPrefix);
        return parser.parseApplication(document.getDocumentElement());
    }

    @Lenient
    public WebApplicationDescriptor parseApplication(Element documentElement) throws MalformedWadlException {
        WebApplicationDescriptor result = new WebApplicationDescriptor();
        Verify.verify(false, "Should not use getElementsByTagName because it's recursive");
        NodeList resourcessNodeList = documentElement.getElementsByTagName(tagName("resources"));
        if(resourcessNodeList.getLength()==1){
            Element resourcesNode = (Element) resourcessNodeList.item(0);
            Verify.verify(false, "Should not use getElementsByTagName because it's recursive");
            NodeList resourcesNodeList = resourcesNode.getElementsByTagName(tagName("resource"));
            for(int i=0;i<resourcesNodeList.getLength();i++) {
                Element resourceElement = (Element) resourcesNodeList.item(i);
                try {
                    result.addResource(parseResource(resourceElement));
                } catch (Exception e){
                    result.addError(e);
                }
            }
            return result;
        } else {
            throw new MalformedWadlException("No resources found");
        }
    }

    @Lenient
    public ResourceDescriptor parseResource(Element resourceNode) {
        assert resourceNode instanceof Element;
        ResourceDescriptor result = new ResourceDescriptor();
        String base = resourceNode.getAttribute("base");
        NodeList resourcesNodeList = resourceNode.getElementsByTagName(tagName("resource"));
        for(int i=0;i<resourcesNodeList.getLength();i++) {
            Element childResourceNode = (Element) resourcesNodeList.item(i);
            try {
                result.addResource(parseResource(childResourceNode));
            } catch (Exception e){
                result.addError(e);
            }
        }
        NodeList methodNodeList = resourceNode.getElementsByTagName(tagName("method"));
        for(int i=0;i<methodNodeList.getLength();i++) {
            Element methodNode = (Element) methodNodeList.item(i);
            try {
                result.addMethod(this.methodParser.parseMethod(methodNode));
            } catch (Exception e) {
                result.addError(e);
            }
        }
        result.setBase(base);
        return result;
    }
}