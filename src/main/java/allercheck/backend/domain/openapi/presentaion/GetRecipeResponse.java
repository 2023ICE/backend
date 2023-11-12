package allercheck.backend.domain.openapi.presentaion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRecipeResponse {

    @JsonProperty("COOKRCP01")
    private CookRcp01 COOKRCP01;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CookRcp01 {
        @JsonProperty("total_count")
        private String total_count;
        @JsonProperty("row")
        private List<Recipe> row;
        @JsonProperty("RESULT")
        private Result RESULT;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Recipe {

        @JsonProperty("RCP_PARTS_DTLS")
        private String partsDetails;

        @JsonProperty("RCP_WAY2")
        private String way2;

        @JsonProperty("MANUAL_IMG20")
        private String manualImage20;

        @JsonProperty("MANUAL20")
        private String manual20;

        @JsonProperty("RCP_SEQ")
        private String sequence;

        @JsonProperty("INFO_NA")
        private String infoNA;

        @JsonProperty("INFO_WGT")
        private String infoWeight;

        @JsonProperty("INFO_PRO")
        private String infoProtein;

        @JsonProperty("MANUAL_IMG13")
        private String manualImage13;

        @JsonProperty("MANUAL_IMG14")
        private String manualImage14;

        @JsonProperty("MANUAL_IMG15")
        private String manualImage15;

        @JsonProperty("MANUAL_IMG16")
        private String manualImage16;

        @JsonProperty("MANUAL_IMG10")
        private String manualImage10;

        @JsonProperty("MANUAL_IMG11")
        private String manualImage11;

        @JsonProperty("MANUAL_IMG12")
        private String manualImage12;

        @JsonProperty("MANUAL_IMG17")
        private String manualImage17;

        @JsonProperty("MANUAL_IMG18")
        private String manualImage18;

        @JsonProperty("MANUAL_IMG19")
        private String manualImage19;

        @JsonProperty("INFO_FAT")
        private String infoFat;

        @JsonProperty("HASH_TAG")
        private String hashTag;

        @JsonProperty("MANUAL_IMG02")
        private String manualImage02;

        @JsonProperty("MANUAL_IMG03")
        private String manualImage03;

        @JsonProperty("RCP_PAT2")
        private String type;

        @JsonProperty("MANUAL_IMG04")
        private String manualImage04;

        @JsonProperty("MANUAL_IMG05")
        private String manualImage05;

        @JsonProperty("MANUAL_IMG01")
        private String manualImage01;

        @JsonProperty("MANUAL01")
        private String manual01;

        @JsonProperty("ATT_FILE_NO_MK")
        private String imagePathBig;

        @JsonProperty("MANUAL11")
        private String manual11;

        @JsonProperty("MANUAL12")
        private String manual12;

        @JsonProperty("MANUAL10")
        private String manual10;

        @JsonProperty("INFO_CAR")
        private String infoCarbohydrate;

        @JsonProperty("MANUAL19")
        private String manual19;

        @JsonProperty("RCP_NA_TIP")
        private String tipSodium;

        @JsonProperty("INFO_ENG")
        private String infoEnergy;

        @JsonProperty("MANUAL17")
        private String manual17;

        @JsonProperty("MANUAL18")
        private String manual18;

        @JsonProperty("RCP_NM")
        private String recipeName;

        @JsonProperty("MANUAL15")
        private String manual15;

        @JsonProperty("MANUAL16")
        private String manual16;

        @JsonProperty("MANUAL13")
        private String manual13;

        @JsonProperty("MANUAL14")
        private String manual14;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Result {
        @JsonProperty("MSG")
        private String MSG;
        @JsonProperty("CODE")
        private String CODE;
    }
}
