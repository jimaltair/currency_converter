package ru.jimaltair.currencyconverter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.CurrencyRate;
import ru.jimaltair.currencyconverter.entity.XMLContent;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XMLService {

    private static final String CBR_XML_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String ROOT_TAG = "ValCurs";
    private static final String VALUTE_TAG = "Valute";
    private static final String NUM_CODE_TAG = "NumCode";
    private static final String CHAR_CODE_TAG = "CharCode";
    private static final String NOMINAL_TAG = "Nominal";
    private static final String NAME_TAG = "Name";
    private static final String VALUE_TAG = "Value";
    private static final String DATE_ATTRIBUTE_NAME = "Date";
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static LocalDate currentDate;

    public static XMLContent initXMLService() {
        Document xmlDocument = readXML(CBR_XML_URL);
        currentDate = getCurrentDateFromXML(xmlDocument);
        return retrieveContentFromXML(xmlDocument);
    }

    private static Document readXML(String URL) {
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

    private static LocalDate getCurrentDateFromXML(Document xmlDocument) {
        log.debug("Trying to get current date from xml {}", xmlDocument.getDocumentURI());

        xmlDocument.getDocumentElement().normalize();
        NodeList nodeList = xmlDocument.getElementsByTagName(ROOT_TAG);
        Node rootNode = nodeList.item(0);

        // получаем текущую дату из аттрибута корневой ноды
        String date = rootNode.getAttributes().getNamedItem(DATE_ATTRIBUTE_NAME).getTextContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(date, formatter);
    }

    private static XMLContent retrieveContentFromXML(Document xmlDocument) {
        log.debug("Trying to get xml content from xml {}", xmlDocument.getDocumentURI());

        NodeList currenciesNodesList = xmlDocument.getElementsByTagName(VALUTE_TAG);
        List<Node> intermediateList = new ArrayList<>();
        for (int i = 0; i < currenciesNodesList.getLength(); i++) {
            intermediateList.add(currenciesNodesList.item(i));
        }
        // получаем лист, заполненный объектами типа Currency через стрим по листу нод
        List<Currency> currenciesList = intermediateList.stream().
                map(XMLService::getCurrencyFromXMLNode).
                collect(Collectors.toList());
        // получаем лист, заполненный объектами типа CurrencyRate через стрим по листу нод
        List<CurrencyRate> currencyRatesList = intermediateList.stream()
                .map(nodeToCurrencyRateMapper)
                .collect(Collectors.toList());

        return new XMLContent(currenciesList, currencyRatesList);
    }

    private static Currency getCurrencyFromXMLNode(Node node) {
        Element element = (Element) node;
        String numCode = element.getElementsByTagName(NUM_CODE_TAG).item(0).getTextContent();
        String charCode = element.getElementsByTagName(CHAR_CODE_TAG).item(0).getTextContent();
        int nominal = Integer.parseInt(element.getElementsByTagName(NOMINAL_TAG).item(0).getTextContent());
        String name = element.getElementsByTagName(NAME_TAG).item(0).getTextContent();
        return new Currency(numCode, charCode, nominal, name);
    }

    static Function<Node, CurrencyRate> nodeToCurrencyRateMapper = node -> {
        Element element = (Element) node;
        double rate = Double.parseDouble(element.getElementsByTagName(VALUE_TAG).item(0).getTextContent().replace(',', '.'));
        return CurrencyRate.builder()
                .rate(rate)
                .date(currentDate)
                .currency(getCurrencyFromXMLNode(node))
                .build();

    };
}
