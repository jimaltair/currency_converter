package ru.jimaltair.currencyconverter.service;

import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map;
import java.util.TreeMap;

@Component
public class CbrXmlReader {

    @PostConstruct
    public Map<String, String> getXML() {
        Map<String, String> valute = new TreeMap<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("http://www.cbr.ru/scripts/XML_daily.asp");

            NodeList nodeList = document.getChildNodes();
            for (int x = 0, size = 34; x < size; x++) {
                valute.put(nodeList.item(0).getChildNodes().item(x).getChildNodes().item(3).getTextContent(), nodeList.item(0).getChildNodes().item(x).getChildNodes().item(4).getTextContent());
            }
            // выводим список валют и курсы в консоль для проверки корректности парсинга
            for (Map.Entry<String, String> entry : valute.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (Exception ignored) {
        }
        return valute;
    }
}
