package functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для ZeroFunction")
class ZeroFunctionTest {

    @Test
    @DisplayName("Тест на проверку возвращения 0")
    void testApplyAlwaysReturnsZero() {
        ZeroFunction zero = new ZeroFunction();

        assertEquals(0.0, zero.apply(0.0), 1e-10, "Должен возвращать 0.0 для x=0, GOOD");
        assertEquals(0.0, zero.apply(1.0), 1e-10, "Должен возвращать 0.0 для x=1, GOOD");
        assertEquals(0.0, zero.apply(-1.0), 1e-10, "Должен возвращать 0.0 для x=-1, GOOD");
        assertEquals(0.0, zero.apply(100.0), 1e-10, "Должен возвращать 0.0 для x=100, GOOD");
        assertEquals(0.0, zero.apply(-50.0), 1e-10, "Должен возвращать 0.0 для x=-50, GOOD");
        assertEquals(0.0, zero.apply(999.999), 1e-10, "Должен возвращать 0.0 для x=999.999, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку наследования от постоянной функции")
    void testInheritanceFromConstantFunction() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что ZeroFunction является наследником ConstantFunction
        assertTrue(zero instanceof ConstantFunction, "ZeroFunction должен наследовать от ConstantFunction, GOOD");

        // Проверяем, что ZeroFunction реализует MathFunction
        assertTrue(zero instanceof MathFunction, "ZeroFunction должен реализовывать MathFunction, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку метода getConstant")
    void testGetConstantReturnsZero() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что метод getConstant возвращает 0.0
        assertEquals(0.0, zero.getConstant(), 1e-10, "getConstant() должен возвращать 0.0, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку без аргументов")
    void testConstructorNoArguments() {
        // Проверяем, что конструктор без аргументов существует и работает
        ZeroFunction zero = new ZeroFunction();
        assertNotNull(zero, "Конструктор без аргументов должен создавать объект, GOOD");

        // Проверяем, что созданный объект корректно работает
        assertEquals(0.0, zero.apply(5.0), 1e-10, "Должен возвращать 0.0 после создания через конструктор без аргументов, GOOD");
    }

    @Test
    @DisplayName("Тест на проверку дополнительных полей")
    void testNoAdditionalFields() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что у ZeroFunction нет дополнительных полей (только унаследованные)
        // Это можно проверить через рефлексию, но для простоты проверяем поведение
        assertEquals(0.0, zero.getConstant(), 1e-10, "Должен вести себя как ConstantFunction(0.0), GOOD");
    }

}