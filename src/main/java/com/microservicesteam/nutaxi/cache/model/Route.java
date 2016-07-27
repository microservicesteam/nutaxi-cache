package com.microservicesteam.nutaxi.cache.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Builder
public class Route implements Serializable {

	@Getter
	private String id;

	@Getter
	private String origin;

	@Getter
	private String destination;

}
