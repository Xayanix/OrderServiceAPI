package pl.xayanix.dpdgroupproject.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.xayanix.dpdgroupproject.model.dao.RequestLogDAO;
import pl.xayanix.dpdgroupproject.repository.RequestLogRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    RequestLogRepository requestLogRepository;
    Executor asyncTaskExecutor = Executors.newSingleThreadExecutor();

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
