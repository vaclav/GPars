
package org.gparallelizer.samples.dataflow

import org.gparallelizer.dataflow.DataFlows
import static org.gparallelizer.dataflow.DataFlow.*
import java.util.concurrent.Executors

static final LIMIT = 3*100*1000

final df = new DataFlows(LIMIT+1, 0.9f, DataFlows.MAX_SEGMENTS)

final many = 1..LIMIT

def scheduler = Executors.newFixedThreadPool (20)

scheduler.execute { df.result = many.collect{
    def v = df[it]
    df.remove it
    v
}.sum() }

// each in a newly started actor:
many.each { num ->
    scheduler.execute {
      df[num] = 1
      println num
    }
}

// Wait for the result to be available
// This is in the main thread, which is DF unaware!
assert many.size() == df.result
println "done"