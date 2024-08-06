package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Task3 {

    private static final Logger logger = Logger.getLogger(Task3.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Запрашиваем путь к директориям у пользователя
        System.out.print("Введите путь к директории, содержащей файл values.json: ");
        String valuesDirPath = scanner.nextLine();
        System.out.print("Введите путь к директории, содержащей файл tests.json: ");
        String testsDirPath = scanner.nextLine();
        System.out.print("Введите путь к директории, где будет создан файл report.json: ");
        String reportDirPath = scanner.nextLine();

        File valuesDir = new File(valuesDirPath);
        File testsDir = new File(testsDirPath);
        File reportDir = new File(reportDirPath);

        // Проверяем, что все предоставленные пути являются директориями
        if (!valuesDir.isDirectory()) {
            logger.severe("The provided path for values.json is not a directory: " + valuesDirPath);
            System.exit(1);
        }

        if (!testsDir.isDirectory()) {
            logger.severe("The provided path for tests.json is not a directory: " + testsDirPath);
            System.exit(1);
        }

        if (!reportDir.isDirectory()) {
            logger.severe("The provided path for report.json is not a directory: " + reportDirPath);
            System.exit(1);
        }

        // Определяем пути к файлам
        File valuesFile = new File(valuesDir, "values.json");
        File testsFile = new File(testsDir, "tests.json");
        File reportFile = new File(reportDir, "report.json");

        // Проверяем доступность файлов
        if (!checkFileAccess(valuesFile)) {
            logger.severe("Access denied or file does not exist: " + valuesFile.getPath());
            System.exit(1);
        }

        if (!checkFileAccess(testsFile)) {
            logger.severe("Access denied or file does not exist: " + testsFile.getPath());
            System.exit(1);
        }

        // Создаем объект ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Чтение данных из файлов
            JsonNode valuesNode = objectMapper.readTree(valuesFile);
            JsonNode testsNode = objectMapper.readTree(testsFile);

            // Обработка данных
            JsonNode reportNode = fillValues(testsNode, valuesNode, objectMapper);

            // Запись результата в файл
            if (writeToFile(reportFile, reportNode, objectMapper)) {
                System.out.println("Report generated successfully at " + reportFile.getPath());
            } else {
                logger.severe("Failed to write to file: " + reportFile.getPath());
                System.exit(1);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while processing the files", e);
        } finally {
            scanner.close();
        }
    }

    // Метод для проверки доступа к файлу
    private static boolean checkFileAccess(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }

    // Метод для записи результата в файл
    private static boolean writeToFile(File file, JsonNode reportNode, ObjectMapper objectMapper) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, reportNode);
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write to file: " + file.getPath(), e);
            return false;
        }
    }

    // Метод для заполнения значений в структуре
    private static JsonNode fillValues(JsonNode testsNode, JsonNode valuesNode, ObjectMapper objectMapper) {
        // Создаем копию структуры testsNode
        ObjectNode resultNode = (ObjectNode) objectMapper.createObjectNode();
        resultNode.setAll((ObjectNode) testsNode);

        // Итерация по узлам структуры tests
        for (Iterator<Map.Entry<String, JsonNode>> fields = testsNode.fields(); fields.hasNext(); ) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();

            if (fieldValue.isObject()) {
                // Рекурсивно обрабатываем вложенные объекты
                resultNode.set(fieldName, fillValues(fieldValue, valuesNode, objectMapper));
            } else if ("value".equals(fieldName)) {
                // Заполняем значение из valuesNode
                String id = fieldValue.asText();
                JsonNode valueNode = valuesNode.get(id);
                if (valueNode != null) {
                    resultNode.set("value", valueNode);
                }
            }
        }

        return resultNode;
    }
}
