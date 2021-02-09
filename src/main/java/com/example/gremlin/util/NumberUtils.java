package com.example.gremlin.util;

import java.util.UUID;

/**
 * @author sa
 * @date 3.02.2021
 * @time 16:31
 */
public class NumberUtils
{
    public static String randomNumber()
    {
        Long number = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return number.toString();
    }
}
