package com.example.commonlibrary;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class WeChatPublicAccountBean {


    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("errorCode")
    private Integer errorCode;
    @SerializedName("errorMsg")
    private String errorMsg;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class DataDTO {
        @SerializedName("children")
        private List<?> children;
        @SerializedName("courseId")
        private Integer courseId;
        @SerializedName("id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("order")
        private Integer order;
        @SerializedName("parentChapterId")
        private Integer parentChapterId;
        @SerializedName("userControlSetTop")
        private Boolean userControlSetTop;
        @SerializedName("visible")
        private Integer visible;
    }
}
