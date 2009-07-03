package org.gparallelizer.samples

import java.util.concurrent.CountDownLatch
import org.gparallelizer.actors.pooledActors.AbstractPooledActor
import org.gparallelizer.actors.pooledActors.PooledActor
import org.gparallelizer.actors.pooledActors.PooledActors

PooledActors.defaultPooledActorGroup.resize 23

//Messages
private final class FileToSort { String fileName }
private final class SortResult { String fileName; List<String> words }

//Worker actor
final class WordSortActor extends AbstractPooledActor {

    private List<String> sortedWords(String fileName) {
        parseFile(fileName).sort {it.toLowerCase()}
    }

    private List<String> parseFile(String fileName) {
        List<String> words = []
        new File(fileName).splitEachLine(' ') {words.addAll(it)}
        return words
    }

    void act() {
        loop {
            react {message ->
                switch (message) {
                    case FileToSort:
//                        println "Sorting file=${message.fileName} on thread ${Thread.currentThread().name}"
                        reply new SortResult(fileName: message.fileName, words: sortedWords(message.fileName))
                }
            }
        }
    }
}

//Master actor
final class SortMaster extends AbstractPooledActor {

    String docRoot = '/'
    int numActors = 1

    List<List<String>> sorted = []
    private CountDownLatch startupLatch = new CountDownLatch(1)
    private CountDownLatch doneLatch

    private void beginSorting() {
        int cnt = sendTasksToWorkers()
        doneLatch = new CountDownLatch(cnt)
    }

    private List createWorkers() {
        return (1..numActors).collect {new WordSortActor().start()}
    }

    private int sendTasksToWorkers() {
        List<PooledActor> workers = createWorkers()
        int cnt = 0
        new File(docRoot).eachFile {
            workers[cnt % numActors] << new FileToSort(fileName: it)
            cnt += 1
        }
        return cnt
    }

    public void waitUntilDone() {
        startupLatch.await()
        doneLatch.await()
    }

    void act() {
        beginSorting()
        startupLatch.countDown()
        loop {
            react {
                switch (it) {
                    case SortResult:
                        sorted << it.words
                        doneLatch.countDown()
//                        println "Received results for file=${it.fileName}"
                }
            }
        }
    }
}

//start the actors to sort words
//todo change the folder name
def master = new SortMaster(docRoot: 'C:/dev/TeamCity/logs/', numActors: 21).start()
master.waitUntilDone()
final long t1 = System.currentTimeMillis()
master = new SortMaster(docRoot: 'C:/dev/TeamCity/logs/', numActors: 21).start()
master.waitUntilDone()
final long t2 = System.currentTimeMillis()
println 'Done ' + (t2 - t1)
//println master.sorted