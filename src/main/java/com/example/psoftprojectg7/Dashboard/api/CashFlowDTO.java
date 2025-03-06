package com.example.psoftprojectg7.Dashboard.api;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Cash Flow Table")
public class CashFlowDTO {

    @Schema(description = "The table's columns with a Plan column and n months from current to desired")
    private List<String> columns;

    @Schema(description = "Rows with Plan name and respective monthly cash flow")
    private List<Object> rows;
}
