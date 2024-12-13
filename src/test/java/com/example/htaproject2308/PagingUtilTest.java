package com.example.htaproject2308;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.htaproject2308.util.PagingUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PagingUtilTest {

    /* 정상적인 케이스 테스트 */
    @Test
    public void calculatePagingTest() {
        // given
        int page = 1;
        int limit = 10;
        int listcount = 100;

        // when
        PagingUtil.Paging paging = PagingUtil.getPaging(page, limit, listcount);

        // then
        assertEquals(page, paging.getPage());
        assertEquals(limit, paging.getLimit());
        assertEquals(listcount, paging.getListcount());

        int expectedMaxPage = 10;
        assertEquals(expectedMaxPage, paging.getMaxpage());
    }

    /* 예외 케이스 테스트 */
    @Test
    public void testPagingWithNegativeValues() {
        assertThrows(IOException.class, () -> {
            PagingUtil.getPaging(-1, 10, 100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            PagingUtil.getPaging(1, -10, 100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            PagingUtil.getPaging(1, 10, -100);
        });
    }

}
