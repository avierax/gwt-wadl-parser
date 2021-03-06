package cu.tissca.x901.wad;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import cu.tissca.x901.wad.model.ResponseDescriptor;
import org.junit.Test;

/**
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class GwtTest_MethodParser_Response extends GWTTestCase {

    static String SAMPLE_RESPONSE = "<ns2:response xmlns:ns2=\"http://wadl.dev.java.net/2009/02\"\n" +
            "        status=\"200\">\n" +
            "    <ns2:doc><![CDATA[a set of worklogs id and update time.]]></ns2:doc>\n" +
            "    <ns2:representation\n" +
            "            mediaType=\"application/json\">\n" +
            "        <ns2:doc>\n" +
            "            <ns3:p\n" +
            "                    xmlns:ns3=\"http://www.w3.org/1999/xhtml\">\n" +
            "                <ns3:h6>Example</ns3:h6>\n" +
            "                <ns3:pre>\n" +
            "                    <ns3:code>\n" +
            "                        {\"values\":[{\"worklogId\":103,\"updatedTime\":1438013671562},{\"worklogId\":104,\"updatedTime\":1438013672165},{\"worklogId\":105,\"updatedTime\":1438013693136}],\"since\":1438013671562,\"until\":1438013693136,\"self\":\"http://www.example.com/jira/worklog/updated?since=1438013671136\",\"nextPage\":\"http://www.example.com/jira/worklog/updated/updated?since=1438013671136&amp;since=1438013693136\",\"lastPage\":true}\n" +
            "                    </ns3:code>\n" +
            "                </ns3:pre>\n" +
            "            </ns3:p>\n" +
            "        </ns2:doc>\n" +
            "        <ns2:doc><![CDATA[Returns a JSON representation of the worklog changes.]]></ns2:doc>\n" +
            "        <ns2:doc>\n" +
            "            <ns3:p\n" +
            "                    xmlns:ns3=\"http://www.w3.org/1999/xhtml\">\n" +
            "                <ns3:h6>Schema</ns3:h6>\n" +
            "                <ns3:pre>\n" +
            "                    <ns3:code>\n" +
            "                        {\"id\":\"https://docs.atlassian.com/jira/REST/schema/worklog-changed-since#\",\"title\":\"Worklog\n" +
            "                        Changed\n" +
            "                        Since\",\"type\":\"object\",\"properties\":{\"values\":{\"type\":\"array\",\"items\":{\"title\":\"Worklog\n" +
            "                        Change\",\"type\":\"object\",\"properties\":{\"worklogId\":{\"type\":\"integer\"},\"updatedTime\":{\"type\":\"integer\"}},\"additionalProperties\":false}},\"since\":{\"type\":\"integer\"},\"until\":{\"type\":\"integer\"},\"isLastPage\":{\"type\":\"boolean\"},\"self\":{\"type\":\"string\",\"format\":\"uri\"},\"nextPage\":{\"type\":\"string\",\"format\":\"uri\"}},\"additionalProperties\":false,\"required\":[\"isLastPage\"]}\n" +
            "                    </ns3:code>\n" +
            "                </ns3:pre>\n" +
            "            </ns3:p>\n" +
            "        </ns2:doc>\n" +
            "    </ns2:representation>\n" +
            "</ns2:response>\n";

    @Test
    public void test_response_docs() throws MalformedWadlException {
        MethodParser parser = new MethodParser("ns2");
        Element responseElement = XMLParser.parse(SAMPLE_RESPONSE).getDocumentElement();
        ResponseDescriptor responseDescriptor = parser.parseResponse(responseElement);
        assertTrue(responseDescriptor.getDocs().get(0).getElement().toString().contains("a set of worklogs id and update time"));
    }

    @Test
    public void test_response_status() throws MalformedWadlException {
        MethodParser parser = new MethodParser("ns2");
        Element responseElement = XMLParser.parse(SAMPLE_RESPONSE).getDocumentElement();
        ResponseDescriptor responseDescriptor = parser.parseResponse(responseElement);
        assertEquals(Integer.valueOf(200), responseDescriptor.getStatus());
    }

    @Test
    public void test_response_representation() throws MalformedWadlException {
        Document responseDocument = XMLParser.parse(SAMPLE_RESPONSE);
        MethodParser wadlParser = new MethodParser("ns2");
        ResponseDescriptor responseDescriptor = wadlParser.parseResponse(responseDocument.getDocumentElement());
        assertTrue(responseDescriptor.getRepresentation()!=null);
    }

    @Override
    public String getModuleName() {
        return "cu.tissca.x901.wad.WadlParserTest";
    }
}
