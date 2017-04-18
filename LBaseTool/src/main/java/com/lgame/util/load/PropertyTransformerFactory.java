/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class PropertyTransformerFactory {

    public static PropertyTransformer newTransformer(Class clazzToTransform, Class<? extends PropertyTransformer> tc){
        if (tc == PropertyTransformer.class) {
            tc = null;
        }
        if (tc != null) {
            try {
                return (PropertyTransformer) tc.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Can't instantiate property transfromer", e);
            }
        }
        if ((clazzToTransform == Boolean.class) || (clazzToTransform == Boolean.TYPE)) {
            return BooleanTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Byte.class) || (clazzToTransform == Byte.TYPE)) {
            return ByteTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Character.class) || (clazzToTransform == Character.TYPE)) {
            return CharTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Double.class) || (clazzToTransform == Double.TYPE)) {
            return DoubleTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Float.class) || (clazzToTransform == Float.TYPE)) {
            return FloatTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Integer.class) || (clazzToTransform == Integer.TYPE)) {
            return IntegerTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Long.class) || (clazzToTransform == Long.TYPE)) {
            return LongTransformer.SHARED_INSTANCE;
        }
        if ((clazzToTransform == Short.class) || (clazzToTransform == Short.TYPE)) {
            return ShortTransformer.SHARED_INSTANCE;
        }
        if (clazzToTransform == String.class) {
            return StringTransformer.SHARED_INSTANCE;
        }
        if (clazzToTransform.isEnum()) {
            return EnumTransformer.SHARED_INSTANCE;
        }
        if (clazzToTransform == File.class) {
            return FileTransformer.SHARED_INSTANCE;
        }
        if (ClassUtils.isSubclass(clazzToTransform, InetSocketAddress.class)) {
            return InetSocketAddressTransformer.SHARED_INSTANCE;
        }
        if (clazzToTransform == Pattern.class) {
            return PatternTransformer.SHARED_INSTANCE;
        }
        if (clazzToTransform == Class.class) {
            return ClassTransformer.SHARED_INSTANCE;
        }
        throw new RuntimeException("Transformer not found for class "
                + clazzToTransform.getName());
    }
}
