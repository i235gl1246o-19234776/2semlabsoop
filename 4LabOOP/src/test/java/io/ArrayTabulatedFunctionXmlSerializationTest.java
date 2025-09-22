package io;

import functions.ArrayTabulatedFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionXmlSerializationTest {

    private static final String TEST_FILE = "test_function.xml";

    @BeforeEach
    public void setUp() {
        // Удаляем файл перед каждым тестом, если существует
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        // Удаляем файл после каждого теста
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSerializeAndDeserializeXml() throws Exception {
        // Arrange: создаём тестовую функцию
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        // Act: сериализуем в XML-файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            FunctionsIO.serializeXml(writer, original);
        }

        // Проверяем, что файл создан
        assertTrue(Files.exists(Paths.get(TEST_FILE)), "Файл XML должен существовать после сериализации");

        // Act: десериализуем из файла
        ArrayTabulatedFunction restored;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            restored = FunctionsIO.deserializeXml(reader);
        }

        // Assert: проверяем, что данные совпадают
        assertNotNull(restored, "Восстановленный объект не должен быть null");
        assertEquals(original.getCount(), restored.getCount(), "Количество точек должно совпадать");

        for (int i = 0; i < original.getCount(); i++) {
            assertEquals(original.getX(i), restored.getX(i), 1e-9, "Значение X[" + i + "] должно совпадать");
            assertEquals(original.getY(i), restored.getY(i), 1e-9, "Значение Y[" + i + "] должно совпадать");
        }
    }

    @Test
    public void testSerializeAndDeserializeXml_EmptyFunction() throws Exception {
        double[] xValues = {5.0, 10.0};
        double[] yValues = {25.0, 100.0};
        ArrayTabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            FunctionsIO.serializeXml(writer, original);
        }

        ArrayTabulatedFunction restored;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            restored = FunctionsIO.deserializeXml(reader);
        }

        assertNotNull(restored, "Десериализованная функция не должна быть null");
        assertEquals(2, restored.getCount(), "Количество точек должно быть 1");
        assertEquals(5.0, restored.getX(0), 1e-9, "X значение должно совпадать");
        assertEquals(25.0, restored.getY(0), 1e-9, "Y значение должно совпадать");
    }

    @Test
    public void testDeserializeXml_FileNotFound() throws Exception {
        // Попытка прочитать из несуществующего файла
        assertThrows(IOException.class, () -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("nonexistent.xml"))) {
                FunctionsIO.deserializeXml(reader);
            }
        }, "Должно выбрасываться IOException для несуществующего файла");
    }

    @Test
    public void testSerializeXml_NullFunction() throws Exception {
        // Проверка, что сериализация null вызывает исключение (или обрабатывается)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            assertThrows(NullPointerException.class, () -> {
                FunctionsIO.serializeXml(writer, null);
            }, "Сериализация null должна выбрасывать исключение");
        }
    }
}