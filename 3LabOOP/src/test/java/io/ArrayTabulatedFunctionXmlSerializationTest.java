package io;

import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ArrayTabulatedFunctionXmlSerializationTest {

    private static final String TEST_FILE = "test_function.xml";

    @Before
    public void setUp() {
        // Удаляем файл перед каждым тестом, если существует
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @After
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
        assertTrue("Файл XML должен существовать после сериализации", Files.exists(Paths.get(TEST_FILE)));

        // Act: десериализуем из файла
        ArrayTabulatedFunction restored;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            restored = FunctionsIO.deserializeXml(reader);
        }

        // Assert: проверяем, что данные совпадают
        assertNotNull("Восстановленный объект не должен быть null", restored);
        assertEquals("Количество точек должно совпадать", original.getCount(), restored.getCount());

        for (int i = 0; i < original.getCount(); i++) {
            assertEquals("Значение X[" + i + "] должно совпадать", original.getX(i), restored.getX(i), 1e-9);
            assertEquals("Значение Y[" + i + "] должно совпадать", original.getY(i), restored.getY(i), 1e-9);
        }
    }

    @Test
    public void testSerializeAndDeserializeXml_EmptyFunction() throws Exception {
        // Тест с пустым или минимальным случаем
        double[] xValues = {5.0};
        double[] yValues = {25.0};
        ArrayTabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            FunctionsIO.serializeXml(writer, original);
        }

        ArrayTabulatedFunction restored;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            restored = FunctionsIO.deserializeXml(reader);
        }

        assertNotNull(restored);
        assertEquals(1, restored.getCount());
        assertEquals(5.0, restored.getX(0), 1e-9);
        assertEquals(25.0, restored.getY(0), 1e-9);
    }

    @Test(expected = IOException.class)
    public void testDeserializeXml_FileNotFound() throws Exception {
        // Попытка прочитать из несуществующего файла
        try (BufferedReader reader = new BufferedReader(new FileReader("nonexistent.xml"))) {
            FunctionsIO.deserializeXml(reader);
        }
    }

    @Test
    public void testSerializeXml_NullFunction() throws Exception {
        // Проверка, что сериализация null вызывает исключение (или обрабатывается)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            FunctionsIO.serializeXml(writer, null);
        }

        // Чтение и проверка, что десериализуется null (если XStream так делает)
        ArrayTabulatedFunction restored;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            restored = FunctionsIO.deserializeXml(reader);
        }

        assertNull("Должен десериализоваться null", restored);
    }
}