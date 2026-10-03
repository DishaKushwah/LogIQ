package com.logiq.processing;
import com.logiq.model.LogLine;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class LogBatcherTest {
    @Test
    void shouldCreateBatchesOfRequestedSize() {
        List<LogLine> lines = List.of(
                new LogLine(1, "line-1"),
                new LogLine(2, "line-2"),
                new LogLine(3, "line-3"),
                new LogLine(4, "line-4"),
                new LogLine(5, "line-5"),
                new LogLine(6, "line-6"),
                new LogLine(7, "line-7")
        );
        LogBatcher batcher = new LogBatcher();
        var iterator = lines.iterator();
        List<LogLine> batch1 = batcher.nextBatch(iterator, 3);
        List<LogLine> batch2 = batcher.nextBatch(iterator, 3);
        List<LogLine> batch3 = batcher.nextBatch(iterator, 3);
        List<LogLine> batch4 = batcher.nextBatch(iterator, 3);
        assertEquals(3, batch1.size());
        assertEquals(3, batch2.size());
        assertEquals(1, batch3.size());
        assertEquals(0, batch4.size());
        assertEquals(1, batch1.get(0).lineNumber());
        assertEquals(4, batch2.get(0).lineNumber());
        assertEquals(7, batch3.get(0).lineNumber());
    }
}