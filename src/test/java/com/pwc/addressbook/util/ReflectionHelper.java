package com.pwc.addressbook.util;

import org.springframework.aop.framework.Advised;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    private static final String MODIFIERS = "modifiers";


    public static Object unwrap(final Object proxiedInstance) throws Exception {
        if (proxiedInstance instanceof Advised) {
            return unwrap(((Advised) proxiedInstance).getTargetSource().getTarget());
        }
        return proxiedInstance;
    }

    public static void setField(Object object, final String fieldName, final Object fieldValue) throws Exception {
        object = unwrap(object);
        final Class clazz = object.getClass();
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        final Field modifiersField = Field.class.getDeclaredField(MODIFIERS);
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(object, fieldValue);
    }

}
