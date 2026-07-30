//package com.dbtraining.reconx.service;
//import com.dbtraining.reconx.model.*;
//
//import java.util.DoubleSummaryStatistics;
//import java.util.List;
//import java.util.Map;
//import org.junit.jupiter.api.Test;
//import static java.util.stream.Collectors.groupingBy;
//import static java.util.stream.Collectors.summarizingDouble;
//class TradeAnalyticsServiceTest {
//
//    @Test
//    void shouldCalculateNotionalByCounterparty() {
//
//        // Arrange
//        List<TradeType> trades = createSampleTrades();
//
//        // Act
//        Map<Long, NotionalSummary> result =
//                service.notionalByCounterparty(trades);
//
//        // Assert
//        assertEquals(2, result.size());
//
//        assertEquals(2,
//                result.get(101L).count());
//
//        assertEquals(
//                new BigDecimal("1500"),
//                result.get(101L).totalNotional());
//
//        assertEquals(1,
//                result.get(102L).count());
//
//        assertEquals(
//                new BigDecimal("200"),
//                result.get(102L).totalNotional());
//    }
//}