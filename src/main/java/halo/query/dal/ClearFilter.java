package halo.query.dal;

import javax.servlet.*;
import java.io.IOException;

/**
 * 进行线程数据清理
 * Created by akwei on 6/15/15.
 */
public class ClearFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        DALStatus.remove();
        HaloData.remove();
    }

    @Override
    public void destroy() {

    }
}
