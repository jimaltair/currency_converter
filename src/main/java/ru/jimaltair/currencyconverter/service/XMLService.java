package ru.jimaltair.currencyconverter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.XMLContent;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XMLService {

    private static String CBR_XML_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String ROOT_TAG = "ValCurs";
    private static final String VALUTE_TAG = "Valute";
    private static final String DATE_ATTRIBUTE_NAME = "Date";

    @PostConstruct
    public void readAndPrintXml() {
        Document xmlDocument = readXML(CBR_XML_URL);
//        tempXmlPrintMethod(xmlDocument);
        XMLContent xmlContent = retrieveContentFromXML(xmlDocument);
    }

    private XMLContent retrieveContentFromXML(Document xmlDocument) {
        xmlDocument.getDocumentElement().normalize();
        NodeList nodeList = xmlDocument.getElementsByTagName(ROOT_TAG);
        Node rootNode = nodeList.item(0);

        // получаем текущую дату из аттрибута корневой ноды
        String date = rootNode.getAttributes().getNamedItem(DATE_ATTRIBUTE_NAME).getTextContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate currentDate = LocalDate.parse(date, formatter);

        // получаем лист, заполненный объектами типа Currency через стрим по листу нод
        NodeList currenciesNodesList = xmlDocument.getElementsByTagName(VALUTE_TAG);
        List<Node> intermediateList = new ArrayList<>();
        for (int i = 0; i < currenciesNodesList.getLength(); i++) {
            intermediateList.add(currenciesNodesList.item(i));
        }
        List<Currency> currenciesList = intermediateList.stream().
                map(nodeMapper).
                collect(Collectors.toList());


        return new XMLContent();
    }

    Function<Node, Currency> nodeMapper = node -> {
        Element element = (Element) node;
        String numCode = element.getElementsByTagName("NumCode").item(0).getTextContent();
        String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent();
        int nominal = Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getTextContent());
        String name = element.getElementsByTagName("Name").item(0).getTextContent();
        return new Currency(numCode, charCode, nominal, name);
    };

    private Document readXML(String URL) {
        log.info("Start to read xml from {}", URL);
        Document document;
        try {
            document = DocumentBuilderFactory.
                    newInstance().
                    newDocumentBuilder().
                    parse(URL);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error("Error while reading or parsing xml");
            throw new IllegalStateException(e);
        }
        log.info("Xml document is ready");
        return document;
    }

    // временный метод - печать курсов в консоль для проверки корректности парсинга
    private void tempXmlPrintMethod(Document document) {
        Map<String, String> valute = new TreeMap<>();
        NodeList nodeList = document.getChildNodes();
        for (int x = 0, size = 34; x < size; x++) {
            valute.put(nodeList.item(0).getChildNodes().item(x).getChildNodes().item(3).getTextContent(),
                    nodeList.item(0).getChildNodes().item(x).getChildNodes().item(4).getTextContent());
        }
        for (Map.Entry<String, String> entry : valute.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
