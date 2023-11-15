package allercheck.backend.domain.allergy.application;

import allercheck.backend.domain.allergy.entity.AllergyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.EnumSet;

@Converter
public class AllergyTypeSetConverter implements AttributeConverter<EnumSet<AllergyType>, String> {

    @Override
    public String convertToDatabaseColumn(EnumSet<AllergyType> attribute) {
        if (attribute == null || attribute.equals(EnumSet.noneOf(AllergyType.class))){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        attribute.forEach(e -> sb.append(e.name()).append(","));
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Override
    public EnumSet<AllergyType> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty() || dbData.contains(".")) {
            return EnumSet.noneOf(AllergyType.class);
        }
        EnumSet<AllergyType> attribute = EnumSet.noneOf(AllergyType.class);
        String[] dbDataArray = StringUtils.trimAllWhitespace(dbData).toUpperCase().split(",");
        Arrays.stream(dbDataArray).forEach(e -> attribute.add(AllergyType.valueOf(e)));
        return attribute;
    }
}