package pl.xayanix.dpdgroupproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncConfig {

	@Value("${async.executor.core-pool-size}")
	private int corePoolSize;

	@Value("${async.executor.max-pool-size}")
	private int maxPoolSize;

	@Value("${async.executor.queue-capacity}")
	private int queueCapacity;

	@Value("${async.executor.thread-name-prefix}")
	private String threadNamePrefix;


	/**
	 * Configures Spring's asynchronous executor.
	 *
	 * @return Asynchronous executor configured with specified parameters.
	 */
	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();

		return executor;
	}

}
