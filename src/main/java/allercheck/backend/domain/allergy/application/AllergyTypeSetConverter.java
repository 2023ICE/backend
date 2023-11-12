package allercheck.backend.domain.allergy.application;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.EnumSet;

@Converter
@Slf4j
public class AllergyTypeSetConverter implements AttributeConverter<EnumSet<AllergyType>, String> {

    @Override
    public String convertToDatabaseColumn(EnumSet<AllergyType> attribute) {
        if (attribute == null || attribute.equals(EnumSet.noneOf(AllergyType.class))){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        attribute.forEach(e -> sb.append(e.name()).append(","));
        String result = sb.toString();
        if(result.charAt(result.length() - 1) == ',') result = result.substring(0, result.length() - 1);
        return result;
    }

    @Override
    public EnumSet<AllergyType> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty() || dbData.contains(".")) return EnumSet.noneOf(AllergyType.class);
        EnumSet<AllergyType> attribute = EnumSet.noneOf(AllergyType.class);
        String[] dbDataArray = StringUtils.trimAllWhitespace(dbData).toUpperCase().split(",");
        Arrays.stream(dbDataArray).forEach(e -> attribute.add(AllergyType.valueOf(e)));
        return attribute;
    }
}