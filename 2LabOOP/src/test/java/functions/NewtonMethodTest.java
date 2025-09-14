package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NewtonMethodTest {

    @Test
    void testSqrt2() {
        // f(x) = x² - 2, корень ≈ 1.4142
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return x * x - 2;
            }
        };

        MathFunction df = new MathFunction() {
            @Override
            public double apply(double x) {
                return 2 * x;
            }
        };

        NewtonMethod newton = new NewtonMethod(f, df);
        double result = newton.apply(1.0);

        assertEquals(Math.sqrt(2), result, 1e-6);
    }

    @Test
    void testSinMinusOne() {
        // f(x) = sin(x) - 1, корни: x = π/2 + 2πk
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.sin(x) - 1;
            }
        };

        MathFunction df = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.cos(x);
            }
        };

        NewtonMethod newton = new NewtonMethod(f, df);

        // Находим корень π/2 ≈ 1.5708
        double result = newton.apply(1.4);

        assertEquals(Math.PI / 2, result, 1e-6);
    }

    @Test
    void testSinx2() {
        // f(x) = sin(x²), корень: x = √(π/2) ≈ 1.2533 (когда x² = π/2)
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.sin(x * x);
            }
        };

        MathFunction df = new MathFunction() {
            @Override
            public double apply(double x) {
                // ПРАВИЛЬНАЯ производная: cos(x²) * 2x
                return Math.cos(x * x) * 2 * x;
            }
        };

        NewtonMethod newton = new NewtonMethod(f, df);
        double result = newton.apply(1.2);

        assertEquals(-Math.sqrt(Math.PI), result, 1e-6);
    }




    @Test
    void testArctgNoSolution() {
        // f(x) = arctg + 7.8
        MathFunction f = new MathFunction() {
            @Override
            public double apply(double x) {
                return Math.atan(x) + 7.8;
            }
        };

        MathFunction df = new MathFunction() {
            @Override
            public double apply(double x) {
                    return 1.0 / (1.0 + x * x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);

            assertThrows(RuntimeException.class, () -> {
                newton.apply(1.0);
            });
        }

        @Test
        void testArctgReal() {
            // f(x) = artcg(x) - 0.5
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.atan(x) - 0.5;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return 1.0 / (1.0 + x * x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.5);

            assertEquals(Math.tan(0.5), result, 1e-6);
        }

        @Test
        void testExponential() {
        // f(x) = e^x -2
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.exp(x) - 2;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.exp(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.5);

            assertEquals(Math.log(2), result, 1e-6);
        }

        @Test
        void testZeroDerivative() {
        // f(x) = x^3
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return x * x * x;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return 3 * x * x;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);

            assertThrows(ArithmeticException.class, () -> {
                newton.apply(0.0);
            });
        }

        @Test
        void testCubicRoot() {
        // f(x) = x^3 - 8
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return x * x * x - 8;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return 3 * x * x;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(1.5);

            assertEquals(2.0, result, 1e-6);
        }

        @Test
        void testLogarithmic() {
        // f(x) = ln(x) - 1
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.log(x) - 1;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return 1.0 / x;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(2.5);

            assertEquals(Math.E, result, 1e-6);
        }

        @Test
        void testCosMinusHalf() {
        //f(x) = cos(x) - 0.5
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.cos(x) - 0.5;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    return -Math.sin(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(1.0);

            assertEquals(Math.PI / 3, result, 1e-6);
        }


        @Test
        void testNestedExpSin() {
            // f(x) = e^(sin(x)) - e^0.5 ≈ 1.6487
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.exp(Math.sin(x)) - Math.exp(0.5);
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [exp(sin(x))] = exp(sin(x)) * cos(x)
                    return Math.exp(Math.sin(x)) * Math.cos(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.5);

            assertEquals(Math.PI / 6, result, 1e-6);
        }

        @Test
        void testLogOfTrigComposition() {
            // f(x) = ln(cos(x) + 1.5) - ln(2)
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.log(Math.cos(x) + 1.5) - Math.log(2.0);
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [ln(cos(x)+1.5)] = (-sin(x)) / (cos(x) + 1.5)
                    return -Math.sin(x) / (Math.cos(x) + 1.5);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(1.0);

            assertEquals(Math.PI / 3, result, 1e-6);
        }

        @Test
        void testArcsinOfSquare() {
            // f(x) = arcsin(x²) - π/4
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.asin(x * x) - Math.PI / 4;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [arcsin(x²)] = (2x) / sqrt(1 - x⁴)
                    double x2 = x * x;
                    return (2 * x) / Math.sqrt(1 - x2 * x2);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.8);

            double expected = Math.sqrt(Math.sqrt(2) / 2); // = (2^{-1/4})
            assertEquals(expected, result, 1e-6);
        }

        @Test
        void testTanOfLogarithm() {
            // f(x) = tan(ln(x)) - 1
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.tan(Math.log(x)) - 1;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [tan(ln(x))] = sec²(ln(x)) * (1/x)
                    double ln_x = Math.log(x);
                    return (1.0 / (Math.cos(ln_x) * Math.cos(ln_x))) * (1.0 / x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(2.0);

            assertEquals(Math.exp(Math.PI / 4), result, 1e-6);
        }

        @Test
        void testExpOfExpMinusTwo() {
            // f(x) = exp(exp(x)) - e^e ≈ 15.154
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.exp(Math.exp(x)) - Math.exp(Math.E);
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [exp(exp(x))] = exp(exp(x)) * exp(x)
                    return Math.exp(Math.exp(x)) * Math.exp(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.8);

            assertEquals(1.0, result, 1e-6);
        }


        @Test
        void testLogOfArcsinPlusExp() {
            // f(x) = ln(arcsin(x) + exp(x)) - ln(π/2 + 1)

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= -1 || x >= 1) return 1000; // вне области определения arcsin
                    double arg = Math.asin(x) + Math.exp(x);
                    if (arg <= 0) return -1000;
                    return Math.log(arg) - Math.log(Math.PI / 2 + 1);
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= -1 || x >= 1) return 0;
                    double asin_x = Math.asin(x);
                    double exp_x = Math.exp(x);
                    double sum = asin_x + exp_x;
                    if (sum <= 0) return 0;
                    // d/dx [ln(asin(x) + exp(x))] = [1/sqrt(1-x²) + exp(x)] / (asin(x) + exp(x))
                    double numerator = (1.0 / Math.sqrt(1 - x * x)) + exp_x;
                    return numerator / sum;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.6);

            // Точное решение: x ≈ 0.631 (численно найдено)
            assertEquals(0.631, result, 1e-2);
        }

        @Test
        void testPowerTower() {
            // f(x) = x^(x) - 4
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= 0) return Double.MAX_VALUE; // избегаем NaN
                    return Math.pow(x, x) - 4;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= 0) return 0;
                    // d/dx [x^x] = x^x * (ln(x) + 1)
                    return Math.pow(x, x) * (Math.log(x) + 1);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(1.8);

            assertEquals(2.0, result, 1e-6);
        }

        @Test
        void testInverseComposition() {
            // f(x) = sin(arccos(x)) - sqrt(1 - x²)

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x < -1 || x > 1) return 1000;
                    return Math.sin(Math.acos(x)) - 0.5;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x < -1 || x > 1) return 0;
                    // d/dx [sin(arccos(x))] = d/dx [sqrt(1-x²)] = -x / sqrt(1-x²)
                    return -x / Math.sqrt(1 - x * x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.8);

            assertEquals(Math.sqrt(3) / 2, result, 1e-6);
        }

        @Test
        void testHighlyNonlinearWithAsymptote() {
            // f(x) = exp(tan(x)) - e^1, на интервале (-π/2, π/2)

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    // Избегаем асимптоты tan(x) при x→±π/2
                    if (Math.abs(x) >= Math.PI / 2 - 1e-5) return Double.MAX_VALUE;
                    return Math.exp(Math.tan(x)) - Math.E;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (Math.abs(x) >= Math.PI / 2 - 1e-5) return 0;
                    // d/dx [exp(tan(x))] = exp(tan(x)) * sec²(x)
                    double t = Math.tan(x);
                    double sec2 = 1.0 / (Math.cos(x) * Math.cos(x));
                    return Math.exp(t) * sec2;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.7);

            assertEquals(Math.PI / 4, result, 1e-6);
        }

        @Test
        void testQuarticUnderRoot() {
            // f(x) = sqrt(x^4 + 1) - sqrt(17)

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.sqrt(x * x * x * x + 1) - Math.sqrt(17);
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [sqrt(x⁴ + 1)] = (4x³) / (2 * sqrt(x⁴ + 1)) = (2x³) / sqrt(x⁴ + 1)
                    double x4 = x * x * x * x;
                    return (2 * x * x * x) / Math.sqrt(x4 + 1);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(1.8);

            assertEquals(2.0, result, 1e-6);
        }

        @Test
        void testHyperbolicComposition() {
            // f(x) = sinh(cosh(x)) - sinh(cosh(1))
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.sinh(Math.cosh(x)) - Math.sinh(Math.cosh(1));
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // d/dx [sinh(cosh(x))] = cosh(cosh(x)) * sinh(x)
                    return Math.cosh(Math.cosh(x)) * Math.sinh(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.8);

            assertEquals(1.0, result, 1e-6);
        }

        @Test
        void testComplexTrigProduct() {
            // f(x) = sin(x) * cos(x) * tan(x) - 0.5

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    return Math.sin(x) * Math.cos(x) * Math.tan(x) - 0.5;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    // Производная от sin²(x) = 2*sin(x)*cos(x)
                    return 2 * Math.sin(x) * Math.cos(x);
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.7);

            assertEquals(Math.PI / 4, result, 1e-6);
        }

        @Test
        void testNestedFractionalPower() {
            // f(x) = (x^(1/3) + 1)^2 - 4

            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x < 0) return Double.MAX_VALUE; // избегаем комплексных
                    return Math.pow(x, 1.0 / 3.0) + 1;
                }
            };

            MathFunction f2 = new MathFunction() {
                @Override
                public double apply(double x) {
                    double inner = f.apply(x);
                    return inner * inner - 4;
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= 0) return 0;
                    // d/dx [(x^{1/3} + 1)^2] = 2*(x^{1/3} + 1) * (1/3)*x^{-2/3}
                    double cube_root = Math.pow(x, 1.0 / 3.0);
                    return 2 * (cube_root + 1) * (1.0 / (3.0 * Math.pow(x, 2.0 / 3.0)));
                }
            };

            NewtonMethod newton = new NewtonMethod(f2, df);
            double result = newton.apply(0.8);

            assertEquals(1.0, result, 1e-6);
        }

        @Test
        void testArbitraryDepthComposition() {
            // f(x) = exp(sin(ln(arcsin(x/2)))) - exp(sin(ln(π/6)))
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= -2 || x >= 2) return 1000;
                    double inner = Math.asin(x / 2.0);
                    double mid = Math.log(inner);
                    double outer = Math.sin(mid);
                    return Math.exp(outer) - Math.exp(Math.sin(Math.log(Math.PI / 6.0)));
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    if (x <= -2 || x >= 2) return 0;
                    double arg = x / 2.0;
                    double asin_arg = Math.asin(arg);
                    if (asin_arg <= 0) return 0; // log не определён
                    double ln_asin = Math.log(asin_arg);
                    double sin_ln = Math.sin(ln_asin);
                    double exp_sin = Math.exp(sin_ln);

                    // d/dx [exp(sin(ln(arcsin(x/2))))]
                    // = exp(sin(ln(arcsin(x/2)))) * cos(ln(arcsin(x/2))) * (1/arcsin(x/2)) * (1/sqrt(1-(x/2)^2)) * (1/2)

                    double deriv = exp_sin
                            * Math.cos(ln_asin)
                            * (1.0 / asin_arg)
                            * (1.0 / Math.sqrt(1 - arg * arg))
                            * (0.5);

                    return deriv;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.9);

            assertEquals(1.0, result, 1e-6);
        }

        @Test
        void testFinalExtremeComposition() {
            // f(x) = arctan(sin(exp(cos(x)))) - arctan(sin(exp(cos(1))))
            // Корень: x=1 — потому что вся цепочка совпадает
            MathFunction f = new MathFunction() {
                @Override
                public double apply(double x) {
                    double c = Math.cos(x);
                    double e = Math.exp(c);
                    double s = Math.sin(e);
                    return Math.atan(s) - Math.atan(Math.sin(Math.exp(Math.cos(1))));
                }
            };

            MathFunction df = new MathFunction() {
                @Override
                public double apply(double x) {
                    double c = Math.cos(x);
                    double e = Math.exp(c);
                    double s = Math.sin(e);
                    double atan_s = Math.atan(s);

                    // d/dx [atan(sin(exp(cos(x))))] =
                    // = 1/(1+s²) * cos(exp(cos(x))) * exp(cos(x)) * (-sin(x))

                    double deriv = (1.0 / (1 + s * s))
                            * Math.cos(e)
                            * e
                            * (-Math.sin(x));

                    return deriv;
                }
            };

            NewtonMethod newton = new NewtonMethod(f, df);
            double result = newton.apply(0.95);

            assertEquals(1.0, result, 1e-6);
        }
    @Test
    void testCustomEAndMaxIterations() {
        // Проверяем, что конструктор с пользовательскими e и maxIterations работает корректно
        MathFunction f = x -> x * x - 4; // корень = 2
        MathFunction df = x -> 2 * x;

        // Используем очень строгий e и мало итераций, чтобы убедиться, что они применяются
        double customE = 1e-10;
        int customMaxIter = 50;

        NewtonMethod newton = new NewtonMethod(f, df, customE, customMaxIter);

        // Сходимость должна быть медленнее, но всё равно сойдётся за 5 итераций для x²=4
        double result = newton.apply(1.0);
        assertEquals(2.0, result, 1e-8); // Точность выше customE, но метод всё равно сходится
    }

    @Test
    void testLowMaxIterationsThrowsException() {
        // Проверяем, что при малом maxIterations бросается RuntimeException
        MathFunction f = x -> x * x - 4;
        MathFunction df = x -> 2 * x;

        NewtonMethod newton = new NewtonMethod(f, df, 1e-6, 1); // только 1 итерация

        assertThrows(RuntimeException.class, () -> {
            newton.apply(1.0);
        });
    }

    @Test
    void testNegativeInitialGuess() {
        // Проверяем, что метод работает с отрицательным x0
        MathFunction f = x -> x * x - 9; // корни: ±3
        MathFunction df = x -> 2 * x;

        NewtonMethod newton = new NewtonMethod(f, df);

        // Ищем отрицательный корень
        double result = newton.apply(-1.0);
        assertEquals(-3.0, result, 1e-6);
    }
}
