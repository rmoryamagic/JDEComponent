package io.elastic.jdee1;

import com.jdedwards.system.xml.XMLRequest;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Utils {

  private static final Logger logger = LoggerFactory.getLogger(Utils.class);

  private Document XMLDoc;
  private Document XMLResponseDoc;
  private Document XMLServer;

  public static final String CFG_USER = "user";
  public static final String CFG_PASSWORD = "password";
  public static final String CFG_ENV = "environment";
  public static final String CFG_FUNCTION = "function"; // function name for verification @ creds
  public static final String CFG_SERVER = "server";
  public static final String CFG_PORT = "port";
  public static final String SESSION = "session";
  public static final String FUNCTION_NAME = "name"; // function name @ runtime processing
  public static final String ERROR_MESSAGE = "error_message";
  public static final String ERROR_CODE = "error_code";
  public static final String RESULT = "result";

  HashMap<Integer, String> errorReturnCodes = new HashMap<Integer, String>() {{
    put(1, "root XML element is not a jdeRequest or jdeResponse.");
    put(2,
        "jdeRequest user identification unknown. Check the user, pwd and environment attributes.");
    put(3, "XML Parse error at line.");
    put(4, "Fatal XML Parse error at line.");
    put(5, "Error during parser initialization, server not configured correctly.");
    put(6, "Unknown parse error.");
    put(7, "Request session attribute is invalid.");
    put(8, "Request type attribute is invalid.");
    put(9, "Request type attribute not given.");
    put(10,
        "Request session attribute is invalid, referenced process 'processid' no longer exists.");
    put(11, "jdeRequest child element is invalid or unknown.");
    put(12,
        "Environment 'Env name' could not be initialized for user, check user, pwd and environment attribute values.");
    put(13, "jdeXMLRequest parameter invalid");
    put(14, "Connection to OneWorld failed");
    put(15, "jdeXMLRequest send failed");
    put(16, "jdeXMLResponse receive failed");
    put(17, "jdeXMLResponse memory allocation failed");
    put(99, "invalid BSFN Name");
  }};

  public String session = "";
  public Boolean executed = false;
  public String returnCode = "-1";
  public String errors = "";

  String[] columnNames = new String[]{"Parameter", "Input Value", "Output Value"};
  Object[][] Parameters = new Object[][]{{"", "", ""}};
  public DefaultTableModel BSFNParmsModel = new DefaultTableModel(Parameters, columnNames);

  private String getSessionIDFromXMLDocument(Document doc) {
    String ret = null;

    try {
      NodeList returncodelist = doc.getElementsByTagName("jdeResponse");
      Node returncode = returncodelist.item(0);
      NamedNodeMap attributes = returncode.getAttributes();
      Node att = attributes.getNamedItem("session");
      ret = att.getNodeValue();
    } catch (Exception var7) {
      ret = "";
    }

    return ret;
  }

  public JsonObject getTemplate_actionPerformed(final JsonObject config, final JsonObject snapshot,
      final JsonObject body) {
    JsonObjectBuilder properties = Json.createObjectBuilder();
    JsonObjectBuilder field = Json.createObjectBuilder();
    JsonObjectBuilder result = Json.createObjectBuilder();
    executed = false;
    String response = "";

    String function = getFunction(config, snapshot, body);
    logger.info("Function: {}", function);

    if (function.compareTo("") != 0) {
      clearBSFNModel();
      errors = "";
      Node node = null;

      logger.info("Config: {}", config.toString());

      try {
        node = createTemplateRequestXMLDocument(config, snapshot, body);
      } catch (ParserConfigurationException var12) {
        errors = "Failed to create XML document for get template.\n" + var12.toString();
        logger.info("Error: {}", errors);
        properties.add(Utils.ERROR_CODE, returnCode);
        properties.add(Utils.ERROR_MESSAGE, errors);
        properties.add(Utils.RESULT, result.build());
        return properties.build();
      }

      try {
        response = executeXMLRequest(config, node);
      } catch (Exception var11) {
        errors = "Failed to Execute XML request for get template.\n" + var11.toString();
        logger.info("Error: {}", errors);
        properties.add(Utils.ERROR_CODE, returnCode);
        properties.add(Utils.ERROR_MESSAGE, errors);
        properties.add(Utils.RESULT, result.build());
        return properties.build();
      }

      try {
        XMLDoc = convertStringToXMLDocument(response);
      } catch (Exception var10) {
        errors = "Failed to convert response to XML document.\n" + var10.toString();
        logger.info("Error: {}", errors);
        properties.add(Utils.ERROR_CODE, returnCode);
        properties.add(Utils.ERROR_MESSAGE, errors);
        properties.add(Utils.RESULT, result.build());
        return properties.build();
      }

      String ret = getReturnCodeFromXMLDocument(XMLDoc);
      if (ret.compareTo("99") == 0) {
        errors = "Failed to retrieve template\n";
        showTemplateErrorMessage(XMLDoc);
      } else {
        NodeList parms = XMLDoc.getElementsByTagName("param");

        for (int i = 0; i < parms.getLength(); ++i) {
          NamedNodeMap attributes = parms.item(i).getAttributes();
          Node x = attributes.getNamedItem("name");
          String[] row = new String[]{x.getNodeValue(), "", ""};

          String name = x.getNodeValue();
          String type = "string";
          field.add("title", name)
              .add("type", type);
          properties.add(name, field);

          BSFNParmsModel.addRow(row);
        }

        executed = true;
      }
    }
    return properties.build();
  }

  private String executeXMLRequest(final JsonObject config, Node node)
      throws UnsupportedEncodingException, IOException {
    String request = convertXMLDocumentToString(node);
    
    final String server = getRequiredNonEmptyString(config, CFG_SERVER, "Server is required");
    final String port = getRequiredNonEmptyString(config, CFG_PORT, "Port is required");
    XMLRequest xml = new XMLRequest(server, Integer.parseInt(port), request);
    String response = xml.execute();
   logger.info("Response Log: {}", response);

    return response;
  }

  private Node createTemplateRequestXMLDocument(final JsonObject config, final JsonObject snapshot,
      final JsonObject body)
      throws ParserConfigurationException {
    Node node = null;
    final String user = getRequiredNonEmptyString(config, CFG_USER, "User is required");
    final String password = getRequiredNonEmptyString(config, CFG_PASSWORD, "Password is required");
    final String environment = getRequiredNonEmptyString(config, CFG_ENV,
        "Environment is required");
    final String function = getFunction(config, snapshot, body);
    DocumentBuilder Builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document Doc = Builder.newDocument();
    Element element = Doc.createElement("jdeRequest");
    element.setAttribute("type", "callmethod");
    element.setAttribute("user", user);
    element.setAttribute("pwd", password);
    element.setAttribute("environment", environment);
    element.setAttribute("session", session);
    Doc.appendChild(element);
    Element element2 = Doc.createElement("callMethodTemplate");
    element2.setAttribute("app", "");
    element2.setAttribute("name", function);
    element.appendChild(element2);
    return Doc;
  }

  private String getReturnCodeFromXMLDocument(Document doc) {
    String ret = null;

    try {
      NodeList returncodelist = doc.getElementsByTagName("returnCode");
      Node returncode = returncodelist.item(0);
      NamedNodeMap attributes = returncode.getAttributes();
      Node att = attributes.getNamedItem("code");
      ret = att.getNodeValue();
    } catch (Exception var7) {
      ret = "";
    }

    return ret;
  }

  private Document convertStringToXMLDocument(String XML)
      throws ParserConfigurationException, SAXException, IOException {
    Document doc = null;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(XML));
    doc = db.parse(is);
    return doc;
  }


  private String convertXMLDocumentToString(Node node) {
    try {
      Source source = new DOMSource(node);
      StringWriter stringWriter = new StringWriter();
      Result result = new StreamResult(stringWriter);
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.transform(source, result);
      return stringWriter.getBuffer().toString();
    } catch (Exception var7) {
      return var7.toString();
    }
  }

  private void clearBSFNModel() {
    int j = BSFNParmsModel.getRowCount();

    for (int i = 0; i < j; ++i) {
      BSFNParmsModel.removeRow(0);
    }
  }

  private void showTemplateErrorMessage(Document doc) {
    String ret = null;

    try {
      NodeList returncodelist = doc.getElementsByTagName("returnCode");
      Node returncode = returncodelist.item(0);
      ret = returncode.getFirstChild().getNodeValue();
    } catch (Exception var5) {
      ret = "";
    }

    errors = ret;
  }

  public JsonObject jbExecute_actionPerformed(JsonObject config, JsonObject body,
      JsonObject snapshot)
      throws ParserConfigurationException, IOException, SAXException, NumberFormatException {
    errors = "";

    String ret;
    JsonObjectBuilder properties = Json.createObjectBuilder();
    JsonObjectBuilder field = Json.createObjectBuilder();
    JsonObjectBuilder result = Json.createObjectBuilder();

    getTemplate_actionPerformed(config, snapshot, body);

    StringBuilder keys = new StringBuilder();
    StringBuilder values = new StringBuilder();
    StringBuilder setString = new StringBuilder();
    int indexParam = 0;

    for (Map.Entry<String, JsonValue> entry : body.entrySet()) {
      if (entry.getValue().toString().compareTo("") != 0) {
        setParameterValue(entry.getKey().toString(), ((JsonString) entry.getValue()).getString());
      }
    }

    setCredentialsInXMLDocument(config, snapshot, false);
    String response = null;

    try {
      response = executeXMLRequest(config, XMLDoc);
    } catch (IOException var13) {
      errors = "Failed to Execute XMLRequest for function call.\n" + var13.toString();
      properties.add(Utils.ERROR_CODE, returnCode);
      properties.add(Utils.ERROR_MESSAGE, errors);
      properties.add(Utils.RESULT, result.build());
      return properties.build();
    }

    try {
      XMLResponseDoc = convertStringToXMLDocument(response);
    } catch (Exception var12) {
      errors = "Failed to Execute XMLRequest for function call.\n" + var12.toString();
      var12.printStackTrace();
      properties.add(Utils.ERROR_CODE, returnCode);
      properties.add(Utils.ERROR_MESSAGE, errors);
      properties.add(Utils.RESULT, result.build());
      return properties.build();
    }

    ret = getReturnCodeFromXMLDocument(XMLResponseDoc);
    returnCode = ret;
    logger.info("Response: {}", response);
    logger.info("returnCode: {}", returnCode);

    if (!returnCode.equals("") && (Integer.parseInt(returnCode) == 7
        || Integer.parseInt(returnCode) == 10)) {

      logger.info("Session is invalid. Error message: \"{}\". Try to get a new session",
          errorReturnCodes.get(Integer.parseInt(returnCode)));

      setCredentialsInXMLDocument(config, snapshot, true);
      response = null;

      try {
        response = executeXMLRequest(config, XMLDoc);
      } catch (IOException var13) {
        errors = "Failed to Execute XMLRequest for function call.\n" + var13.toString();
        properties.add(Utils.ERROR_CODE, returnCode);
        properties.add(Utils.ERROR_MESSAGE, errors);
        properties.add(Utils.RESULT, result.build());
        return properties.build();
      }

      try {
        XMLResponseDoc = convertStringToXMLDocument(response);
      } catch (Exception var12) {
        errors = "Failed to Execute XMLRequest for function call.\n" + var12.toString();
        var12.printStackTrace();
        properties.add(Utils.ERROR_CODE, returnCode);
        properties.add(Utils.ERROR_MESSAGE, errors);
        properties.add(Utils.RESULT, result.build());
        return properties.build();
      }

      ret = getReturnCodeFromXMLDocument(XMLResponseDoc);
      returnCode = ret;
    }

    Node node = null;

    logger.info("Session: {}", session);
    if (!returnCode.equals("") && Integer.parseInt(returnCode) > 0) {
      errors = errorReturnCodes.get(Integer.parseInt(returnCode));
      logger.info("Error: {} - {}", returnCode, errors);
      NodeList errors = XMLResponseDoc.getElementsByTagName("error");
      if (errors.getLength() > 0) {
        for (int i = 0; i < errors.getLength(); ++i) {
          node = errors.item(i);
          String errorMessage = node.getTextContent();
          NamedNodeMap attributes = node.getAttributes();
          Node x = attributes.getNamedItem("code");
          String errorCode = x.getNodeValue();
          Node textNode = null;
          properties.add(ERROR_CODE, errorCode);
          properties.add(ERROR_MESSAGE, errorMessage);
          properties.add(RESULT, Json.createObjectBuilder().build());
          return properties.build();
        }
      }
    }

    NodeList parms = XMLResponseDoc.getElementsByTagName("param");
    for (int i = 0; i < parms.getLength(); ++i) {
      node = parms.item(i);
      NamedNodeMap attributes = node.getAttributes();
      Node x = attributes.getNamedItem("name");
      String parmname = x.getNodeValue();
      Node textNode = null;
      if ((textNode = node.getFirstChild()) != null) {
        int index = getParameterIndexByName(parmname);
        if (index != -1) {
          BSFNParmsModel.setValueAt(textNode.getNodeValue(), index, 2);

          String name = x.getNodeValue();
          String type = "string";
          field.add("title", name)
              .add("type", type);
          result.add(name, textNode.getNodeValue());

        }
      }
    }
    properties.add(Utils.SESSION, session);
    properties.add(Utils.ERROR_CODE, returnCode);
    properties.add(Utils.ERROR_MESSAGE, "");
    properties.add(Utils.RESULT, result.build());

    XMLResponseDoc.getElementsByTagName("jdeResponse");

    displayBSFNErrors(XMLResponseDoc);

    return properties.build();
  }

  public void setParameterValue(String key, String value) {
    NodeList parms = XMLDoc.getElementsByTagName("param");
    for (int i = 0, len = parms.getLength(); i < len; i++) {
      Element elm = (Element) parms.item(i);
      if (elm.getAttribute("name").contains(key)) {
        Node node = parms.item(i);
        Node textNode = null;
        if ((textNode = node.getFirstChild()) == null) {
          textNode = XMLDoc.createTextNode(value);
          node.appendChild((Node) textNode);
        }

        if (textNode != null) {
          ((Node) textNode).setNodeValue(value);
        }
      }
    }
  }

  private void setCredentialsInXMLDocument(JsonObject config, JsonObject snapshot,
      boolean resetSession) {
    NodeList requestlist = XMLDoc.getElementsByTagName("jdeRequest");
    Node request = requestlist.item(0);

    String user = getUser(config, snapshot);
    String password = getPassword(config, snapshot);
    String env = getEnv(config, snapshot);
    String session = (resetSession) ? "" : getSession(snapshot);

    try {
      NamedNodeMap attributes = request.getAttributes();
      Node att = attributes.getNamedItem(Utils.CFG_USER);
      att.setNodeValue(user);
      att = attributes.getNamedItem("pwd");
      att.setNodeValue(password);
      att = attributes.getNamedItem(Utils.CFG_ENV);
      att.setNodeValue(env);
      att = attributes.getNamedItem(Utils.SESSION);
      att.setNodeValue(session);
    } catch (Exception var6) {
      var6.printStackTrace();
    }
  }

  private int getParameterIndexByName(String parmname) {
    for (int i = 0; i < this.BSFNParmsModel.getRowCount(); ++i) {
      String parm = null;
      parm = (String) this.BSFNParmsModel.getValueAt(i, 0);
      if (parm.compareTo(parmname) == 0) {
        return i;
      }
    }

    return -1;
  }

  private void displayBSFNErrors(Document doc) {
    String codestring = "";
    String message = "";

    try {
      NodeList errorlist = doc.getElementsByTagName("error");

      for (int i = 0; i < errorlist.getLength(); ++i) {
        Node error = errorlist.item(i);
        NamedNodeMap attributes = error.getAttributes();
        Node code = attributes.getNamedItem("code");
        if (code != null) {
          codestring = code.getNodeValue();
        }

        message = error.getFirstChild().getNodeValue();
        errors += codestring + " - " + message + "\n";
      }
    } catch (Exception var9) {
      errors += "Failed to get error";
    }

  }

  public static String documentToString(Document doc) {
    try {
      StringWriter sw = new StringWriter();
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      transformer.transform(new DOMSource(doc), new StreamResult(sw));
      return sw.toString();
    } catch (Exception ex) {
      throw new RuntimeException("Error converting to String", ex);
    }
  }

  public void addAttribute(Document doc, String name, String value) {
    Element root = doc.getDocumentElement();
    Element person = (Element) root.getFirstChild();
    person.setAttribute(name, value);
  }

  public static JsonObject removeProperty(JsonObject origin, String key) {
    JsonObjectBuilder builder = Json.createObjectBuilder();

    Integer index = 0;
    for (Map.Entry<String, JsonValue> entry : origin.entrySet()) {
      if (entry.getKey().equals(key)) {
        continue;
      } else {
        builder.add(entry.getKey(), entry.getValue());
      }
    }
    return builder.build();
  }

  public static String getNonNullString(final JsonObject config, final String key) {
    Object value = "";
    try {
      Set<Entry<String, JsonValue>> kvPairs = config.entrySet();
      for (Map.Entry<String, JsonValue> kvPair : kvPairs) {
        if (kvPair.getKey().equals(key)) {
          value = config.get(key);
        }
      }
    } catch (NullPointerException e) {
      logger.info("key {} doesn't have any mapping: {}", key, e);
      e.printStackTrace();
    } catch (ClassCastException e) {
      logger.info("key {} doesn't have any mapping: {}", key, e);
      e.printStackTrace();
    }
    return value.toString().replaceAll("\"", "");
  }

  private static String getRequiredNonEmptyString(final JsonObject config, final String key,
      final String message) {
    final String value = config.getString(key);
    if (value == null || value.isEmpty()) {
      throw new RuntimeException(message);
    }
    return value;
  }

  private static String getUser(JsonObject config, JsonObject snapshot) {
    String user = "";
    if (snapshot.containsKey(CFG_USER)
        && Utils.getNonNullString(snapshot, CFG_USER).length() != 0) {
      user = snapshot.getString(CFG_USER);
    } else if (config.containsKey(CFG_USER)
        && Utils.getNonNullString(config, CFG_USER).length() != 0) {
      user = config.getString(CFG_USER);
    } else {
      throw new RuntimeException("Username is required");
    }
    return user;
  }

  private static String getPassword(JsonObject config, JsonObject snapshot) {
    String password = "";
    if (snapshot.containsKey(CFG_PASSWORD)
        && Utils.getNonNullString(snapshot, CFG_PASSWORD).length() != 0) {
      password = snapshot.getString(CFG_PASSWORD);
    } else if (config.containsKey(CFG_PASSWORD)
        && Utils.getNonNullString(config, CFG_PASSWORD).length() != 0) {
      password = config.getString(CFG_PASSWORD);
    } else {
      throw new RuntimeException("Password is required");
    }
    return password;
  }

  private static String getEnv(JsonObject config, JsonObject snapshot) {
    String env = "";
    if (snapshot.containsKey(CFG_ENV)
        && Utils.getNonNullString(snapshot, CFG_ENV).length() != 0) {
      env = snapshot.getString(CFG_ENV);
    } else if (config.containsKey(CFG_ENV)
        && Utils.getNonNullString(config, CFG_ENV).length() != 0) {
      env = config.getString(CFG_ENV);
    } else {
      throw new RuntimeException("Environment is required");
    }
    return env;
  }

  private static String getFunction(JsonObject config, JsonObject snapshot, JsonObject body) {
    String function = "";
    if (body.containsKey(CFG_FUNCTION)
        && Utils.getNonNullString(body, CFG_FUNCTION).length() != 0) {
      function = config.getString(CFG_FUNCTION);
    } else if (snapshot.containsKey(CFG_FUNCTION)
        && Utils.getNonNullString(snapshot, CFG_FUNCTION).length() != 0) {
      function = snapshot.getString(CFG_FUNCTION);
    } else if (config.containsKey(FUNCTION_NAME)
        && Utils.getNonNullString(config, FUNCTION_NAME).length() != 0) {
      function = config.getString(FUNCTION_NAME);
    } else if (config.containsKey(CFG_FUNCTION)
        && Utils.getNonNullString(config, CFG_FUNCTION).length() != 0) {
      function = config.getString(CFG_FUNCTION);
    } else {
      throw new RuntimeException("Function is required");
    }
    return function;
  }

  private static String getServer(JsonObject config, JsonObject snapshot) {
    String server = "";
    if (snapshot.containsKey(CFG_SERVER)
        && Utils.getNonNullString(snapshot, CFG_SERVER).length() != 0) {
      server = snapshot.getString(CFG_SERVER);
    } else if (config.containsKey(CFG_SERVER)
        && Utils.getNonNullString(config, CFG_SERVER).length() != 0) {
      server = config.getString(CFG_SERVER);
    } else {
      throw new RuntimeException("Server is required");
    }
    return server;
  }

  private static String getPort(JsonObject config, JsonObject snapshot) {
    String port = "";
    if (snapshot.containsKey(CFG_PORT)
        && Utils.getNonNullString(snapshot, CFG_PORT).length() != 0) {
      port = snapshot.getString(CFG_PORT);
    } else if (config.containsKey(CFG_PORT)
        && Utils.getNonNullString(config, CFG_PORT).length() != 0) {
      port = config.getString(CFG_PORT);
    } else {
      throw new RuntimeException("Port is required");
    }
    return port;
  }

  private static String getSession(JsonObject snapshot) {
    String session = "";
    if (snapshot.containsKey("session")
        && Utils.getNonNullString(snapshot, SESSION).length() != 0) {
      session = snapshot.getString(SESSION);
    }
    return session;
  }

}