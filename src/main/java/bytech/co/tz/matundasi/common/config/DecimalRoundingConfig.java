package bytech.co.tz.matundasi.common.config;

import jakarta.validation.constraints.Digits;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.jdk.NumberDeserializers;
import tools.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounds incoming BigDecimal request fields to the {@code fraction} of their {@link Digits} constraint.
 * Legacy CSV exports carry float noise (e.g. 89.800003, -0.0099999998) that would otherwise fail validation;
 * the database columns use the same scale, so values are stored identically either way.
 */
@Configuration
public class DecimalRoundingConfig {

    @Bean
    public JacksonModule decimalRoundingModule() {
        return new SimpleModule("DecimalRounding")
                .addDeserializer(BigDecimal.class, new DigitsRoundingDeserializer(null));
    }

    static class DigitsRoundingDeserializer extends ValueDeserializer<BigDecimal> {
        private final Integer scale;

        DigitsRoundingDeserializer(Integer scale) {
            this.scale = scale;
        }

        @Override
        public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
            Digits digits = property == null ? null : property.getAnnotation(Digits.class);
            return digits == null ? this : new DigitsRoundingDeserializer(digits.fraction());
        }

        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            BigDecimal value = NumberDeserializers.BigDecimalDeserializer.instance.deserialize(p, ctxt);
            if (value == null || scale == null || value.scale() <= scale) {
                return value;
            }
            return value.setScale(scale, RoundingMode.HALF_UP);
        }
    }
}
