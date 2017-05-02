/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgame.util.load.properties;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Documented
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Properties
{
  String keyName() default "";
  
  String fileName() default "";
  
  String defaultValue() default "";
  
  boolean required() default true;

  Class<? extends PropertyTransformer> propertyTransformer() default PropertyTransformer.class;
}
