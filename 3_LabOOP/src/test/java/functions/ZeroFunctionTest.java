package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZeroFunctionTest {

    @Test
    void testApplyAlwaysReturnsZero() {
        ZeroFunction zero = new ZeroFunction();

        assertEquals(0.0, zero.apply(0.0), 1e-10, "Должен возвращать 0.0 для x=0");
        assertEquals(0.0, zero.apply(1.0), 1e-10, "Должен возвращать 0.0 для x=1");
        assertEquals(0.0, zero.apply(-1.0), 1e-10, "Должен возвращать 0.0 для x=-1");
        assertEquals(0.0, zero.apply(100.0), 1e-10, "Должен возвращать 0.0 для x=100");
        assertEquals(0.0, zero.apply(-50.0), 1e-10, "Должен возвращать 0.0 для x=-50");
        assertEquals(0.0, zero.apply(999.999), 1e-10, "Должен возвращать 0.0 для x=999.999");
    }

    @Test
    void testInheritanceFromConstantFunction() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что ZeroFunction является наследником ConstantFunction
        assertTrue(zero instanceof ConstantFunction, "ZeroFunction должен наследовать от ConstantFunction");

        // Проверяем, что ZeroFunction реализует MathFunction
        assertTrue(zero instanceof MathFunction, "ZeroFunction должен реализовывать MathFunction");
    }

    @Test
    void testGetConstantReturnsZero() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что метод getConstant возвращает 0.0
        assertEquals(0.0, zero.getConstant(), 1e-10, "getConstant() должен возвращать 0.0");
    }

    @Test
    void testConstructorNoArguments() {
        // Проверяем, что конструктор без аргументов существует и работает
        ZeroFunction zero = new ZeroFunction();
        assertNotNull(zero, "Конструктор без аргументов должен создавать объект");

        // Проверяем, что созданный объект корректно работает
        assertEquals(0.0, zero.apply(5.0), 1e-10, "Должен возвращать 0.0 после создания через конструктор без аргументов");
    }

    @Test
    void testNoAdditionalFields() {
        ZeroFunction zero = new ZeroFunction();

        // Проверяем, что у ZeroFunction нет дополнительных полей (только унаследованные)
        // Это можно проверить через рефлексию, но для простоты проверяем поведение
        assertEquals(0.0, zero.getConstant(), 1e-10, "Должен вести себя как ConstantFunction(0.0)");
    }

}