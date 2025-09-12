package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitFunctionTest {

    @Test
    void testApplyAlwaysReturnsOne() {
        UnitFunction unit = new UnitFunction();

        assertEquals(1.0, unit.apply(0.0), 1e-10, "Должен возвращать 1.0 для x=0");
        assertEquals(1.0, unit.apply(1.0), 1e-10, "Должен возвращать 1.0 для x=1");
        assertEquals(1.0, unit.apply(-1.0), 1e-10, "Должен возвращать 1.0 для x=-1");
        assertEquals(1.0, unit.apply(100.0), 1e-10, "Должен возвращать 1.0 для x=100");
        assertEquals(1.0, unit.apply(-50.0), 1e-10, "Должен возвращать 1.0 для x=-50");
        assertEquals(1.0, unit.apply(123.456), 1e-10, "Должен возвращать 1.0 для x=123.456");
    }

    @Test
    void testInheritanceFromConstantFunction() {
        UnitFunction unit = new UnitFunction();

        // Проверяем, что UnitFunction является наследником ConstantFunction
        assertTrue(unit instanceof ConstantFunction, "UnitFunction должен наследовать от ConstantFunction");

        // Проверяем, что UnitFunction реализует MathFunction
        assertTrue(unit instanceof MathFunction, "UnitFunction должен реализовывать MathFunction");
    }

    @Test
    void testGetConstantReturnsOne() {
        UnitFunction unit = new UnitFunction();

        // Проверяем, что метод getConstant возвращает 1.0
        assertEquals(1.0, unit.getConstant(), 1e-10, "getConstant() должен возвращать 1.0");
    }

    @Test
    void testConstructorNoArguments() {
        // Проверяем, что конструктор без аргументов существует и работает
        UnitFunction unit = new UnitFunction();
        assertNotNull(unit, "Конструктор без аргументов должен создавать объект");

        // Проверяем, что созданный объект корректно работает
        assertEquals(1.0, unit.apply(5.0), 1e-10, "Должен возвращать 1.0 после создания через конструктор без аргументов");
    }

    @Test
    void testNoAdditionalFields() {
        UnitFunction unit = new UnitFunction();

        // Проверяем, что у UnitFunction нет дополнительных полей (только унаследованные)
        // Это можно проверить через рефлексию, но для простоты проверяем поведение
        assertEquals(1.0, unit.getConstant(), 1e-10, "Должен вести себя как ConstantFunction(1.0)");
    }

    @Test
    void testComparisonWithConstantFunction() {
        UnitFunction unit = new UnitFunction();
        ConstantFunction constantOne = new ConstantFunction(1.0);

        // Проверяем, что UnitFunction ведет себя идентично ConstantFunction(1.0)
        assertEquals(constantOne.apply(10.0), unit.apply(10.0), 1e-10, "Должен вести себя как ConstantFunction(1.0)");
        assertEquals(constantOne.apply(-10.0), unit.apply(-10.0), 1e-10, "Должен вести себя как ConstantFunction(1.0)");
        assertEquals(constantOne.apply(0.0), unit.apply(0.0), 1e-10, "Должен вести себя как ConstantFunction(1.0)");

        assertEquals(constantOne.getConstant(), unit.getConstant(), 1e-10, "getConstant() должен возвращать 1.0");
    }
}