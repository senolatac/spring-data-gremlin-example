package com.example.gremlin.util;

import com.example.gremlin.annotation.Edge;
import com.example.gremlin.annotation.Id;
import com.example.gremlin.annotation.Property;
import com.example.gremlin.annotation.Vertex;
import com.example.gremlin.model.IModel;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sa
 * @date 8.02.2021
 * @time 15:43
 */
public class ModelUtils
{
    public static Map<String, Object> findNonNullPersistentFieldsOfModel(IModel model)
    {
        Map<String, Object> fields = new HashMap<>();
        try
        {
            for (final Field field : FieldUtils.getAllFields(model.getClass()))
            {
                field.setAccessible(true);
                Object value = field.get(model);
                if (value != null && isFieldPersistent(field))
                {
                    fields.put(field.getName(), field.get(model));
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("can not access model field", e);
        }
        return fields;
    }

    public static boolean setField(Object targetObject, Field field, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(targetObject, fieldValue);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static boolean isFieldPersistent(Field field)
    {
        for (Annotation a : field.getAnnotations())
        {
            if (Arrays.asList(Id.class, Property.class).contains(a.annotationType()))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isVertexClass(T model)
    {
        for (Annotation a : model.getClass().getAnnotations())
        {
            if (Arrays.asList(Vertex.class).contains(a.annotationType()))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isEdgeClass(T model)
    {
        for (Annotation a : model.getClass().getAnnotations())
        {
            if (Arrays.asList(Edge.class).contains(a.annotationType()))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isFieldId(Field field)
    {
        for (Annotation a : field.getAnnotations())
        {
            if (Arrays.asList(Id.class).contains(a.annotationType()))
            {
                return true;
            }
        }
        return false;
    }
}
