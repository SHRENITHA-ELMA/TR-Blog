//package com.epam.travel.management.application.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import lombok.Builder;
//import lombok.Data;
//@Data
//@Builder
//public class AdminBlogRequest {
//
//    @NotBlank(message="Id must be provided")
//    private Long blogId;
//    @NotBlank(message = "Status must be provided")
//    private String status;
//}
package com.epam.travel.management.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBlogRequest {

    @NotNull(message = "Blog ID must be provided")
    private Long blogId;

    @NotNull(message = "Status must be provided")
    private String status;
}
