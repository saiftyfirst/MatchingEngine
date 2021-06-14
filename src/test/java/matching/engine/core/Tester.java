package matching.engine.core;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class Tester {

    @Test
    public void longTest() {

        long matchable = 30;

        List<Long> exists = Arrays.asList(12L, 17L, 25L);

        long total = exists.stream().mapToLong(Long::longValue).sum();

        long cumProp = 0;

        long remaining = matchable;

        for (long e: exists) {
            long prop = Math.round((e * matchable) / (double) total);
//            long prop = (e * matchable) / total ;
//            System.out.println((e * matchable) % total);
//            if ((e * matchable) % total > total / 2) {
//                prop++;
//            }
            prop = Math.min(prop, remaining);
            remaining -= prop;
            System.out.println(prop);
            cumProp += prop;
        }

        System.out.println(cumProp);

        Assert.assertTrue(true);


//        // 1.
//        new BigDecimal("35.3456").round(new MathContext(4, RoundingMode.HALF_UP));
////result = 35.35
//// 2.
//        new BigDecimal("35.3456").setScale(4, RoundingMode.HALF_UP);
//// result = 35.3456

    }

    @Test
    public void BigDecimalTest() {

        BigDecimal matchable = BigDecimal.valueOf(30);

        List<BigDecimal> exists = Arrays.asList(BigDecimal.valueOf(11), BigDecimal.valueOf(17), BigDecimal.valueOf(32));

        BigDecimal total = BigDecimal.valueOf(exists.stream().mapToLong(BigDecimal::longValue).sum());

        BigDecimal cumProp = BigDecimal.ZERO;

        BigDecimal remaining = BigDecimal.valueOf(matchable.longValue());

        for (BigDecimal e: exists) {
            BigDecimal prop = e.multiply(matchable).divide(total, RoundingMode.HALF_UP);

            prop = prop.min(remaining);
            remaining = remaining.subtract(prop);
            System.out.println(prop);
            cumProp = cumProp.add(prop);
        }

        System.out.println(cumProp);

        Assert.assertTrue(true);

    }


}
