package pl.xayanix.dpdgroupproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncConfig {


	/**
	 * Configuration for asynchronous task execution using a thread pool.
	 * Allows controlling application performance by adjusting thread pool parameters.
	 *
	 * Configuration settings:
	 * - Core Pool Size: Specifies the initial number of threads in the pool.
	 *   Value: 10
	 *
	 * - Max Pool Size: Specifies the maximum number of threads that can be
	 *   used concurrently by the pool.
	 *   Value: 20
	 *
	 * - Queue Capacity: Specifies the maximum number of tasks that can be pending
	 *   execution by the threads in the pool when all threads are busy.
	 *   Value: 1000
	 *
	 * - Thread Name Prefix: Prefix used for thread names in the pool, aiding in
	 *   identifying threads associated with asynchronous tasks.
	 *   Value: "Async-"
	 *
	 * Adjust these values based on your application's requirements and expected workload.
	 * Optimizing the thread pool configuration can significantly impact application performance,
	 * especially for handling long-running tasks or high asynchronous traffic.
	 *
	 * Usage Example:
	 *
	 * <pre>{@code
	 * @Configuration
	 * @EnableAsync
	 * public class AsyncConfig {
	 *
	 *     @Bean(name = "asyncExecutor")
	 *     public Executor asyncExecutor() {
	 *         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	 *         executor.setCorePoolSize(10);
	 *         executor.setMaxPoolSize(20);
	 *         executor.setQueueCapacity(1000);
	 *         executor.setThreadNamePrefix("Async-");
	 *         executor.initialize();
	 *         return executor;
	 *     }
	 * }
	 * }</pre>
	 */
	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix("Async-");
		executor.initialize();

		return executor;
	}

}
