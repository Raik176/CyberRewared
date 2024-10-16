package org.rhm.test;

import org.junit.jupiter.api.Test;
import org.rhm.util.CyberUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CyberUtilTest {
    @Test
    public void testPrettyTime() {
        assertEquals("00:30", CyberUtil.prettyTime(30));
        assertEquals("01:15", CyberUtil.prettyTime(75));
        assertEquals("10:00", CyberUtil.prettyTime(600));
    }
}
