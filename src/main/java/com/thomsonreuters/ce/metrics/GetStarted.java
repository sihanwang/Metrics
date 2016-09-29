package com.thomsonreuters.ce.metrics;

import com.codahale.metrics.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class GetStarted {
	static final MetricRegistry metrics = new MetricRegistry();
	static final JmxReporter jmxreporter = JmxReporter.forRegistry(metrics).build();
	
	
	
	public static void main(String args[]) {
		startReport();
		//�㲥��JMX
		jmxreporter.start();
		
		//Meter����׷�ٵ�λʱ���ڷ���ĳ���¼���Ƶ��
		Meter requests = metrics.meter("requests");
		requests.mark();
		
		wait5Seconds();
		
		//Gauge������¼ĳһʱ��ľ���ֵ
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
		
		//Counter������׷���ۻ�����ֵ�Ĺ���
		
		final Counter pendingJobs = metrics.counter(MetricRegistry.name(GetStarted.class, "pending-jobs"));
		pendingJobs.inc();
		wait5Seconds();
		pendingJobs.dec();
		
		//Histograms�������ռ���̫�ֲ��Ĺ���
		final Histogram responseSizes = metrics.histogram(MetricRegistry.name(GetStarted.class, "response-sizes"));
		responseSizes.update(8);
		responseSizes.update(9);
		wait5Seconds();
		responseSizes.update(10);
		responseSizes.update(0);
		wait5Seconds();
		
		//Timer��������ʱ���Ƶ�ʺ�ʱ���ķֲ�
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
		
		//��ϵͳ����̨���ϵĴ�ӡmetrics�еĸ���ָ��
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