package com.thomsonreuters.ce.metrics;

import com.codahale.metrics.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class GetStarted {
	static final MetricRegistry metrics = new MetricRegistry();
	static final JmxReporter jmxreporter = JmxReporter.forRegistry(metrics).build();
	
	
	
	public static void main(String args[]) {
		startReport();
		//广播到JMX
		jmxreporter.start();
		
		//Meter用来追踪单位时间内发生某个事件的频率
		Meter requests = metrics.meter("requests");
		requests.mark();
		
		wait5Seconds();
		
		//Gauge用来记录某一时间的具体值
		final ArrayList<String> ArraySample=new ArrayList<String>();
		ArraySample.add("A");
		ArraySample.add("B");
		ArraySample.add("C");
		metrics.register(MetricRegistry.name(GetStarted.class, "ArraySample", "size"),
                new Gauge<Integer>() {

            public Integer getValue() {
                return ArraySample.size();
            }
        });
		
		wait5Seconds();
		ArraySample.add("D");
		
		//Counter是用来追踪累积型数值的工具
		
		final Counter pendingJobs = metrics.counter(MetricRegistry.name(GetStarted.class, "pending-jobs"));
		pendingJobs.inc();
		wait5Seconds();
		pendingJobs.dec();
		
		//Histograms是用来收集正太分布的工具
		final Histogram responseSizes = metrics.histogram(MetricRegistry.name(GetStarted.class, "response-sizes"));
		responseSizes.update(8);
		responseSizes.update(9);
		wait5Seconds();
		responseSizes.update(10);
		responseSizes.update(0);
		wait5Seconds();
		
		//Timer用来测量时间的频率和时常的分布
		final Timer responses = metrics.timer(MetricRegistry.name(GetStarted.class, "responses"));
		
		Timer.Context context=responses.time();
		wait5Seconds();
		context.stop();
								
		context=responses.time();
		try {
			Thread.sleep(8*1000);
		}
		catch(InterruptedException e) {}
		context.stop();
		
		wait5Seconds();
	}
	

	static void startReport() {
		
		//想系统控制台不断的打印metrics中的各种指标
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.build();
		
		reporter.start(1, TimeUnit.SECONDS);
	}
	
	
	
	

	static void wait5Seconds() {
		try {
			Thread.sleep(5*1000);
		}
		catch(InterruptedException e) {}
	}
}