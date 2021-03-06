package com.basic.benchmark;

import com.basic.benchmark.task.ReceiverTask;
import com.basic.util.BenchmarkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wang on 17-11-12.
 * java -cp nioAction-1.0-SNAPSHOT.jar com.basic.benchmark.JavaNIOParallelClient
 */
public class JavaNIOParallelClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(BenchmarkConstants.transferThreadNum);
    private static Logger logger= LoggerFactory.getLogger(JavaNIOParallelClient.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i = 0; i< BenchmarkConstants.transferThreadNum; i++){
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress(BenchmarkConstants.hostname, 9898));
            ReceiverTask receiverTask=new ReceiverTask(sChannel);
            executorService.submit(receiverTask);
        }
        long startTimeMills = System.currentTimeMillis();

        executorService.shutdown();
        executorService.awaitTermination(10000000, TimeUnit.SECONDS);
        long endTimeMills = System.currentTimeMillis();
        logger.info("delayTime: "+(endTimeMills-startTimeMills));
    }
}
