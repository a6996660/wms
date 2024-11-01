package com.project.wms.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2024-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;


}
