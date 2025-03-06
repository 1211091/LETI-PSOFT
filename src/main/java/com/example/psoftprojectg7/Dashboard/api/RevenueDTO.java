package com.example.psoftprojectg7.Dashboard.api;

import java.util.List;

import com.example.psoftprojectg7.Dashboard.model.RevenueObj;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Revenue Table")
public class RevenueDTO {

    @Schema(description = "The table's columns with Plan, Metric and Total columns")
    private List<String> columns;

    @Schema(description = "Rows with the data for each Plan and a last row with the TOTAL")
    private List<RevenueObj> rows;
}
