//BATCHING

package com.logiq.processing;
import com.logiq.model.LogLine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogBatcher {
    public List<LogLine> nextBatch(
            Iterator<LogLine> iterator,
            int batchSize
    ) {
        List<LogLine> batch = new ArrayList<>(batchSize);
        while (iterator.hasNext() && batch.size() < batchSize) {
            batch.add(iterator.next());
        }
        return batch;
    }
}
