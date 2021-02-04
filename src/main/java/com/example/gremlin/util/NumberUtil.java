package com.example.gremlin.util;

import java.util.UUID;

/**
 * @author sa
 * @date 3.02.2021
 * @time 16:31
 */
public class NumberUtil
{
    public static Long randomNumber()
    {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
