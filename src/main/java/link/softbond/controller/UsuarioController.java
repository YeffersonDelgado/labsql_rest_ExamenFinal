package link.softbond.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.auth.service.JWTService;
import link.softbond.dto.UsuarioDTO;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
import link.softbond.service.JpaUserDetailsService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private JpaUserDetailsService usuarioService;
    
    private AuthenticationManager authenticationManager;
	private JWTService jwtService;

    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Usuario> registerNewUser(@RequestBody UsuarioDTO usuarioDto) {
        try {
            Usuario newUser = usuarioService.registerNewUser(usuarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody UsuarioDTO userDto) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDto.getUsuario(),
                    userDto.getClave()
                )
            );

            String token = jwtService.create(authentication);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    
}
