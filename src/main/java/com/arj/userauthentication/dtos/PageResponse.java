package com.arj.userauthentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse {

  private Object content;
  private int currentPage;
  private long totalItems;
  private int totalPages;

}
