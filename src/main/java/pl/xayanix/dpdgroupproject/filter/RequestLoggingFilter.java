package pl.xayanix.dpdgroupproject.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.xayanix.dpdgroupproject.model.dao.RequestLogDAO;
import pl.xayanix.dpdgroupproject.repository.RequestLogRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLogRepository requestLogRepository;
    private final Executor asyncTaskExecutor;

    @Autowired
    public RequestLoggingFilter(RequestLogRepository requestLogRepository){
        this.requestLogRepository = requestLogRepository;
        this.asyncTaskExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            RequestLogDAO requestLog = new RequestLogDAO();
            requestLog.setRequestUrl(request.getRequestURI());
            requestLog.setHttpMethod(request.getMethod());
            requestLog.setIpAddress(request.getRemoteAddr());
            requestLog.setTimestamp(LocalDateTime.now());

            asyncTaskExecutor.execute(() -> requestLogRepository.save(requestLog));
        } catch (Exception e) {
            logger.error("Error logging request: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
