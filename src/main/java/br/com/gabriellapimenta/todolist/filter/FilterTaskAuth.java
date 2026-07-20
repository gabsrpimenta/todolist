package br.com.gabriellapimenta.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabriellapimenta.todolist.user.IUserRespository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRespository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {

            var authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Basic")) {
                response.sendError(401, "Acesso negado. Token de autenticação não fornecido.");
                return;
            }

            var authEncoded = authorization.substring("Basic ".length()).trim();

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            var authString =  new String(authDecoded);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "Acesso negado. Usuário não encontrado.");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Acesso negado. Senha incorreta.");
                }
            }

        } else  {
            filterChain.doFilter(request, response);
        }
    }
}