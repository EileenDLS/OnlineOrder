package com.fullstack.onlineorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddToCartBody(
        @JsonProperty("menu_Id") Long menuId
) {

}
