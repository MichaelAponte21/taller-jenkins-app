package taller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraTest {

    @Test
    void sumaDosNumeros() {
        assertEquals(6, Calculadora.sumar(2, 3));
    }

    @Test
    void sumaNegativos() {
        assertEquals(-1, Calculadora.sumar(2, -3));
    }
}
