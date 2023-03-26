package com.t.medicaldocument.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsNestedChild implements Cloneable{
	private String estype;
	private String esvalue;
}
