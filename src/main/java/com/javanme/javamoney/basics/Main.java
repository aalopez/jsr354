package com.javanme.javamoney.basics;

import org.javamoney.moneta.CurrencyUnitBuilder;

import javax.money.*;
import javax.money.convert.*;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

/**
 * Clase que usa las funcionalidades básicas ofrecidas por el JSR354: Monedas y Dinero
 * Este código es compatible con Java SE 8
 * @author Alexis Lopez
 */
public class Main {

    public static void main(String... args) {
        monedas();
        valoresMonetarios();
        aritmetica();
        operadoresMonetarios();
        queriesMonetarios();
        formato();
        redondeo();
        redondeoAvanzado();
        conversion();
        conversion2();
    }

    /**
     * Ejemplos con monedas
     */
    public static void monedas() {
        System.out.println("\n****monedas****");

        //obtiene monedas pesos colombianos. Se puede obtener pasando código de la moneda o el java.util.Locale de Colombia
        CurrencyUnit pesosCop = Monetary.getCurrency("COP");
        CurrencyUnit pesosColombia = Monetary.getCurrency(new Locale("es", "CO"));

        //Ontiene una moneda basado en una constante definida en java.util.Locale
        CurrencyUnit usDolars = Monetary.getCurrency(Locale.US);

        //Crea una moneda personalizada y la registra para su posterior uso
        CurrencyUnit moneda = CurrencyUnitBuilder.of("XBT", "default")
                                                 .setNumericCode(-1)
                                                 .setDefaultFractionDigits(3)
                                                 .build();

        //Verifica que las monedas obtenidas previamente son la misma referencia
        System.out.println("COP Ref Iguales = " + (pesosCop == pesosColombia));

        System.out.printf("Cod = %s\n", pesosCop.getCurrencyCode());
        System.out.printf("Cod Num = %d\n", pesosCop.getNumericCode());
        System.out.printf("Dígitos fracc = %d\n", pesosCop.getDefaultFractionDigits());

        System.out.println("");

        System.out.printf("Cod = %s\n", moneda.getCurrencyCode());
        System.out.printf("Cod Num = %d\n", moneda.getNumericCode());
        System.out.printf("Dígitos fracc = %d\n", moneda.getDefaultFractionDigits());

    }

    /**
     * Ejemplos con valores monetarios
     */
    public static void valoresMonetarios() {
        System.out.println("\n****valoresMonetarios****");

        //Obtiene un valor monetario en Pesos colombianos usando la fabrica por defecto
        MonetaryAmount valorCop = Monetary.getDefaultAmountFactory()
                                          .setCurrency("COP")
                                          .setNumber(500_000)
                                          .create();

        //Obtiene un valor monetario usando dólares americanos y la fabrica por defecto
        CurrencyUnit dolarUS = Monetary.getCurrency(Locale.US);
        MonetaryAmount valorUsd = Monetary.getDefaultAmountFactory()
                                          .setCurrency(dolarUS)
                                          .setNumber(500.55)
                                          .create();

        //Podemos obtener información de los valores monetarios
        System.out.printf("Valor = %s\n", valorCop.getCurrency());
        System.out.printf("Valor = %s\n", valorCop.getNumber());

        System.out.println("");

        System.out.printf("Moneda = %s\n", valorUsd.getCurrency());
        System.out.printf("Cantidad = %s\n", valorUsd.getNumber());

        //Cantidad de dígitos, incluyendo decimales
        System.out.printf("Precisión = %d\n", valorUsd.getNumber()
                                                      .getPrecision());

        //cantidad de decimales
        System.out.printf("Escala = %d\n", valorUsd.getNumber()
                                                   .getScale());

        //Valor de los decimales
        System.out.printf("Numerador fracción = %d\n", valorUsd.getNumber()
                                                               .getAmountFractionNumerator());

        //Denominador de la parte fraccional. Por ejemplo 55 centavos retorna el valor 100
        System.out.printf("Denominador fracción = %d\n", valorUsd.getNumber()
                                                                 .getAmountFractionDenominator());

    }

    /**
     * Ejemplos de operaciones aritméticas entre valores monetarios
     */
    public static void aritmetica() {
        System.out.println("\n****aritmetica****");

        //Obtiene un valor monetario en pesos colombianos
        MonetaryAmount valorCop = Monetary.getDefaultAmountFactory()
                                          .setCurrency("COP")
                                          .setNumber(500_000)
                                          .create();

        //Obtiene otro valor monetario en pesos colombianos
        MonetaryAmount valorCop100 = Monetary.getDefaultAmountFactory()
                                             .setCurrency("COP")
                                             .setNumber(100_000)
                                             .create();

        //Realiza operaciones aritméticas encadenadas para obtener un valor final
        MonetaryAmount valorCopFinal = valorCop.subtract(valorCop100)
                                               .multiply(2)
                                               .add(valorCop100);

        System.out.printf("Valor Final = %s\n", valorCopFinal);
    }

    /**
     * Ejemplo mostrando el uso de un operador monetario
     */
    public static void operadoresMonetarios() {
        System.out.println("\n****operadoresMonetarios****");

        //Obtiene un valor monetario en pesos colombianos
        MonetaryAmount valorCop = Monetary.getDefaultAmountFactory()
                                          .setCurrency("COP")
                                          .setNumber(500_000)
                                          .create();

        //Crea una instancia de la interfase funcional javax.money.MonetaryOperator usando una expresión lambda
        //Este operador nos permitira duplicar los valores monetarios sobre los cuales se aplique
        MonetaryOperator duplicador = v -> v.multiply(2);
        System.out.printf("Valor Duplicado = %s\n", valorCop.with(duplicador));
    }

    /**
     * Ejemplo usando un query monetario
     */
    public static void queriesMonetarios() {
        System.out.println("\n****queriesMonetarios****");

        //Obtiene un valor monetario en pesos colombianos
        MonetaryAmount valorCop = Monetary.getDefaultAmountFactory()
                                          .setCurrency("COP")
                                          .setNumber(500_000)
                                          .create();

        //Crea una instancia de la interfase funcional javax.money.MonetaryQuery usando una expresión lambda
        //Este operador nos permitira saber si los valores monetarios sobre los cuales se aplique son positivos
        MonetaryQuery positivoQuery = v -> v.isPositive();
        System.out.printf("Valor positivo? = %s\n", valorCop.query(positivoQuery));
    }

    /**
     * Ejemplo del uso de formato de valores monetarios de diferentes monedas
     */
    public static void formato() {
        System.out.println("\n****formato****");

        //Obtenemos un valor en dólares americanos
        MonetaryAmount valorUsd = Monetary.getDefaultAmountFactory()
                                          .setCurrency("USD")
                                          .setNumber(500.55)
                                          .create();

        //Obtenemos un valor en Rupias Indias
        MonetaryAmount valorInr = Monetary.getDefaultAmountFactory()
                                          .setCurrency("INR")
                                          .setNumber(123456789101112.123456)
                                          .create();

        //Solicitamos el formato por defecto para dólares americanos
        MonetaryAmountFormat formato = MonetaryFormats.getAmountFormat(Locale.US);

        //Solicitamos la creación de un formato personalizado para dólares americanos diferente al formato por defecto
        MonetaryAmountFormat formPersonalizado = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(Locale.US)
                                        .set("pattern", "$###.## ¤;($###.##) ¤")
                                        .build()
        );


        //Solicitamos la creación de un formato personalizado para la moneda India diferente al formato por defecto
        AmountFormatQuery query = AmountFormatQueryBuilder.of(new Locale("", "INR"))
                                                          .set("groupingSizes", new int[]{3, 2})
                                                          .build();
        MonetaryAmountFormat formatINR = MonetaryFormats.getAmountFormat(query);


        System.out.printf("Formato USD = %s\n", formato.format(valorUsd));
        System.out.printf("Formato Personalizado USD = %s\n", formPersonalizado.format(valorUsd));
        System.out.printf("Formato INR = %s\n", formatINR.format(valorInr));
    }

    /**
     * Ejemplo usando el redondeo por defecto
     */
    public static void redondeo() {
        System.out.println("\n****redondeo****");

        //Obtenemos un valor monetario en pesos con decimales
        MonetaryAmount valor = Monetary.getDefaultAmountFactory()
                                       .setCurrency("COP")
                                       .setNumber(500_000.3472)
                                       .create();

        //Solicitamos el redondeo por defecto para la moneda pesos colombianos
        MonetaryRounding roundingCop = Monetary.getRounding(Monetary.getCurrency("COP"));

        //Aplicamos el redondeo por defecto
        System.out.printf("COP redondeado = %s\n", valor.with(roundingCop));
    }

    /**
     * Ejemplo de como se usaría un redondeo personalizado ofrecido por la implementación, si ésta lo ofreciera
     */
    public static void redondeoAvanzado() {
        System.out.println("\n****redondeoAvanzado****");

        MonetaryAmount valorChf = Monetary.getDefaultAmountFactory()
                                          .setCurrency("COP")
                                          .setNumber(500_000.3472)
                                          .create();

        MonetaryRounding rounding = Monetary.getRounding(
                RoundingQueryBuilder.of()
                                    .setRoundingName("cashRounding")
                                    .setCurrency(Monetary.getCurrency("CHF"))
                                    .build());

        //Aplicamos el redondeo personalizado. Funcionaría si la implementación ofreciera un redondeo llamado cashRounding
        //System.out.printf("CHF redondeado con cashRounding = %s\n", valorChf.with(rounding));
    }

    /**
     * Ejemplo de conversión entre monedas. La implementación de referencia, Moneta, no tiene la conversión de todas
     * las monedas, por lo tanto  los ejemplos no aplican para todas las monedas
     */
    public static void conversion() {
        System.out.println("\n****conversion****");

        MonetaryAmount valorMxn = Monetary.getDefaultAmountFactory()
                                          .setCurrency("MXN")
                                          .setNumber(500)
                                          .create();

        CurrencyConversion conversion = MonetaryConversions.getConversion("USD");
        MonetaryAmount valorEnDolares = valorMxn.with(conversion);

        valorEnDolares = valorMxn.with(conversion);
        System.out.printf("En dólares = %s\n", valorEnDolares);
        System.out.printf("Tasa = %s\n", conversion.getExchangeRate(valorMxn)
                                                   .getFactor());
        System.out.printf("Proveedor = %s\n", conversion.getExchangeRate(valorMxn)
                                                        .getContext()
                                                        .getProviderName());
    }

    /**
     * Ejemplo de como se usaría una conversión más personalizada si la implementación la ofreciera
     */
    public static void conversion2() {
        System.out.println("\n****conversion2****");

        MonetaryAmount valorMxn = Monetary.getDefaultAmountFactory()
                                          .setCurrency("MXN")
                                          .setNumber(500)
                                          .create();

        ConversionQuery query = ConversionQueryBuilder.of()
                                                      .setRateTypes(RateType.DEFERRED)
                                                      .set("customerID", 1234)
                                                      .set("contractID", "ABC")
                                                      .setTermCurrency("CHF")
                                                      .build();

        CurrencyConversion conversion = MonetaryConversions.getConversion(query);
        MonetaryAmount valorEnFrancos = valorMxn.with(conversion);

        System.out.printf("En Francos = %s\n", valorEnFrancos);
        System.out.printf("Tasa = %s\n", conversion.getExchangeRate(valorMxn)
                                                   .getFactor());
        System.out.printf("Proveedor = %s\n", conversion.getExchangeRate(valorMxn)
                                                        .getContext()
                                                        .getProviderName());
    }
}
