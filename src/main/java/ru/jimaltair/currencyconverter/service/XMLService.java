package ru.jimaltair.currencyconverter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class XMLService implements XMLReader {

    private static String CBR_XML_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    @PostConstruct
    public void readAndPrintXml() {
        Document xml = readXML(CBR_XML_URL);
        tempXmlPrintMethod(xml);
    }

    @Override
    public Document readXML(String URL) {
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
        tempXmlPrintMethod(document);
        return document;
    }

    public void tempXmlPrintMethod(Document document) {
        Map<String, String> valute = new TreeMap<>();
        NodeList nodeList = document.getChildNodes();
        for (int x = 0, size = 34; x < size; x++) {
            valute.put(nodeList.item(0).getChildNodes().item(x).getChildNodes().item(3).getTextContent(),
                    nodeList.item(0).getChildNodes().item(x).getChildNodes().item(4).getTextContent());
        }
        // выводим список валют и курсы в консоль для проверки корректности парсинга
        for (Map.Entry<String, String> entry : valute.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
