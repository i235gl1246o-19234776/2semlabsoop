package functions;

import exception.ArrayIsNotSortedException;
import exception.DifferentLengthOfArraysException;
import exception.InterpolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты конструкторов пользовательских исключений")
class ExceptionConstructorsTest {

    @Test
    @DisplayName("ArrayIsNotSortedException() должен создавать исключение с null-сообщением")
    void testArrayIsNotSortedExceptionDefaultConstructor() {
        ArrayIsNotSortedException exception = new ArrayIsNotSortedException();
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("ArrayIsNotSortedException(String) должен сохранять сообщение")
    void testArrayIsNotSortedExceptionMessageConstructor() {
        String message = "Массив не отсортирован!";
        ArrayIsNotSortedException exception = new ArrayIsNotSortedException(message);
        assertEquals(message, exception.getMessage());
    }

    // ============ DifferentLengthOfArraysException ============

    @Test
    @DisplayName("DifferentLengthOfArraysException() должен создавать исключение с null-сообщением")
    void testDifferentLengthOfArraysExceptionDefaultConstructor() {
        DifferentLengthOfArraysException exception = new DifferentLengthOfArraysException();
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("DifferentLengthOfArraysException(String) должен сохранять сообщение")
    void testDifferentLengthOfArraysExceptionMessageConstructor() {
        String message = "Длины массивов не совпадают!";
        DifferentLengthOfArraysException exception = new DifferentLengthOfArraysException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("InterpolationException() должен создавать исключение с null-сообщением")
    void testInterpolationExceptionDefaultConstructor() {
        InterpolationException exception = new InterpolationException();
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("InterpolationException(String) должен сохранять сообщение")
    void testInterpolationExceptionMessageConstructor() {
        String message = "Ошибка интерполяции!";
        InterpolationException exception = new InterpolationException(message);
        assertEquals(message, exception.getMessage());
    }
}